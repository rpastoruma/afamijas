import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerHealthLogListComponent } from './worker-health-log-list.component';

describe('WorkerHealthLogListComponent', () => {
  let component: WorkerHealthLogListComponent;
  let fixture: ComponentFixture<WorkerHealthLogListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkerHealthLogListComponent]
    });
    fixture = TestBed.createComponent(WorkerHealthLogListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
