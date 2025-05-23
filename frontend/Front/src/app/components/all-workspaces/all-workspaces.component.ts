
import {Component, inject} from '@angular/core';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {Config} from '../../config';
import {CommonModule} from '@angular/common';
import {CreateWorkspaceComponent} from '../workspaces-team/create-workspace/create-workspace.component';
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
  selector: 'app-all-workspaces',
  imports: [CommonModule,RouterModule,CreateWorkspaceComponent],
  templateUrl: './all-workspaces.component.html',
  styleUrl: './all-workspaces.component.css'
})
export class AllWorkspacesComponent {
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private http = inject(HttpClient);
  private apiUrl = Config.API_URL;

  teamId: number | null = null;
  workspaces: WorkspaceResponse[] = [];
  loading: boolean = true;
  error: string | null = null;
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

    this.http.get<ApiResponse>(`${this.apiUrl}/api/workspaces`)
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.workspaces = response.data ??[];
            console.log("Team Id es: "+this.teamId);
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
