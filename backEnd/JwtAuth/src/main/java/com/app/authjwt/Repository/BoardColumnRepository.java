package com.app.authjwt.Repository;

import com.app.authjwt.Model.Board;
import com.app.authjwt.Model.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardColumnRepository extends JpaRepository<BoardColumn,Long> {
    Optional<BoardColumn> findFirstByBoardOrderByPositionAsc(Board board);
    List<BoardColumn> findByBoardId(Long boardId);

}
