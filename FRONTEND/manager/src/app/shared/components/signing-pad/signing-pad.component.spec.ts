import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SigningPadComponent } from './signing-pad.component';

describe('SigningPadComponent', () => {
  let component: SigningPadComponent;
  let fixture: ComponentFixture<SigningPadComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SigningPadComponent]
    });
    fixture = TestBed.createComponent(SigningPadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
