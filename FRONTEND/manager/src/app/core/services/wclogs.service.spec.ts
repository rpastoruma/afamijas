import { TestBed } from '@angular/core/testing';

import { WclogsService } from './wclogs.service';

describe('WclogsService', () => {
  let service: WclogsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WclogsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
