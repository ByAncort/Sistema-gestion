import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkspacesBoardComponent } from './workspaces-board.component';

describe('WorkspacesBoardComponent', () => {
  let component: WorkspacesBoardComponent;
  let fixture: ComponentFixture<WorkspacesBoardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkspacesBoardComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WorkspacesBoardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
