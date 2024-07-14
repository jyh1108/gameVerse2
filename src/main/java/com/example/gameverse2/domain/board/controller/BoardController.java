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
    public String list(@RequestParam(value = "kw", required = false) String kw,
                       @RequestParam(value = "tag", required = false) String tag,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       Model model) {
        // 페이지 정보 및 검색 키워드와 태그 필터링 정보로 게시글 목록 가져오기
        Page<Board> paging = boardService.getBoardList(kw, tag, page);

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("tag", tag);

        return "domain/board/board_list";
    }
    @GetMapping("/list/{boardCode}")
    public String booardlist(@RequestParam(value = "kw", required = false) String kw,
                             @RequestParam(value = "tag", required = false) String tag,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             Model model, @PathVariable String boardCode) {
        // 페이지 정보 및 검색 키워드와 태그 필터링 정보로 게시글 목록 가져오기
        Page<Board> paging = boardService.getBoardCodeList(kw, tag, page,boardCode);

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("tag", tag);

        return "domain/board/board_List1";
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
        return "domain/board/board_mdform";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{boardNo}")
    public String boardModify(@Valid BoardDto boardDto, BindingResult bindingResult,
                                 Principal principal, @PathVariable("boardNo") Long boardNo) {
        if (bindingResult.hasErrors()) {
            return "domain/board/board_mdform";
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
    public String boardDelete(Principal principal, @PathVariable("boardNo") Long boardNo) {
        Board board = this.boardService.getBoard(boardNo);
        if (!board.getAuthor().getLoginId().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        this.boardService.delete(board);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{boardNo}")
    public String boardVote(Principal principal, @PathVariable("boardNo") Long boardNo) {
        Board board = this.boardService.getBoard(boardNo);
        Member member = this.memberService.getMember(principal.getName());
        this.boardService.vote(board, member);
        return String.format("redirect:/board/detail/%s", boardNo);
    }

}