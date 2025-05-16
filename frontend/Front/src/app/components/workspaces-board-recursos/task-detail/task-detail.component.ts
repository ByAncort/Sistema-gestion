// task-detail.component.ts
import {Component, Input, Output, EventEmitter, inject} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {CommonModule} from '@angular/common';
import {Config} from '../../../config';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-task-detail',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './task-detail.component.html',
  styleUrls: ['./task-detail.component.css']
})
export class TaskDetailComponent {
  @Input() selectedTask: any;
  @Output() close = new EventEmitter<void>();
  @Output() save = new EventEmitter<any>();
  @Output() deleteSubtask = new EventEmitter<number>();
  @Output() openSubtaskModal = new EventEmitter<void>();
  private readonly apiUrl = Config.API_URL;
  private readonly http = inject(HttpClient);

  onSave(): void {
    this.save.emit(this.selectedTask);
  }

  onClose(): void {
    this.close.emit();
  }

  onDeleteSubtask(index: number,idSubtask: number): void {
    this.deleteSubtask.emit(index);
    console.log("Eliminando Subtask:" +idSubtask);
    this.http.delete(`${this.apiUrl}/api/boards/delete-subtask/${idSubtask}`)
      .subscribe({
        next: () => {
          console.log(`Subtarea con ID ${idSubtask} eliminada correctamente`);
        },
        error: (error) => {
          console.error(`Error al eliminar subtarea con ID ${idSubtask}:`, error);
        }
      });  }

  onOpenSubtaskModal(): void {
    this.openSubtaskModal.emit();
  }
}
