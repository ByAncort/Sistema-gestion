import { Routes } from '@angular/router';
import {LoginComponent} from './features/login/login.component';
import {RegisterComponent} from './features/register/register.component';
import {PorfileComponent} from './features/porfile/porfile.component';
import {AuthGuard} from './services/AuthGuard';
import {NoAuthGuard} from './services/NoAuthGuard';
import {DashboardComponent} from './features/dashboard/dashboard.component';

export const routes: Routes = [
  {path:"login",component:LoginComponent,canActivate:[NoAuthGuard]},
  {path:"register",component:RegisterComponent,canActivate:[NoAuthGuard]},
  {path:"profile",component:PorfileComponent, canActivate:[AuthGuard]},
  {path:"dashboard",component:DashboardComponent, canActivate:[AuthGuard]},
  {path:'**',redirectTo:'login'}
];
