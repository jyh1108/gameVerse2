package com.example.gameverse2.domain.reply.controller;

import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.board.service.BoardService;
import com.example.gameverse2.domain.reply.service.ReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reply")
public class ReplyController {

    private final BoardService boardService;
    private final ReplyService replyService;

    @PostMapping("/create/{BoardNo}")
    public String createAnswer(Model model, @PathVariable("BoardNo") Long BoardNo, @RequestParam(value="replyContent") String replyContent) {
        Board board = this.boardService.getBoard(BoardNo);
        this.replyService.create(board, replyContent);
        return String.format("redirect:/board/detail/%s", BoardNo);
    }
}
