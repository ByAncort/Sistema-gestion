import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CdkDragDrop, CdkDrag, CdkDropList } from '@angular/cdk/drag-drop';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Config } from '../../config';
import { TaskDetailComponent } from '../workspaces-board-recursos/task-detail/task-detail.component';
import { SubtaskCreationComponent } from '../workspaces-board-recursos/subtask-creation/subtask-creation.component';
import {TaskCreationComponent} from '../workspaces-board-recursos/task-creation/task-creation.component';

@Component({
  selector: 'app-workspaces-board',
  standalone: true,
  imports: [
    CommonModule,
    CdkDropList,
    CdkDrag,
    FormsModule,
    TaskDetailComponent,
    SubtaskCreationComponent,
    TaskCreationComponent
  ],
  templateUrl: './workspaces-board.component.html',
  styleUrls: ['./workspaces-board.component.css']
})
export class WorkspacesBoardComponent implements OnInit {
  // Propiedades del estado de la aplicación
  board: any;
  groupedTasks: { [columnName: string]: any[] } = {};
  selectedTask: any = null;
  loading = true;
  error: string | null = null;

  // Configuración y servicios
  private readonly apiUrl = Config.API_URL;
  private readonly http = inject(HttpClient);
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);

  // Estado de los modales
  showDetailModal = false;
  showSubtaskModal = false;

  // IDs
  workspaceId = '';
  boardId = 0;

  // Modelo de nueva subtarea
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

  // Métodos de inicialización
  private initializeBoardData(): void {
    this.route.params.subscribe(params => {
      this.workspaceId = params['workspaceId'];
      this.fetchBoardData();
    });
  }

  handleTaskCreated() {
  this.fetchBoardData();
  }

  // Métodos de obtención de datos
  private fetchBoardData(): void {
    this.http.get<any>(`${this.apiUrl}/api/boards/workspace/${this.workspaceId}`).subscribe({
      next: (response) => this.handleBoardResponse(response),
      error: () => this.handleError('Error connecting to the server')
    });
  }

  private fetchBoardTask(id: number): void {
    this.http.get<any[]>(`${this.apiUrl}/api/boards/boards/${id}/full-tasks`).subscribe({
      next: (tasks) => this.groupTasks(tasks),
      error: () => this.handleError('Error fetching tasks')
    });
  }

  // Manejo de tareas
  openTaskDetail(task: any): void {
    this.selectedTask = task;
    this.showDetailModal = true;
  }

  saveTaskChanges(updatedTask: any): void {
    console.log('Saving task:', updatedTask);
    // Aquí iría tu lógica para actualizar la tarea
    this.closeTaskDetail();
  }


  deleteSubtask(index: number): void {
    if (this.selectedTask?.subtasks) {
      this.selectedTask.subtasks.splice(index, 1);
    }
  }

  // Manejo de subtareas
  handleSubtaskCreate(newSubtask: any): void {
    if (this.selectedTask) {
      this.selectedTask.subtasks = this.selectedTask.subtasks || [];
      this.selectedTask.subtasks.push(newSubtask);
      console.log('Nueva subtarea creada:', newSubtask);
    }
  }

  // Manejo de modales
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

  // Métodos auxiliares
  private groupTasks(tasks: any[]): void {
    this.groupedTasks = tasks.reduce((acc, task) => {
      acc[task.columnName] = acc[task.columnName] || [];
      acc[task.columnName].push(task);
      return acc;
    }, {});
  }

  private handleBoardResponse(response: any): void {
    if (response.success) {
      this.board = response.data;
      this.boardId = Number(response.data.id);
      this.fetchBoardTask(this.boardId);
      this.loading = false;
    } else {
      this.handleError('Error fetching board data');
    }
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

}
