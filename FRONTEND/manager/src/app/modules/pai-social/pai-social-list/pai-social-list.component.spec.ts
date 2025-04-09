import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaiSocialListComponent } from './pai-social-list.component';

describe('PaiSocialListComponent', () => {
  let component: PaiSocialListComponent;
  let fixture: ComponentFixture<PaiSocialListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaiSocialListComponent]
    });
    fixture = TestBed.createComponent(PaiSocialListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
