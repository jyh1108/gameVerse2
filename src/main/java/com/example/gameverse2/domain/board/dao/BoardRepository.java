package com.example.gameverse2.domain.board.dao;

import com.example.gameverse2.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
