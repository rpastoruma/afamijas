import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerFeedingRegisterListComponent } from './worker-feeding-register-list.component';

describe('WorkerFeedingRegisterListComponent', () => {
  let component: WorkerFeedingRegisterListComponent;
  let fixture: ComponentFixture<WorkerFeedingRegisterListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkerFeedingRegisterListComponent]
    });
    fixture = TestBed.createComponent(WorkerFeedingRegisterListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
