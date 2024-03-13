import { TestBed } from '@angular/core/testing';

import { HealthlogsService } from './healthlogs.service';

describe('HealthlogsService', () => {
  let service: HealthlogsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HealthlogsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
