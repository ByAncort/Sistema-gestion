import {Component, inject} from '@angular/core';
import {AccesoService} from '../../services/acceso.service';
import {Router} from '@angular/router';
import { FormGroup, FormControl, Validators, FormBuilder }  from '@angular/forms';
import {SignIn} from '../../models/request/SignIn';
import { ReactiveFormsModule } from '@angular/forms'; // Add this import


@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule], // Add ReactiveFormsModule here
  standalone: true, // Add this if you're using standalone components
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  private LoginService = inject(AccesoService);
  private router = inject(Router);
  private formBuild = inject(FormBuilder);

  public formLogin: FormGroup = this.formBuild.group({
    username: ['', Validators.required],
    password: ['', Validators.required]
  });

  iniciarSesion() {
    if(this.formLogin.invalid) return;

    const objeto: SignIn = {
      username: this.formLogin.value.username,
      password: this.formLogin.value.password
    };

    this.LoginService.login(objeto).subscribe({
      next: (data) => {
        if(data.token) {
          localStorage.setItem("token", data.token);
          this.router.navigate(['profile']);
        } else {
          alert("Credenciales incorrectas");
        }
      },
      error: (error) => {
        console.log(error.message);
      }
    });
  }

  registrarte() {
    this.router.navigate(['register']);
  }
}
