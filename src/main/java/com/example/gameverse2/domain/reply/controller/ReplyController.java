package com.example.gameverse2.domain.reply.controller;

import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.board.service.BoardService;
import com.example.gameverse2.domain.member.entity.Member;
import com.example.gameverse2.domain.member.service.MemberService;
import com.example.gameverse2.domain.reply.dto.ReplyCreateForm;
import com.example.gameverse2.domain.reply.entity.Reply;
import com.example.gameverse2.domain.reply.service.ReplyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

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
        this.replyService.create(board, replyCreateForm.getReplyContent(), member);
        return String.format("redirect:/board/detail/%s", BoardNo);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{ReplyNo}")
    public String ReplyModify(@PathVariable("ReplyNo") Long ReplyNo, Principal principal, Model model) {
        Reply reply = this.replyService.getReply(ReplyNo);
        if (!reply.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        ReplyCreateForm replyCreateForm = new ReplyCreateForm();
        replyCreateForm.setReplyContent(reply.getReplyContent());
        model.addAttribute("replyCreateForm", replyCreateForm);
        model.addAttribute("replyNo", ReplyNo);
        return "domain/reply/reply_form";
    }


    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{ReplyNo}")
    public String ReplyModify(@Valid ReplyCreateForm replyCreateForm, BindingResult bindingResult,
                              @PathVariable("ReplyNo") Long ReplyNo, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "domain/reply/reply_form";
        }
        Reply reply = this.replyService.getReply(ReplyNo);
        if (!reply.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.replyService.modify(reply, replyCreateForm.getReplyContent());
        return String.format("redirect:/board/detail/%s", reply.getBoard().getBoardNo());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{ReplyNo}")
    public String ReplyDelete(Principal principal, @PathVariable("ReplyNo") Long ReplyNo) {
        Reply reply = this.replyService.getReply(ReplyNo);
        if (!reply.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.replyService.delete(reply);
        return "redirect:/";
    }
}
