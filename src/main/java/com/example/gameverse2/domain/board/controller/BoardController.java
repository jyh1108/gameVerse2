package com.example.gameverse2.domain.board.controller;

import com.example.gameverse2.domain.board.dao.BoardRepository;
import com.example.gameverse2.domain.board.dto.BoardDto;
import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.board.service.BoardService;
import com.example.gameverse2.domain.member.entity.Member;
import com.example.gameverse2.domain.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

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
    public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        Page<Board> paging = this.boardService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
//        List<Board> boardList = this.boardService.getList();
//        model.addAttribute("board", boardList);
        return "domain/board/board_list";
    }


    @GetMapping("/detail/{boardNo}")
    public String detail(Model model, @PathVariable Long boardNo){
        Board board = this.boardService.getBoard(boardNo);
        model.addAttribute("board", board);
        return "domain/board/board_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{boardNo}")
    public String boardModify(BoardDto boardDto, @PathVariable("boardNo") Long boardNo, Principal principal) {
        Board board = this.boardService.getBoard(boardNo);
        if(!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        boardDto.setBoardTitle(board.getBoardTitle());
        boardDto.setBoardText(board.getBoardText());
        boardDto.setBoardCode(board.getBoardCode());
        boardDto.setTag(board.getTag());
        return "domain/board/board_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{boardNo}")
    public String boardModify(@Valid BoardDto boardDto, BindingResult bindingResult,
                                 Principal principal, @PathVariable("boardNo") Long boardNo) {
        if (bindingResult.hasErrors()) {
            return "domain/board/board_form";
        }
        Board board = this.boardService.getBoard(boardNo);
        if (!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        this.boardService.modify(board, boardDto.getBoardTitle(), boardDto.getBoardText());
        return String.format("redirect:/board/detail/%s", boardNo);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{boardNo}")
    public String questionDelete(Principal principal, @PathVariable("boardNo") Long boardNo) {
        Board board = this.boardService.getBoard(boardNo);
        if (!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.boardService.delete(board);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{boardNo}")
    public String questionVote(Principal principal, @PathVariable("boardNo") Long boardNo) {
        Board board = this.boardService.getBoard(boardNo);
        Member member = this.memberService.getMember(principal.getName());
        this.boardService.vote(board, member);
        return String.format("redirect:/board/detail/%s", boardNo);
    }
}