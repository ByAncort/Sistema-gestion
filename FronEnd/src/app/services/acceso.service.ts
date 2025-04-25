import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {appsetting} from '../settings/appsetting';
import {SignIn} from '../models/request/SignIn';
import {Observable} from 'rxjs';
import {TokenResponse} from '../models/response/TokenResponse';
import {SignUp} from '../models/request/SignUp';

@Injectable({
  providedIn: 'root'
})
export class AccesoService {

  private http = inject(HttpClient)
  private baseUrl:string= appsetting.apiUrl;

  constructor() { }

  registrarse(objeto:SignUp): Observable<TokenResponse>{
    return this.http.post<TokenResponse>(`${this.baseUrl}/auth/signup`,objeto)
  }
  login(objeto:SignIn): Observable<TokenResponse>{
    return this.http.post<TokenResponse>(`${this.baseUrl}/auth/signin`,objeto)
  }
}
