import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SocialWorkerListComponent } from './social-worker-list.component';

describe('SocialWorkerListComponent', () => {
  let component: SocialWorkerListComponent;
  let fixture: ComponentFixture<SocialWorkerListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SocialWorkerListComponent]
    });
    fixture = TestBed.createComponent(SocialWorkerListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
