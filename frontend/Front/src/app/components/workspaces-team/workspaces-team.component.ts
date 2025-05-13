// workspaces-team.component.ts
import { Component, inject, OnInit } from '@angular/core';
import {Router, ActivatedRoute, RouterModule} from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Config } from '../../config';
import {CommonModule} from '@angular/common';

interface WorkspaceResponse {
  id: number;
  name: string;
}

interface ApiResponse {
  data: WorkspaceResponse[];
  errors: string[];
  success: boolean;
}

@Component({
  selector: 'app-workspaces-team',
  templateUrl: './workspaces-team.component.html',
  imports: [CommonModule,RouterModule],
  styleUrls: ['./workspaces-team.component.css']
})
export class WorkspacesTeamComponent implements OnInit {
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);
  private apiUrl = Config.API_URL;

  workspaces: WorkspaceResponse[] = [];
  loading: boolean = true;
  error: string | null = null;
  teamName: string = '';
  navigateToWorkspaceTasks(workspaceId: number) {
    this.router.navigate(['/workspace', workspaceId, 'board']);
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.teamName = params['teamName'];
      this.loadWorkspaces();
    });
  }

  loadWorkspaces() {
    this.loading = true;
    this.error = null;

    this.http.get<ApiResponse>(`${this.apiUrl}/api/workspaces/find/team-name/${this.teamName}`)
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.workspaces = response.data;
          } else {
            this.error = response.errors.join(', ') || 'Error al cargar los workspaces';
          }
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error de conexión';
          this.loading = false;
          console.error('Error:', err);
        }
      });
  }
}
