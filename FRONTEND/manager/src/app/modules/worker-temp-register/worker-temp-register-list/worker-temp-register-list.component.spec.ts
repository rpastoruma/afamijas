import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerTempRegisterListComponent } from './worker-temp-register-list.component';

describe('WorkerTempRegisterListComponent', () => {
  let component: WorkerTempRegisterListComponent;
  let fixture: ComponentFixture<WorkerTempRegisterListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkerTempRegisterListComponent]
    });
    fixture = TestBed.createComponent(WorkerTempRegisterListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
