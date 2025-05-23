import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-create-workspace',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './create-workspace.component.html',
  styleUrls: ['./create-workspace.component.css']
})
export class CreateWorkspaceComponent {
  @Input() teamId: number | null = null;
  isModalOpen = false;

  workspace = {
    name: '',
    teamsId: [] as number[]
  };
  isLoading = false;
  errorMessage = '';
  successMessage = '';
  workspaces: any[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit() {
    if (this.teamId) {
      this.workspace.teamsId = [this.teamId];
    }
  }

  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
    this.resetForm();
    this.errorMessage = '';
    this.successMessage = '';
  }

  onSubmit() {
    this.isLoading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });

    this.http.post('http://localhost:9011/api/workspaces', this.workspace, { headers })
      .subscribe({
        next: (response) => {
          this.isLoading = false;
          this.successMessage = 'Workspace creado exitosamente!';

          this.createBoard(this.workspace.teamsId[0]);

          setTimeout(() => this.closeModal(), 15000);
          this.resetForm();

          location.reload();
        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = 'Error al crear el workspace. Por favor intenta nuevamente.';
          console.error('Error:', error);
        }
      });
  }

  createBoard(id:number){
    const boardName=this.workspace.name;

    const headers = new HttpHeaders({
      'Content-Type': 'application/json'
    });
    const jsonData: { [clave: string]: any } = {
      name: boardName+" - Task",
    };
    this.http.post('http://localhost:9011/api/boards/workspace/'+id, jsonData, { headers })
      .subscribe({
        next: (response) => {

        },
        error: (error) => {
          this.isLoading = false;
          this.errorMessage = 'Error al crear el board. Por favor intenta nuevamente.';
          console.error('Error:', error);
        }
      });
  }
  resetForm() {
    this.workspace = {
      name: '',
      teamsId: this.teamId ? [this.teamId] : []
    };
  }
}
