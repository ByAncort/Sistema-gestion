// team.component.ts
import { Component, OnInit, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Config } from '../../config';
import { CommonModule } from '@angular/common';
import {Router} from '@angular/router';

interface TeamResponse {
  nombreTeam: string;
  responsable: {
    usermane: string;
    email: string;
    roles: {
      name: string;
      permissions: any[];
    }[];
    enabled: boolean;
  };
  users: {
    usermane: string;
    email: string;
    roles: {
      name: string;
      permissions: any[];
    }[];
    enabled: boolean;
  }[];
  workspaces: {
    id: number;
    name: string;
  }[];
}

interface ApiResponse {
  data: TeamResponse[];
  errors: string[];
  success: boolean;
}

@Component({
  selector: 'app-team',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './team.component.html',
  styleUrls: ['./team.component.css']
})
export class TeamComponent implements OnInit {
  private router = inject(Router);
  private apiUrl = Config.API_URL;
  private http = inject(HttpClient);
  teams: TeamResponse[] = [];
  loading: boolean = true;
  error: string | null = null;

  navigateToTeamTasks(teamName: String) {
    this.router.navigate(['/team', 'workspaces',teamName]);
  }

  ngOnInit() {
    this.loadTeams();
  }

  loadTeams() {
    this.loading = true;
    this.error = null;


    this.http.get<ApiResponse>(`${this.apiUrl}/api/teams`)
      .subscribe({
        next: (response) => {
          if (response.success) {
            this.teams = response.data;
          } else {
            this.error = response.errors.join(', ') || 'Error al cargar los equipos';
          }
          this.loading = false;
        },
        error: (err) => {
          this.error = 'Error de conexión al cargar los equipos';
          this.loading = false;
          console.error('Error loading teams:', err);
        }
      });
  }
}
