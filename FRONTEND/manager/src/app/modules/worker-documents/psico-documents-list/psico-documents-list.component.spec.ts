import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PsicoDocumentsListComponent } from './psico-documents-list.component';

describe('PsicoDocumentsListComponent', () => {
  let component: PsicoDocumentsListComponent;
  let fixture: ComponentFixture<PsicoDocumentsListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PsicoDocumentsListComponent]
    });
    fixture = TestBed.createComponent(PsicoDocumentsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
