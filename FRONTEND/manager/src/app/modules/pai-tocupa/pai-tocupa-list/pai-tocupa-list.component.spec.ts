import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaiTocupaListComponent } from './pai-tocupa-list.component';

describe('PaiTocupaListComponent', () => {
  let component: PaiTocupaListComponent;
  let fixture: ComponentFixture<PaiTocupaListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaiTocupaListComponent]
    });
    fixture = TestBed.createComponent(PaiTocupaListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
