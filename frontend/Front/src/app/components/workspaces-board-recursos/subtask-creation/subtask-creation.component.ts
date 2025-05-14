// subtask-creation.component.ts
import { Component, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-subtask-creation',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './subtask-creation.component.html',
  styleUrls: ['./subtask-creation.component.css']
})
export class SubtaskCreationComponent {
  @Output() create = new EventEmitter<any>();
  @Output() close = new EventEmitter<void>();

  newSubtask: any = {
    title: '',
    priority: 'Medium',
    dueDate: '',
    horas: 0,
    puntos: 0
  };

  onSubmit(): void {
    if (this.isFormValid()) {
      this.create.emit(this.newSubtask);
      this.resetForm();
      this.closeModal();
    }
  }

  closeModal(): void {
    this.close.emit();
  }

  private resetForm(): void {
    this.newSubtask = {
      title: '',
      priority: 'Medium',
      dueDate: '',
      horas: 0,
      puntos: 0
    };
  }

  private isFormValid(): boolean {
    return !!this.newSubtask.title &&
      this.newSubtask.horas > 0 &&
      this.newSubtask.puntos > 0;
  }
}
