package com.its.board.controller;

import com.its.board.common.PagingConst;
import com.its.board.dto.MemberDTO;
import com.its.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save-form")
    public String saveForm(){
        return "/memberPages/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) throws IOException {
        memberService.save(memberDTO);
        return "redirect:/member/login-form";
    }

    @PostMapping("/dup-check")
    public @ResponseBody String dupCheck(@RequestParam("memberEmail") String memberEmail){
        String checkResult = memberService.dupCheck(memberEmail);
        return checkResult;
    }

    @GetMapping("/login-form")
    public String loginForm(){
        return "/memberPages/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            session.setAttribute("loginId", loginResult.getId());
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            session.setAttribute("loginPassword", loginResult.getMemberPassword());
            return "redirect:/board";
        }
        else {
            return "memberPages/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/admin")
    public String admin(Model model,HttpSession session){
        MemberDTO memberDTO = memberService.findByMemberEmail((String) session.getAttribute("loginEmail"));
        if("admin".equals(memberDTO.getMemberEmail())) {
            model.addAttribute("member", memberDTO);
            return "/memberPages/admin";
        }
        else{
            return "redirect:/";
        }
    }
    @GetMapping
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<MemberDTO> memberList = memberService.paging(pageable);
        model.addAttribute("memberList", memberList);
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.BLOCK_LIMIT))) - 1) * PagingConst.BLOCK_LIMIT + 1;
        int endPage = ((startPage + PagingConst.BLOCK_LIMIT - 1) < memberList.getTotalPages()) ? startPage + PagingConst.BLOCK_LIMIT - 1 : memberList.getTotalPages();
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "memberPages/list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model){
        MemberDTO memberDTO = memberService.findById(id);
        model.addAttribute("member", memberDTO);
        return "memberPages/detail";
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAjax(@PathVariable("id") Long id){
        memberService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
