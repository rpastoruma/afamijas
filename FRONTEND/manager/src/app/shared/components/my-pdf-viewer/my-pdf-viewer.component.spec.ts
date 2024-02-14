import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyPdfViewerComponent } from './my-pdf-viewer.component';

describe('MyPdfViewerComponent', () => {
  let component: MyPdfViewerComponent;
  let fixture: ComponentFixture<MyPdfViewerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MyPdfViewerComponent]
    });
    fixture = TestBed.createComponent(MyPdfViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
