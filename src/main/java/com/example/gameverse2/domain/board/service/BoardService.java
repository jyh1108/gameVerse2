package com.example.gameverse2.domain.board.service;

import com.example.gameverse2.domain.board.dao.BoardRepository;
import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.member.dao.MemberRepository;
import com.example.gameverse2.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;



    public Board createBoard(String boardTitle, String boardText, String boardCode, String tag, Member author) {
        Board board = new Board();
        board.setBoardCode(boardCode);
        board.setTag(tag);
        board.setBoardTitle(boardTitle);
        board.setBoardText(boardText);
        board.setCreateDate(new Date());
        board.setLikeCount(0);
        board.setReadCount(0);
        board.setBoardDelete('N');
        board.setAuthor(author);
        this.boardRepository.save(board);
        return board;
    }
}
