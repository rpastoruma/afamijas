import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaiListComponent } from './pai-list.component';

describe('PaiListComponent', () => {
  let component: PaiListComponent;
  let fixture: ComponentFixture<PaiListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaiListComponent]
    });
    fixture = TestBed.createComponent(PaiListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
