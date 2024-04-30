import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerDocumentsListComponent } from './worker-documents-list.component';

describe('WorkerDocumentsListComponent', () => {
  let component: WorkerDocumentsListComponent;
  let fixture: ComponentFixture<WorkerDocumentsListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkerDocumentsListComponent]
    });
    fixture = TestBed.createComponent(WorkerDocumentsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
