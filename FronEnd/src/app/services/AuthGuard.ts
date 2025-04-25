import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {appsetting} from '../settings/appsetting';
import {catchError, map, Observable, of} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private http: HttpClient) {}

  canActivate(): Observable<boolean> {
    const token = localStorage.getItem('token');

    if (!token) {
      this.router.navigate(['/login']);
      return of(false);
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get(`${appsetting.apiUrl}/api/v1/users/api/test-connection`, { headers }).pipe(
      map(() => true),
      catchError(() => {
        localStorage.removeItem('token');
        this.router.navigate(['/login']);
        return of(false);
      })
    );
  }
}
