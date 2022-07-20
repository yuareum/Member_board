package com.its.board.controller;

import com.its.board.common.PagingConst;
import com.its.board.dto.BoardDTO;
import com.its.board.dto.CommentDTO;
import com.its.board.service.BoardService;
import com.its.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    private final CommentService commentService;

    @GetMapping("/save-form")
    public String saveForm(){
        return "boardPages/save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException {
        Long id = boardService.save(boardDTO);
        return "redirect:/board/"+id;

    }
    @GetMapping
    public String findAll(@PageableDefault(page = 1) Pageable pageable, Model model){
        Page<BoardDTO> boardList = boardService.paging(pageable);
        model.addAttribute("boardList", boardList);
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / PagingConst.BLOCK_LIMIT))) - 1) * PagingConst.BLOCK_LIMIT + 1;
        int endPage = ((startPage + PagingConst.BLOCK_LIMIT - 1) < boardList.getTotalPages()) ? startPage + PagingConst.BLOCK_LIMIT - 1 : boardList.getTotalPages();
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "boardPages/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        BoardDTO findDTO = boardService.detail(id);
        model.addAttribute("board", findDTO);
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);
        return "boardPages/detail";

    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        boardService.delete(id);
        return "redirect:/board";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") Long id, Model model){
        BoardDTO findDTO = boardService.findById(id);
        model.addAttribute("updateBoard", findDTO);
        return "boardPages/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO){
        boardService.update(boardDTO);
        return "redirect:/board/" + boardDTO.getId();
    }
    @GetMapping("/search")
    public String search(@RequestParam("searchType") String searchType, @RequestParam("q") String q, Model model, @PageableDefault(page = 1) Pageable pageable){
        if(Objects.equals(searchType, "boardTitle")){
            List<BoardDTO> searchList = boardService.searchTitle(q);
            model.addAttribute("searchList", searchList);
        }
        else if(Objects.equals(searchType, "boardWriter")){
            List<BoardDTO> searchList = boardService.searchWriter(q);
            model.addAttribute("searchList", searchList);
        }
        else if(Objects.equals(searchType, "boardTitleOrBoardWriter")){
            List<BoardDTO> searchList = boardService.search(q);
            model.addAttribute("searchList", searchList);
        }
        return "boardPages/searchList";
    }
}
