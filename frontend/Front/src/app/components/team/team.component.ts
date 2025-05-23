// team.component.ts
import { Component, OnInit, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Config } from '../../config';
import { CommonModule } from '@angular/common';
import {Router} from '@angular/router';
import {TeamCreateComponent} from './team-create/team-create.component';

interface TeamResponse {
  id: number,
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
  imports: [CommonModule,TeamCreateComponent],
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
  usersList: any[] = [];

  navigateToTeamTasks(teamName: number) {
    this.router.navigate(['/team', 'workspaces',teamName]);
  }

  ngOnInit() {
    this.loadTeams();
    this.loadUser();
    const storedUsers = localStorage.getItem("users");
    if (storedUsers) {
      this.usersList = JSON.parse(storedUsers);
      console.log(this.usersList);
    }

  }
  loadUser() {
    this.http.get<any[]>(`${this.apiUrl}/api/v1/users/listar-users`).subscribe({
      next: (users) => {
        localStorage.setItem("users", JSON.stringify(users));
        this.usersList = users;
      },
      error: (err) => {
        console.error("Error al cargar usuarios:", err);
      }
    });
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

  //modal de create
  handleTeamCreated(teamData: any) {
    console.log(teamData);
    this.http.post(`${this.apiUrl}/api/teams`, teamData).subscribe({
      next: (response) => {
        console.log("Equipo actualizado/creado correctamente:", response);
        // Aquí podrías recargar los equipos si deseas
        this.loadTeams();
      },
      error: (error) => {
        console.error("Error al crear/actualizar el equipo:", error);
      }
    });
  }

  workspacesList = [
    {
      id: 1,
      name: 'Proyecto Alpha',
      description: 'Desarrollo de aplicación principal'
    },
    {
      id: 2,
      name: 'Marketing Digital',
      description: 'Campañas publicitarias'
    },
    {
      id: 3,
      name: 'Soporte Técnico',
      description: 'Equipo de asistencia'
    },
    {
      id: 4,
      name: 'Investigación y Desarrollo',
      description: 'Innovación tecnológica'
    }
  ];


}
