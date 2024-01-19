import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbsencesListComponent } from './absences-list.component';

describe('AbsencesListComponent', () => {
  let component: AbsencesListComponent;
  let fixture: ComponentFixture<AbsencesListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AbsencesListComponent]
    });
    fixture = TestBed.createComponent(AbsencesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
