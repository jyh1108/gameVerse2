package com.example.gameverse2;

import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class mainController {

    private final BoardService boardService;
    @GetMapping("/")
    public String mainPage(Model model) {
        model.addAttribute("code1Top5", boardService.getTop5BoardsByCode("1")); // 롤
        model.addAttribute("code2Top5", boardService.getTop5BoardsByCode("2")); // 발로란트
        model.addAttribute("code3Top5", boardService.getTop5BoardsByCode("3")); // 오버워치

        List<Board> latestTop5Boards = boardService.getLatestTop5Boards();
        model.addAttribute("latestBoards", latestTop5Boards);
        return "/main/main";
    }
}
