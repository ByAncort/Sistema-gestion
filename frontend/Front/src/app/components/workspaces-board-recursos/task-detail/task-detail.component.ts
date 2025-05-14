// task-detail.component.ts
import { Component, Input, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import {CommonModule} from '@angular/common';

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

  onSave(): void {
    this.save.emit(this.selectedTask);
  }

  onClose(): void {
    this.close.emit();
  }

  onDeleteSubtask(index: number): void {
    this.deleteSubtask.emit(index);
  }

  onOpenSubtaskModal(): void {
    this.openSubtaskModal.emit();
  }
}
