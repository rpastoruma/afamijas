import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerMedicationListComponent } from './worker-medication-list.component';

describe('WorkerMedicationListComponent', () => {
  let component: WorkerMedicationListComponent;
  let fixture: ComponentFixture<WorkerMedicationListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkerMedicationListComponent]
    });
    fixture = TestBed.createComponent(WorkerMedicationListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
