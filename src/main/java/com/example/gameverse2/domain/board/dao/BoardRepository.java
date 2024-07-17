package com.example.gameverse2.domain.board.dao;

import com.example.gameverse2.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query("SELECT b FROM Board b WHERE b.boardDelete = 'N'")
    Page<Board> findAllActiveBoards(Pageable pageable);
    @Query("SELECT b FROM Board b WHERE b.boardDelete = 'N'")
    Page<Board> findboardAll(Pageable pageable);

    @Query("select "
            + "distinct q "
            + "from Board q "
            + "left outer join Member u1 on q.author=u1 "
            + "left outer join Reply a on a.board=q "
            + "left outer join Member u2 on a.author=u2 "
            + "where "
            + "   q.boardTitle like %:kw% "
            + "   or q.boardText like %:kw% "
            + "   or u1.nickName like %:kw% "
            + "   or a.replyContent like %:kw% "
            + "   or u2.nickName like %:kw% ")
    Page<Board> findAllByKeyword(@Param("kw") String kw, Pageable pageable);

    List<Board> findTop5ByBoardCodeOrderByLikeCountDesc(String boardCode);

    List<Board> findTop5ByOrderByCreateDateDesc();  // 최신글 5개를 가져오는 메서드


    Page<Board> findByBoardTitleContainingAndTag(String kw, String tag, Pageable pageable);

    Page<Board> findByBoardTitleContaining(String kw, Pageable pageable);

    Page<Board> findByTag(String tag, Pageable pageable);

    Page<Board> findByBoardCode(String boardCode, Pageable pageable);


    Page<Board> findByBoardTitleContainingAndTagAndBoardCode(String kw, String tag, String boardCode, Pageable pageable);

    Page<Board> findByBoardTitleContainingAndBoardCode(String kw, String boardCode, Pageable pageable);

    Page<Board> findByTagAndBoardCode(String tag, String boardCode, Pageable pageable);
}
