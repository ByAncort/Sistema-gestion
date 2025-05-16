import {Component, inject} from '@angular/core';
import {AuthService} from '../../auth.service';
import {CommonModule} from '@angular/common';
import {Config} from '../../config';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent {
  authService = inject(AuthService);
  // Configuración y servicios
  private readonly apiUrl = Config.API_URL;
  private readonly http = inject(HttpClient);
  private readonly route = inject(ActivatedRoute);

  // Estado de los m
  user?:any;
  constructor() {
    this.authService.getCurrentAuthUser().subscribe((r) => {
      console.log(r)
      this.user = r;
    })
  }
  getUser(): void {
    this.http.get<any[]>(this.apiUrl + "/api/v1/users/listar-users").subscribe({
      next: (users) => {
        if (users.length > 0) {
          const user = users[0]; // Suponiendo que querés guardar el primero
          localStorage.setItem('user', JSON.stringify(user));
          console.log('Usuario guardado en localStorage:', user);
        } else {
          console.warn('No se encontraron usuarios en la respuesta.');
        }
      },
      error: (error) => {
        console.error('Error al obtener usuarios:', error);
      }
    });
  }


  logout() {
    this.authService.logout();
  }
}
