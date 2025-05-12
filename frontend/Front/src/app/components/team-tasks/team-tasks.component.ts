import { Component } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-team-tasks',
  imports: [],
  templateUrl: './team-tasks.component.html',
  styleUrl: './team-tasks.component.css'
})
export class TeamTasksComponent {
  teamId!: number;
  tasks: any[] = [];
  loading = true;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient
  ) {}



}
