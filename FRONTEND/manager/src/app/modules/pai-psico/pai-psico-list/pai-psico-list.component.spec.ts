import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaiPsicoListComponent } from './pai-psico-list.component';

describe('PaiPsicoListComponent', () => {
  let component: PaiPsicoListComponent;
  let fixture: ComponentFixture<PaiPsicoListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PaiPsicoListComponent]
    });
    fixture = TestBed.createComponent(PaiPsicoListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
