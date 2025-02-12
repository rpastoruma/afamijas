import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AtencionesListComponent } from './atenciones-list.component';

describe('AtencionesListComponent', () => {
  let component: AtencionesListComponent;
  let fixture: ComponentFixture<AtencionesListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AtencionesListComponent]
    });
    fixture = TestBed.createComponent(AtencionesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
