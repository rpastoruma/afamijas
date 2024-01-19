import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BodyLayoutComponent } from './body-layout.component';

describe('BodyLayoutComponent', () => {
  let component: BodyLayoutComponent;
  let fixture: ComponentFixture<BodyLayoutComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BodyLayoutComponent]
    });
    fixture = TestBed.createComponent(BodyLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
