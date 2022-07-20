package com.its.board.repository;

import com.its.board.dto.BoardDTO;
import com.its.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits= b.boardHits+1 where b.id= :id")
    void boardHits(@Param("id") Long id);

    List<BoardEntity> findByBoardTitleContainingOrBoardWriterContaining(String q, String q1);

    List<BoardEntity> findByBoardWriterContaining(String q);

    List<BoardEntity> findByBoardTitleContaining(String q);
}
