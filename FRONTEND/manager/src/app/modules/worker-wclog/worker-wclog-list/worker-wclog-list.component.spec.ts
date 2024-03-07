import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerWclogListComponent } from './worker-wclog-list.component';

describe('WorkerWclogListComponent', () => {
  let component: WorkerWclogListComponent;
  let fixture: ComponentFixture<WorkerWclogListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkerWclogListComponent]
    });
    fixture = TestBed.createComponent(WorkerWclogListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
