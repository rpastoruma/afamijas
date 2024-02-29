import { TestBed } from '@angular/core/testing';

import { TempsService } from './temps.service';

describe('TempsService', () => {
  let service: TempsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TempsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
