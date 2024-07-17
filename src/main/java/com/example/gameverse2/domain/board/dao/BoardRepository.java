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

    List<Board> findTop5ByBoardDeleteOrderByCreateDateDesc(Character boardDelete);

    Page<Board> findByBoardTitleContainingAndTagAndBoardDelete(String boardTitle, String tag, Character boardDelete, Pageable pageable);
    Page<Board> findByBoardTitleContainingAndBoardDelete(String boardTitle, Character boardDelete, Pageable pageable);
    Page<Board> findByTagAndBoardDelete(String tag, Character boardDelete, Pageable pageable);
    Page<Board> findByBoardDelete(Character boardDelete, Pageable pageable);

    Page<Board> findByBoardTitleContainingAndTagAndBoardCodeAndBoardDelete(String boardTitle, String tag, String boardCode, Character boardDelete, Pageable pageable);
    Page<Board> findByBoardTitleContainingAndBoardCodeAndBoardDelete(String boardTitle, String boardCode, Character boardDelete, Pageable pageable);
    Page<Board> findByTagAndBoardCodeAndBoardDelete(String tag, String boardCode, Character boardDelete, Pageable pageable);
    Page<Board> findByBoardCodeAndBoardDelete(String boardCode, Character boardDelete, Pageable pageable);
}
