package com.app.authjwt.Service;

import com.app.authjwt.Model.*;
import com.app.authjwt.Repository.BoardColumnRepository;
import com.app.authjwt.Repository.BoardRepository;
import com.app.authjwt.Repository.WorkspaceRepository;
import com.app.authjwt.dto.BoardColumnDto;
import com.app.authjwt.dto.BoardDto;
import com.app.authjwt.dto.ServiceResult;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardColumnRepository columnRepository;
    private final WorkspaceRepository workspaceRepository;


    public ServiceResult<BoardDto> createBoardWithDefaultColumns(Long workspaceId, BoardDto boardName) {
        try {
            Workspace workspace = workspaceRepository.findById(workspaceId)
                    .orElseThrow(() -> new RuntimeException("Workspace no encontrado"));

            Board board = new Board();
            board.setName(boardName.getName());
            board.setWorkspace(workspace);
            Board savedBoard = boardRepository.save(board);

            List<BoardColumn> defaultColumns = List.of(
                    createColumn(savedBoard, "To Do", 0),
                    createColumn(savedBoard, "In Progress", 1),
                    createColumn(savedBoard, "Done", 2)
            );
            boardRepository.save(savedBoard);
            return new ServiceResult<>(
                    BoardDto.builder()
                            .name(savedBoard.getName())
                            .column(defaultColumns.stream()
                                    .map(boardColumn -> new BoardColumnDto(
                                            boardColumn.getId(),
                                            boardColumn.getName(),
                                            boardColumn.getPosition()))
                                    .collect(Collectors.toList())
                            )
                            .build());


        } catch (DataAccessException ex) {
            return new ServiceResult<>(List.of("Error de acceso a datos: " + ex.getRootCause().getMessage()));

        } catch (Exception ex) {
            return new ServiceResult<>(List.of("Error interno del servidor: " + ex.getMessage()));
        }
    }

    private BoardColumn createColumn(Board board, String name, int position) {
        BoardColumn column = new BoardColumn();
        column.setName(name);
        column.setPosition(position);
        column.setBoard(board);
        return columnRepository.save(column);
    }

    public ServiceResult<BoardDto> mostrarBoardWorkspace(Long id){
        try {
         Board board=boardRepository.findById(id)
                 .orElseThrow(() -> new RuntimeException("board no encontrado"));
         List<BoardColumn> columns=board.getColumns();

         BoardDto dtoFinal= BoardDto.builder()
                 .id(board.getId())
                 .name(board.getName())
                 .column(
                         columns.stream()
                                 .map(boardColumn -> new BoardColumnDto(
                                         boardColumn.getId(),
                                         boardColumn.getName(),
                                         boardColumn.getPosition()

                                 ))
                                 .collect(Collectors.toList())

                 )
                 .build();
         return new ServiceResult<>(dtoFinal);

        }catch (Exception e){
            return new ServiceResult<>(List.of("Error al recuperar Board: " + e.getMessage()));
        }
    }


    @Transactional
    public void reorderColumns(Long boardId, List<Long> newColumnOrder) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        Map<Long, Integer> positionMap = new HashMap<>();
        for (int i = 0; i < newColumnOrder.size(); i++) {
            positionMap.put(newColumnOrder.get(i), i);
        }

        board.getColumns().forEach(column -> {
            if (positionMap.containsKey(column.getId())) {
                column.setPosition(positionMap.get(column.getId()));
            }
        });

        boardRepository.save(board);
    }

    @Transactional
    public BoardColumn addNewColumnToBoard(Long boardId, String columnName, Integer position) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // Adjust positions for existing columns
        board.getColumns().stream()
                .filter(c -> c.getPosition() >= position)
                .forEach(c -> c.setPosition(c.getPosition() + 1));

        BoardColumn newColumn = createColumn(columnName, position);
        newColumn.setBoard(board);

        board.getColumns().add(newColumn);
        boardRepository.save(board);
        return newColumn;
    }

    private BoardColumn createColumn(String name, Integer position) {
        BoardColumn column = new BoardColumn();
        column.setName(name);
        column.setPosition(position);
        return columnRepository.save(column);
    }



}
