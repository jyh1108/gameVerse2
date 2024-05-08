package com.example.gameverse2.domain.board.controller;

import com.example.gameverse2.domain.board.dao.BoardRepository;
import com.example.gameverse2.domain.board.dto.BoardDto;
import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.board.service.BoardService;
import com.example.gameverse2.domain.member.entity.Member;
import com.example.gameverse2.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final MemberService memberService;
    private final BoardRepository boardRepository;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String BoardCreate() {
        return "domain/board/board_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createBoard(@Valid BoardDto boardDto, BindingResult bindingResult, Principal principal) {
        Member member = this.memberService.getMember(principal.getName());

        boardService.createBoard(boardDto.getBoardTitle(), boardDto.getBoardText()
                , boardDto.getBoardCode(), boardDto.getTag(), member);

        return "redirect:/";
    }

    @GetMapping("/list")
    public String list(Model model) {
        List<Board> boardList = this.boardService.getList();
        model.addAttribute("board", boardList);
        return "domain/board/board_list";
    }

    @GetMapping("/detail/{boardNo}")
    public String detail(Model model, @PathVariable Long boardNo){
        Board board = this.boardService.getBoard(boardNo);
        model.addAttribute("board", board);
        return "domain/board/board_detail";
    }
}