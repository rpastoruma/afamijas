import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffContratosListComponent } from './staff-contratos-list.component';

describe('StaffContratosListComponent', () => {
  let component: StaffContratosListComponent;
  let fixture: ComponentFixture<StaffContratosListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StaffContratosListComponent]
    });
    fixture = TestBed.createComponent(StaffContratosListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
