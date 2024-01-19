import { TestBed } from '@angular/core/testing';

import { FrontValuesService } from './front-values.service';

describe('FrontValuesService', () => {
  let service: FrontValuesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FrontValuesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
