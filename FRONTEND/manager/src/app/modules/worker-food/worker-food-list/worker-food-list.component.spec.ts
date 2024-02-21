import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkerFoodListComponent } from './worker-food-list.component';

describe('WorkerFoodListComponent', () => {
  let component: WorkerFoodListComponent;
  let fixture: ComponentFixture<WorkerFoodListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkerFoodListComponent]
    });
    fixture = TestBed.createComponent(WorkerFoodListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
