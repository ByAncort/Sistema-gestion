import { Routes } from '@angular/router';
import {authGuard} from './auth.guard';
import {LoginComponent} from './components/login/login.component';
import {HomeComponent} from './components/home/home.component';
import {noAuthGuard} from './no-auth.guard';
import {PrivateLayoutComponent} from './layouts/private-layout/private-layout.component';

export const routes: Routes = [
  {
    path: '',
    component: PrivateLayoutComponent,
    canActivate: [authGuard],
    children:[
      {
        path:'',
        component: HomeComponent,
      }
    ]
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [noAuthGuard]
  },
  {
    path: '**',
    redirectTo: '/login'
  }
];
