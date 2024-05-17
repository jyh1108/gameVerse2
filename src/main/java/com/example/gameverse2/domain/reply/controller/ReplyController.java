package com.example.gameverse2.domain.reply.controller;

import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.board.service.BoardService;
import com.example.gameverse2.domain.member.entity.Member;
import com.example.gameverse2.domain.member.service.MemberService;
import com.example.gameverse2.domain.reply.dto.ReplyCreateForm;
import com.example.gameverse2.domain.reply.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final BoardService boardService;
    private final ReplyService replyService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{BoardNo}")
    public String createAnswer(Model model, @PathVariable("BoardNo") Long BoardNo
                               , @Valid ReplyCreateForm replyCreateForm,
                               BindingResult bindingResult, Principal principal) {
        Board board = this.boardService.getBoard(BoardNo);
        Member member = this.memberService.getMember(principal.getName());
        if (bindingResult.hasErrors()) {
            model.addAttribute("board", board);
            return "domain/board/board_detail";
        }
        this.replyService.create(board, replyCreateForm.getReplyContent(),member);
        return String.format("redirect:/board/detail/%s", BoardNo);
    }
}
