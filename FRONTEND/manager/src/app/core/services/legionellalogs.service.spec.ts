import { TestBed } from '@angular/core/testing';

import { LegionellalogsService } from './legionellalogs.service';

describe('LegionellalogsService', () => {
  let service: LegionellalogsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LegionellalogsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
