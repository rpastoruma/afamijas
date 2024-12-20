import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyHtmlViewerComponent } from './my-html-viewer.component';

describe('MyHtmlViewerComponent', () => {
  let component: MyHtmlViewerComponent;
  let fixture: ComponentFixture<MyHtmlViewerComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MyHtmlViewerComponent]
    });
    fixture = TestBed.createComponent(MyHtmlViewerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
