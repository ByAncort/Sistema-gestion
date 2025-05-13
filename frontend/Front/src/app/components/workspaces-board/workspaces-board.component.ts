import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { CdkDragDrop, CdkDrag, CdkDropList, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import {Config} from '../../config';

@Component({
  selector: 'app-workspaces-board',
  standalone: true,
  imports: [CommonModule, CdkDropList, CdkDrag],
  templateUrl: './workspaces-board.component.html',
  styleUrls: ['./workspaces-board.component.css']
})
export class WorkspacesBoardComponent implements OnInit {
  board: any;
  loading: boolean = true;
  error: string | null = null;
  isKanbanView: boolean = true;
  allTasks: any[] = [];
  private apiUrl = Config.API_URL;
  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchBoardData();
  }

  fetchBoardData(): void {
    this.http.get(`${this.apiUrl}/api/boards/workspace/1`)
      .subscribe({
        next: (response: any) => {
          if (response.success) {
            this.board = response.data;

            // Inicializar tasks si no existen
            this.board.column.forEach((col: any) => {
              col.tasks = col.tasks || [];
            });

            this.prepareTasks();
          }
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error de conexión con el servidor';
          this.loading = false;
        }
      });
  }

  prepareTasks(): void {
    this.allTasks = (this.board?.column || [])
      .flatMap((column: any) =>
        (column.tasks || []).map((task: any) => ({
          ...task,
          columnName: column.name,
          columnId: column.id
        }))
      );
  }


  drop(event: CdkDragDrop<any[]>, column: any): void {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex
      );
      // Actualizar el estado de la tarea en el backend
      const task = event.container.data[event.currentIndex];
      this.updateTaskStatus(task.id, column.id);
    }
  }

  updateTaskStatus(taskId: number, newColumnId: number): void {
    // Implementar llamada al servicio para actualizar el estado
    this.http.patch(`/api/tasks/${taskId}`, { columnId: newColumnId })
      .subscribe();
  }

  toggleView(): void {
    this.isKanbanView = !this.isKanbanView;
  }
}
