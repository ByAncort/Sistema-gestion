import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Config } from '../../config';
import { TaskDetailComponent } from '../workspaces-board-recursos/task-detail/task-detail.component';
import { SubtaskCreationComponent } from '../workspaces-board-recursos/subtask-creation/subtask-creation.component';
import { TaskCreationComponent } from '../workspaces-board-recursos/task-creation/task-creation.component';
import { CdkDragDrop, DragDropModule, transferArrayItem } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-workspaces-board',
  standalone: true,
  imports: [
    CommonModule,
    DragDropModule,
    FormsModule,
    TaskDetailComponent,
    SubtaskCreationComponent,
    TaskCreationComponent
  ],
  templateUrl: './workspaces-board.component.html',
  styleUrls: ['./workspaces-board.component.css']
})
export class WorkspacesBoardComponent implements OnInit {
  board: any;
  groupedTasks: { [columnName: string]: any[] } = {};
  selectedTask: any = null;
  loading = true;
  error: string | null = null;
  viewMode: 'board' | 'table' = 'board';
  showAddColumnModal = false;
  addingColumn = false;
  newColumn = {
    name: '',
    position: 1
  };

  private readonly apiUrl = Config.API_URL;
  private readonly http = inject(HttpClient);
  private readonly route = inject(ActivatedRoute);

  showDetailModal = false;
  showSubtaskModal = false;
  workspaceId = '';
  boardId = 0;
  connectedDropListsIds: string[] = [];

  newSubtask = {
    title: '',
    priority: 'Medium',
    dueDate: '',
    horas: 0,
    puntos: 0
  };

  ngOnInit(): void {
    this.initializeBoardData();
  }
  getTasksCountForColumn(columnName: string): number {
    return this.groupedTasks[columnName]?.length || 0;
  }
  onTaskDrop(event: CdkDragDrop<any[]>, targetColumn: any) {
    if (event.previousContainer === event.container) return;

    const task = event.previousContainer.data[event.previousIndex];

    transferArrayItem(
      event.previousContainer.data,
      event.container.data,
      event.previousIndex,
      event.currentIndex
    );

    // Mostrar IDs en consola
    console.log('Task ID:', task.id, 'New Column ID:', targetColumn.id);

    this.http.put(`${this.apiUrl}/api/boards/update/${task.id}/column/${targetColumn.id}`, {})
      .subscribe({
        next: (res) => console.log('Task updated successfully', res),
        error: (err) => console.error('Error updating task', err)
      });

  }

  private initializeBoardData(): void {
    this.route.params.subscribe(params => {
      this.workspaceId = params['workspaceId'];
      this.fetchBoardData();
    });
  }

  private fetchBoardData(): void {
    this.http.get<any>(`${this.apiUrl}/api/boards/workspace/${this.workspaceId}`).subscribe({
      next: (response) => this.handleBoardResponse(response),
      error: () => this.handleError('Error connecting to the server')
    });
  }

  private handleBoardResponse(response: any): void {
    if (response.success) {
      this.board = response.data;
      this.boardId = Number(response.data.id);

      // Configurar listas conectadas después de cargar el board
      this.connectedDropListsIds = this.board.column.map((col: any) => `cdk-drop-list-${col.id}`);

      this.fetchBoardTask(this.boardId);
      this.loading = false;
    } else {
      this.handleError('Error fetching board data');
    }
  }

  private fetchBoardTask(id: number): void {
    this.http.get<any[]>(`${this.apiUrl}/api/boards/boards/${id}/full-tasks`).subscribe({
      next: (tasks) => {
        this.groupTasks(tasks);
      },
      error: () => this.handleError('Error fetching tasks')
    });
  }

  // Resto de métodos sin cambios...
  handleTaskCreated() {
    this.fetchBoardData();
  }

  openTaskDetail(task: any): void {
    this.selectedTask = task;
    this.showDetailModal = true;
  }

  saveTaskChanges(updatedTask: any): void {
    const body = {
      id: updatedTask.id,
      title: updatedTask.title,
      description: updatedTask.description,
      dueDate: updatedTask.dueDate,
      columnId: updatedTask.columnId,
      position: updatedTask.position,
    };

    this.http.put<any>(`${this.apiUrl}/api/boards/actualizar`, body).subscribe({
      next: (response) => {
        console.log('Tarea actualizada:', response);
        this.closeTaskDetail();
      },
      error: (error) => {
        console.error('Error al actualizar la tarea:', error);
      }
    });
  }

  deleteSubtask(index: number): void {
    if (this.selectedTask?.subtasks) {
      this.selectedTask.subtasks.splice(index, 1);
    }
  }

  handleSubtaskCreate(newSubtask: any): void {
    if (this.selectedTask) {
      this.selectedTask.subtasks = this.selectedTask.subtasks || [];
      this.selectedTask.subtasks.push(newSubtask);
    }
  }

  closeTaskDetail(): void {
    this.showDetailModal = false;
    this.selectedTask = null;
  }

  openSubtaskModalHandler(): void {
    this.showSubtaskModal = true;
  }

  closeSubtaskModal(): void {
    this.showSubtaskModal = false;
    this.resetSubtaskForm();
  }

  private groupTasks(tasks: any[]): void {
    this.groupedTasks = this.board.column.reduce((acc: any, col: any) => {
      acc[col.name] = [];
      return acc;
    }, {});

    tasks.forEach(task => {
      if (this.groupedTasks[task.columnName]) {
        this.groupedTasks[task.columnName].push(task);
      }
    });
  }

  private handleError(message: string): void {
    this.error = message;
    this.loading = false;
  }

  private resetSubtaskForm(): void {
    this.newSubtask = {
      title: '',
      priority: 'Medium',
      dueDate: '',
      horas: 0,
      puntos: 0
    };
  }

  openAddColumnModal() {
    this.showAddColumnModal = true;
    this.newColumn = {
      name: '',
      position: this.board.column.length + 1
    };
  }
  closeAddColumnModal() {
    this.showAddColumnModal = false;
  }
  submitNewColumn() {
    if (!this.newColumn.name.trim()) {
      return; // Validación básica
    }

    this.addingColumn = true;

    this.http.post<any>(
      `${this.apiUrl}/api/boards/workspace/agregar/columna/${this.boardId}`,
      {
        name: this.newColumn.name,
        position: this.newColumn.position
      },

    ).subscribe({
      next: (response) => {
        this.addingColumn = false;
        this.closeAddColumnModal();
        this.fetchBoardData(); // Recargar los datos del board
      },
      error: (error) => {
        this.addingColumn = false;
        this.error = 'Error adding column: ' + error.message;
        setTimeout(() => this.error = null, 5000);
      }
    });
  }
}
