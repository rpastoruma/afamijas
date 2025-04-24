import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffNominasListComponent } from './staff-nominas-list.component';

describe('StaffNominasListComponent', () => {
  let component: StaffNominasListComponent;
  let fixture: ComponentFixture<StaffNominasListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StaffNominasListComponent]
    });
    fixture = TestBed.createComponent(StaffNominasListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
