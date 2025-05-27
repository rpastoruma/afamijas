import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RelativesListComponent } from './relatives-list.component';

describe('RelativesListComponent', () => {
  let component: RelativesListComponent;
  let fixture: ComponentFixture<RelativesListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RelativesListComponent]
    });
    fixture = TestBed.createComponent(RelativesListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
