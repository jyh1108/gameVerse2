package com.example.gameverse2.domain.board.service;

import com.example.gameverse2.domain.board.dao.BoardRepository;
import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.member.dao.MemberRepository;
import com.example.gameverse2.domain.member.entity.DataNotFoundException;
import com.example.gameverse2.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        board.setCreateDate(LocalDateTime.now());
        board.setLikeCount(0);
        board.setReadCount(0);
        board.setBoardDelete('N');
        board.setAuthor(author);
        this.boardRepository.save(board);
        return board;
    }

    public List<Board> getList() {
        return this.boardRepository.findAll();
    }

    public Board getBoard(Long boardNo){
        Optional<Board> board = this.boardRepository.findById(boardNo);
        if(board.isPresent()){
            return board.get();
        }else {
            throw new DataNotFoundException("board not found");
        }
    }

    public void modify(Board board, String boardTitle, String boardText) {
        board.setBoardTitle(boardTitle);
        board.setBoardText(boardText);
        board.setUpdateDate(LocalDateTime.now());
        this.boardRepository.save(board);
    }


    public void delete(Board board) {
        this.boardRepository.delete(board);
    }
}
