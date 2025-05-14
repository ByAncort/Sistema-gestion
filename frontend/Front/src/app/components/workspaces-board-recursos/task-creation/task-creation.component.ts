import { Component, EventEmitter, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {Config } from '../../../config';
import {FormsModule} from '@angular/forms';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-task-creation',
  templateUrl: './task-creation.component.html',
  styleUrls: ['./task-creation.component.css'],
  imports: [
    FormsModule,
    CommonModule
  ]

})
export class TaskCreationComponent {
  @Output() taskCreated = new EventEmitter<void>();
  showModal = false;
  newTask = {
    title: '',
    description: '',
    dueDate: '',
    columnId: 1,
    position: 1
  };
  errorMessage = '';
  loading = false;

  private apiUrl = Config.API_URL;

  constructor(private http: HttpClient) {}

  openModal() {
    console.log('Abrir modal');
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.resetForm();
  }

  createTask() {
    this.loading = true;
    this.errorMessage = '';

    this.http.post(`${this.apiUrl}/api/boards/save`, this.newTask)
      .subscribe({
        next: () => {
          this.taskCreated.emit();
          this.closeModal();
        },
        error: (error) => {
          this.errorMessage = 'Error creating task';
          console.error('Error:', error);
        }
      })
      .add(() => this.loading = false);
  }

  private resetForm() {
    this.newTask = {
      title: '',
      description: '',
      dueDate: '',
      columnId: 1,
      position: 1
    };
  }
}
