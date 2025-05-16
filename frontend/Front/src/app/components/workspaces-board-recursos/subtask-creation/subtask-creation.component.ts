// Angular Component Class
import {Component, Output, EventEmitter, Input, inject} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {CommonModule} from '@angular/common';
import {Config} from '../../../config';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-subtask-creation',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './subtask-creation.component.html',
  styleUrls: ['./subtask-creation.component.css']
})
export class SubtaskCreationComponent {
  @Output() create = new EventEmitter<any>();
  @Output() close = new EventEmitter<void>();
  @Input() taskId!: number | undefined;

  users: any[] = [];

  newSubtask: any = {
    title: '',
    assigneeId: null,
    priority: 'Medium',
    dueDate: '',
    horas: 0,
    puntos: 0
  };

  constructor() {
    const storedUser = localStorage.getItem('user');
    if (storedUser) {
      const user = JSON.parse(storedUser);
      this.users.push(user);
      console.log();
    }
  }

  onSubmit(): void {
    if (this.isFormValid()) {
      this.create.emit({
        ...this.newSubtask,
        status: 'Pending'
      });
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
      this.newSubtask.assigneeId &&
      this.newSubtask.horas > 0 &&
      this.newSubtask.puntos > 0;
  }
}
