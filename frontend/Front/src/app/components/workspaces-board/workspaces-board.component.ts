import { Component, inject, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CdkDragDrop, CdkDrag, CdkDropList, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { Config } from '../../config';
import { ActivatedRoute, Router } from '@angular/router';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-workspaces-board',
  standalone: true,
  imports: [CommonModule, CdkDropList, CdkDrag,FormsModule],
  templateUrl: './workspaces-board.component.html',
  styleUrls: ['./workspaces-board.component.css']
})
export class WorkspacesBoardComponent implements OnInit {
  board: any;
  boardId: number = 0;
  loading: boolean = true;
  error: string | null = null;
  isKanbanView: boolean = true;
  groupedTasks: { [columnName: string]: any[] } = {};
  private apiUrl = Config.API_URL;
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);
  workspaceId: string = '';
  openSubtaskModal: boolean = false;

  // Variables para la tarea seleccionada y su modal
  selectedTask: any = null;
  showDetailModal: boolean = false;

  // Variables para la creación de nuevas subtareas
  newSubtask: any = {
    title: '',
    priority: 'Medium',
    dueDate: '',
    horas: 0,
    puntos: 0
  };

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.workspaceId = params['workspaceId'];
      this.fetchBoardData();
    });
  }

  fetchBoardData(): void {
    this.http.get<any>(`${this.apiUrl}/api/boards/workspace/${this.workspaceId}`).subscribe({
      next: (response) => {
        if (response.success) {
          this.board = response.data;
          this.boardId = Number(response.data.id);
          this.fetchBoardTask(this.boardId);
          this.loading = false;
        } else {
          this.error = 'Error fetching board data.';
          this.loading = false;
        }
      },
      error: () => {
        this.error = 'Error connecting to the server.';
        this.loading = false;
      }
    });
  }

  fetchBoardTask(id: number): void {
    this.http.get<any[]>(`${this.apiUrl}/api/boards/boards/${id}/full-tasks`).subscribe({
      next: (tasks) => {
        this.groupedTasks = {};
        for (const task of tasks) {
          if (!this.groupedTasks[task.columnName]) {
            this.groupedTasks[task.columnName] = [];
          }
          this.groupedTasks[task.columnName].push(task);
        }
      },
      error: () => {
        this.error = 'Error fetching tasks.';
      }
    });
  }

  openTaskDetail(task: any): void {
    this.selectedTask = task;
    this.showDetailModal = true;
  }

  closeTaskDetail(): void {
    this.showDetailModal = false;
    this.selectedTask = null;
  }

  openSubtaskModalHandler(): void {
    this.openSubtaskModal = true;
  }

  closeSubtaskModal(): void {
    this.openSubtaskModal  = false;
    this.newSubtask = {
      title: '',
      priority: 'Medium',
      dueDate: '',
      horas: 0,
      puntos: 0
    };
  }
  saveTaskChanges() {
    // Implement your save logic here
    // This should update the task in your backend/service
    console.log('Saving task:', this.selectedTask);
    this.closeTaskDetail();
  }
  deleteSubtask(index: number) {
    if (this.selectedTask?.subtasks) {
      this.selectedTask.subtasks.splice(index, 1);
    }
  }
  createSubtask(): void {
    if (this.newSubtask.title && this.newSubtask.horas && this.newSubtask.puntos) {
      // Suponiendo que la API para crear la subtarea es algo como esto:
      console.log(this.newSubtask)
    }
  }
}
