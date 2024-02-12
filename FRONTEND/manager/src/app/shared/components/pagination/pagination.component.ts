import { Component, OnInit, Input, Output, EventEmitter, OnChanges } from '@angular/core';
import { FillPipe } from '../../pipes/fill.pipe';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss'],
  providers: [FillPipe]
})
export class PaginationComponent implements OnInit, OnChanges {

  @Input() totalPages: number;
  @Input() pageActive: number;
  @Output() chagePage: EventEmitter<number> = new EventEmitter();
  pages: number[];
  pointLeft = false;
  pointRight = false;

  constructor(private fillPipe: FillPipe) { }

  ngOnInit(): void {
  }

  ngOnChanges() {
    this.initPagination();
  }

  initPagination() {
    this.pages = this.fillPipe.transform(this.totalPages);
    this.pointLeft = false;
    this.pointRight = false;
    if (this.totalPages > 6) {
      const leftDistance = this.pageActive - 1;
      const rightDistance = this.totalPages - this.pageActive;
      if (leftDistance > 2 && rightDistance > 2) {
        this.pages = this.pages.filter(item => item >= this.pageActive && item <= this.pageActive + 1);
        this.pointLeft = true;
        this.pointRight = true;
      } else if (leftDistance > 2 ) {
        this.pages = this.pages.filter(item => item >= this.totalPages - 3);
        this.pointLeft = true;
      } else if (rightDistance > 2 ) {
        this.pages = this.pages.filter(item => item <= 4);
        this.pointRight = true;
      }
    }
  }

  setPageActive(setPage: number) {
    if (setPage >= 0 && setPage < this.totalPages && setPage !== this.pageActive) {
      this.pageActive = setPage;
      this.chagePage.emit(this.pageActive);
      this.initPagination();
    }
  }
}
