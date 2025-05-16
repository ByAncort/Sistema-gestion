// team-create.component.ts
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-team-create',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './team-create.component.html',
  styleUrls: ['./team-create.component.css']
})
export class TeamCreateComponent {
  @Input() users: any[] = []; // Deberías recibir esta lista del padre
  @Input() workspaces: any[] = []; // Deberías recibir esta lista del padre
  @Output() teamCreated = new EventEmitter<any>();
  @Output() closed = new EventEmitter<void>();

  isOpen = false;
  teamData = {
    nombreTeam: '',
    responsable: null,
    usersIds: [],
    workspacesIDs: []
  };

  open() {
    this.isOpen = true;
  }

  close() {
    this.isOpen = false;
    this.teamData = {
      nombreTeam: '',
      responsable: null,
      usersIds: [],
      workspacesIDs: []
    };
    this.closed.emit();
  }

  onSubmit() {
    this.teamCreated.emit({
      nombreTeam: this.teamData.nombreTeam,
      responsable: Number(this.teamData.responsable),
      usersIds: this.teamData.usersIds.map(Number),
      workspacesIDs: this.teamData.workspacesIDs.map(Number)
    });
    this.close();
  }
}
