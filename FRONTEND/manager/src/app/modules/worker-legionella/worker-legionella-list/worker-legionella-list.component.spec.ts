import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerLegionellaListComponent } from './worker-legionella-list.component';

describe('WorkerLegionellaListComponent', () => {
  let component: WorkerLegionellaListComponent;
  let fixture: ComponentFixture<WorkerLegionellaListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkerLegionellaListComponent]
    });
    fixture = TestBed.createComponent(WorkerLegionellaListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
