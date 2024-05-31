import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MembersReceiptsListComponent } from './members-receipts-list.component';

describe('MembersReceiptsListComponent', () => {
  let component: MembersReceiptsListComponent;
  let fixture: ComponentFixture<MembersReceiptsListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MembersReceiptsListComponent]
    });
    fixture = TestBed.createComponent(MembersReceiptsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
