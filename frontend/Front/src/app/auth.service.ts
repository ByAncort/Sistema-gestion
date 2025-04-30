import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';
import { Config } from './config';
import { tap, catchError, map } from 'rxjs/operators'; // Import corregido

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly JWT_TOKEN = "JWT_TOKEN";
  private loggedUser!: string;
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);
  private router = inject(Router);
  private http = inject(HttpClient);
  private apiUrl = Config.API_URL;
  private _redirectUrl: string | null = null;


  // Getter para redirectUrl
  get redirectUrl(): string | null {
    return this._redirectUrl;
  }

  // Setter para redirectUrl
  set redirectUrl(url: string | null) {
    this._redirectUrl = url;
  }

  constructor() {
    this.checkTokenInitially();
  }

  private checkTokenInitially() {
    const token = localStorage.getItem(this.JWT_TOKEN);
    if (token) {
      this.comprobarToken().subscribe({
        next: (isValid) => {
          this.isAuthenticatedSubject.next(isValid);
          if (!isValid) {
            this.logout();
          }
        }
      });
    }
  }

  login(user: { username: string, password: string }): Observable<any> {
    return this.http.post(`${this.apiUrl}/api/auth/login`, user).pipe(
      tap((response: any) => this.doLoginUser(user.username, response.token)),
      catchError(error => {
        console.error('Login error:', error);
        return of(null);
      })
    );
  }

  private doLoginUser(username: string, token: any) {
    this.loggedUser = username;
    this.storeJwtToken(token);
    this.isAuthenticatedSubject.next(true);
  }

  private storeJwtToken(jwt: string) {
    localStorage.setItem(this.JWT_TOKEN, jwt);
  }

  logout() {
    localStorage.removeItem(this.JWT_TOKEN);
    this.isAuthenticatedSubject.next(false);
    this.router.navigate(['/login']);
  }

  getCurrentAuthUser() {
    return this.http.get(`${this.apiUrl}/api/v1/users/info/profile`);
  }

  isLoggedIn(): Observable<boolean> {
    const token = localStorage.getItem(this.JWT_TOKEN);
    if (!token) {
      return of(false);
    }

    return of(true).pipe(
      catchError(() => {
        this.logout();
        return of(false);
      })
    );
  }

  comprobarToken(): Observable<boolean> {
    const token = localStorage.getItem(this.JWT_TOKEN);

    if (!token || this.isTokenExpired(token)) {
      return of(false);
    }

    return this.http.get(`${this.apiUrl}/api/v1/users/api/test-connection`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      map(() => true), // Ahora map está correctamente importado
      catchError(error => {
        if (error.status === 401) {
          this.logout();
        }
        return of(false);
      })
    );
  }

  private isTokenExpired(token: string): boolean {
    try {
      const decoded = jwtDecode(token);
      if (!decoded.exp) return true;

      const expirationDate = decoded.exp * 1000;
      const now = new Date().getTime();

      return expirationDate < now;
    } catch (error) {
      return true;
    }
  }

  get isAuthenticated$(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }
}
