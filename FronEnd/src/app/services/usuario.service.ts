import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {appsetting} from '../settings/appsetting';
import {SignUp} from '../models/request/SignUp';
import {Observable} from 'rxjs';
import {TokenResponse} from '../models/response/TokenResponse';
import {UsuarioResponse} from '../models/response/UsuarioResponse';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private http = inject(HttpClient)
  private baseUrl:string= appsetting.apiUrl;

  constructor() { }
  getInfoUser(): Observable<UsuarioResponse>{
    return this.http.post<UsuarioResponse>(`${this.baseUrl}/api/v1/info/profile`)
  }

}
