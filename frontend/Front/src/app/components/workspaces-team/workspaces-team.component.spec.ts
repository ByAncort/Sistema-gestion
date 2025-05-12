import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkspacesTeamComponent } from './workspaces-team.component';

describe('WorkspacesTeamComponent', () => {
  let component: WorkspacesTeamComponent;
  let fixture: ComponentFixture<WorkspacesTeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkspacesTeamComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkspacesTeamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
