import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaiEnferListComponent } from './pai-enfer-list.component';

describe('PaiEnferListComponent', () => {
  let component: PaiEnferListComponent;
  let fixture: ComponentFixture<PaiEnferListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaiEnferListComponent]
    });
    fixture = TestBed.createComponent(PaiEnferListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
