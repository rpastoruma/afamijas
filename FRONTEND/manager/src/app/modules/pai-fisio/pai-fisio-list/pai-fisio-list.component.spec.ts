import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaiFisioListComponent } from './pai-fisio-list.component';

describe('PaiFisioListComponent', () => {
  let component: PaiFisioListComponent;
  let fixture: ComponentFixture<PaiFisioListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaiFisioListComponent]
    });
    fixture = TestBed.createComponent(PaiFisioListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
