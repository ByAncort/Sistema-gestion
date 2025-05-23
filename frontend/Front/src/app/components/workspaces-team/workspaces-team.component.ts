// workspaces-team.component.ts
import { Component, inject, OnInit,ViewChild  } from '@angular/core';
import {Router, ActivatedRoute, RouterModule} from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Config } from '../../config';
import {CommonModule} from '@angular/common';
import {TeamCreateComponent} from '../team/team-create/team-create.component';
import {CreateWorkspaceComponent} from './create-workspace/create-workspace.component';

interface WorkspaceResponse {
  id: number;
  name: string;
  teamsId: number[];
}

interface ApiResponse {
  data: WorkspaceResponse[];
  errors: string[];
  success: boolean;
}

@Component({
  selector: 'app-workspaces-team',
  templateUrl: './workspaces-team.component.html',
  imports: [CommonModule,RouterModule,CreateWorkspaceComponent],
  styleUrls: ['./workspaces-team.component.css']
})
export class WorkspacesTeamComponent implements OnInit {
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);
  private apiUrl = Config.API_URL;

  teamId: number | null = null;
  workspaces: WorkspaceResponse[] = [];
  loading: boolean = true;
  error: string | null = null;
  teamName: string = '';
  navigateToWorkspaceTasks(workspaceId: number) {
    this.router.navigate(['/workspace', workspaceId, 'board']);
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      this.teamId = params['teamId'];
      this.loadWorkspaces();
    });
  }

  loadWorkspaces() {
    this.loading = true;
    this.error = null;

    this.http.get<ApiResponse>(`${this.apiUrl}/api/workspaces/find/team-name/${this.teamId}`)
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.workspaces = response.data ??[];
            console.log("Team Id es: "+this.teamId);
            if (response.data!=null) {
              this.teamName = this.workspaces[0].name;
            } else {
              this.teamName = 'Equipo sin nombre';
            }          } else {
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
