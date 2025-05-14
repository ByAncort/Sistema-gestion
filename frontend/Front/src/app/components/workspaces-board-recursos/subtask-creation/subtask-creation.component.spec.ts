import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubtaskCreationComponent } from './subtask-creation.component';

describe('SubtaskCreationComponent', () => {
  let component: SubtaskCreationComponent;
  let fixture: ComponentFixture<SubtaskCreationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubtaskCreationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubtaskCreationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
