package com.app.authjwt.Service;

import com.app.authjwt.Model.BoardColumn;
import com.app.authjwt.Model.Task;
import com.app.authjwt.Repository.*;
import com.app.authjwt.Service.CustomException.WipLimitExceededException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardColumnService {
    private final BoardColumnRepository columnRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public void moveTaskBetweenColumns(Long sourceColumnId, Long targetColumnId, Long taskId, Integer newPosition) {
        BoardColumn sourceColumn = columnRepository.findById(sourceColumnId)
                .orElseThrow(() -> new RuntimeException("Source column not found"));

        BoardColumn targetColumn = columnRepository.findById(targetColumnId)
                .orElseThrow(() -> new RuntimeException("Target column not found"));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Remove from source column
        sourceColumn.getTasks().removeIf(t -> t.getId().equals(taskId));

        // Add to target column
        targetColumn.getTasks().stream()
                .filter(t -> t.getPosition() >= newPosition)
                .forEach(t -> t.setPosition(t.getPosition() + 1));

        task.setPosition(newPosition);
        task.setColumn(targetColumn);
        targetColumn.getTasks().add(task);

        checkWipLimit(targetColumn);

        columnRepository.saveAll(List.of(sourceColumn, targetColumn));
    }


    @Transactional
    public void updateColumnWipLimit(Long columnId, Integer newWipLimit) {
        BoardColumn column = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));

        column.setWipLimit(newWipLimit);
        checkWipLimit(column);
        columnRepository.save(column);
    }

    private void checkWipLimit(BoardColumn column) {
        if (column.getWipLimit() != null && column.getWipLimit() > 0) {
            int currentTasks = column.getTasks().size();
            if (currentTasks > column.getWipLimit()) {
                throw new WipLimitExceededException(
                        "WIP Limit exceeded for column: " + column.getName() +
                                " (" + currentTasks + "/" + column.getWipLimit() + ")"
                );
            }
        }
    }

    @Transactional
    public void deleteColumnAndRedistributeTasks(Long columnId, Long targetColumnId) {
        BoardColumn columnToDelete = columnRepository.findById(columnId)
                .orElseThrow(() -> new RuntimeException("Column not found"));

        BoardColumn targetColumn = columnRepository.findById(targetColumnId)
                .orElseThrow(() -> new RuntimeException("Target column not found"));

        // Move all tasks to target column
        columnToDelete.getTasks().forEach(task -> {
            task.setColumn(targetColumn);
            task.setPosition(targetColumn.getTasks().size() + task.getPosition());
        });

        targetColumn.getTasks().addAll(columnToDelete.getTasks());
        columnRepository.delete(columnToDelete);
        columnRepository.save(targetColumn);
    }
}