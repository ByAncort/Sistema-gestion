import {Component, inject} from '@angular/core';
import { FormsModule } from '@angular/forms';
import {AuthService} from '../../auth.service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';
  authService = inject(AuthService);
  route = inject(Router);

  onSubmit() {
    console.log(`login: ${this.username} / ${this.password}`);
    this.authService.login({
      username: this.username,
      password: this.password,
    }).subscribe({
      next: (response) => {
        alert('Login success!');
        this.route.navigate(['/']);
      },
      error: (err) => {
        this.errorMessage = err.message;
        console.error('Error in component:', err.message);
        alert(`Login failed: ${err.message}`);  // Optional: show alert for error
      }
    });
  }

}
