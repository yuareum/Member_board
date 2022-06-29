package com.its.board.controller;

import com.its.board.dto.MemberDTO;
import com.its.board.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
        return "redirect:/board/";
    }

    @PostMapping("/dup-check")
    public @ResponseBody String dupCheck(@RequestParam("memberId") String memberId){
        String checkResult = memberService.dupCheck(memberId);
        return checkResult;
    }
}
