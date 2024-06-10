package com.example.gameverse2.domain.board.service;

import com.example.gameverse2.domain.board.dao.BoardRepository;
import com.example.gameverse2.domain.board.entity.Board;
import com.example.gameverse2.domain.member.dao.MemberRepository;
import com.example.gameverse2.domain.member.entity.DataNotFoundException;
import com.example.gameverse2.domain.member.entity.Member;
import com.example.gameverse2.domain.reply.entity.Reply;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public Page<Board> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.boardRepository.findAllByKeyword(kw, pageable);
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
        board.setDeleteDate(LocalDateTime.now());
        board.setBoardDelete('Y');
        this.boardRepository.save(board);
    }

    public void vote(Board board, Member member) {
        if (!board.getVoter().contains(member)) {
            board.getVoter().add(member);
            board.setLikeCount(board.getLikeCount() + 1); // likeCount를 증가시킵니다.
            this.boardRepository.save(board);
        }
    }

    private Specification<Board> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Board> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Board, Member> u1 = q.join("author", JoinType.LEFT);
                Join<Board, Reply> a = q.join("replyList", JoinType.LEFT);
                Join<Reply, Member> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("boardTitle"), "%" + kw + "%"), // 제목
                        cb.like(q.get("boardText"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("author"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("replyContent"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("author"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }
}
