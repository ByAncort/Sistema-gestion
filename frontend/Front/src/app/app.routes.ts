import { Routes } from '@angular/router';
import {authGuard} from './auth.guard';
import {LoginComponent} from './components/login/login.component';
import {HomeComponent} from './components/home/home.component';
import {noAuthGuard} from './no-auth.guard';
import {PrivateLayoutComponent} from './layouts/private-layout/private-layout.component';
import {TeamComponent} from './components/team/team.component';
import {TeamTasksComponent} from './components/team-tasks/team-tasks.component';
import {WorkspacesTeamComponent} from './components/workspaces-team/workspaces-team.component';
import {WorkspacesBoardComponent} from './components/workspaces-board/workspaces-board.component';

export const routes: Routes = [
  {
    path: '',
    component: PrivateLayoutComponent,
    canActivate: [authGuard],
    children:[
      {
        path:'',
        component: HomeComponent,
      },
      {
        path:'team',
        component: TeamComponent,
      },
      {
        path:'team/workspaces/:teamName',
        component: WorkspacesTeamComponent
      },
      {
        path:'workspace/:workspaceId/board',
        component: WorkspacesBoardComponent
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
