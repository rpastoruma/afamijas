import { TestBed } from '@angular/core/testing';

import { FeedingsService } from './feedings.service';

describe('FeedingsService', () => {
  let service: FeedingsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FeedingsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
