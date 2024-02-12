import { TestBed } from '@angular/core/testing';

import { RelativeAbsencesService } from './relative-absences.service';

describe('RelativeAbsencesService', () => {
  let service: RelativeAbsencesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RelativeAbsencesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
