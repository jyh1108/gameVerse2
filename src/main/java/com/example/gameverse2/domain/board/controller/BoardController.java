package com.example.gameverse2.domain.board.controller;

import com.example.gameverse2.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/Board/")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/create")
    public String BoardCreate() {
        return "board_form";
    }
}
