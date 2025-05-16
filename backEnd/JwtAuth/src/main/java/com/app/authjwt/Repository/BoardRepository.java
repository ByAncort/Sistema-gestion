package com.app.authjwt.Repository;

import com.app.authjwt.Model.Board;
import com.app.authjwt.Model.BoardColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long> {
    @Query("SELECT DISTINCT b FROM Board b " +
            "LEFT JOIN FETCH b.columns c " +
            "LEFT JOIN FETCH c.tasks t " +
            "WHERE b.id = :boardId")
    Optional<Board> findBoardWithColumnsAndTasks(@Param("boardId") Long boardId);

}
