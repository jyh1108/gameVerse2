package com.example.gameverse2.domain.board.dao;

import com.example.gameverse2.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b FROM Board b WHERE b.boardDelete = 'N'")
    List<Board> findAllActiveBoards();
}
