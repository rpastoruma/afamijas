import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbsencesWorkerListComponent } from './absences-worker-list.component';

describe('AbsencesWorkerListComponent', () => {
  let component: AbsencesWorkerListComponent;
  let fixture: ComponentFixture<AbsencesWorkerListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AbsencesWorkerListComponent]
    });
    fixture = TestBed.createComponent(AbsencesWorkerListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
