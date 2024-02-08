import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RelativeRouteComponent } from './relative-route.component';

describe('RelativeRouteComponent', () => {
  let component: RelativeRouteComponent;
  let fixture: ComponentFixture<RelativeRouteComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RelativeRouteComponent]
    });
    fixture = TestBed.createComponent(RelativeRouteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
