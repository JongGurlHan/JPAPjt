package com.JGHan.JPAPjt.repository;

import com.JGHan.JPAPjt.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
