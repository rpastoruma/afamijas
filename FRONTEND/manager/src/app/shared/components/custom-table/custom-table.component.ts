import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { MAT_MOMENT_DATE_FORMATS, MomentDateAdapter } from '@angular/material-moment-adapter';
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { Subscription } from 'rxjs';
import { LocalStorageService } from 'src/app/core/services/local-storage.service';

@Component({
  selector: 'app-custom-table',
  templateUrl: './custom-table.component.html',
  styleUrls: ['./custom-table.component.scss'],
  providers: [
    {provide: MAT_DATE_LOCALE, useValue: 'es-ES'},
    {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
    {provide: MAT_DATE_FORMATS, useValue: MAT_MOMENT_DATE_FORMATS}
  ]
})
export class CustomTableComponent implements OnInit, OnDestroy {

  @Input() keys: string[];
  @Input() values: any[];
  @Input() actions: any[];
  @Input() widthToCollapse = 700;
  @Input() isProcessing = true;
  @Input() withPagination = true;
  @Input() totalPages = 1;
  @Input() page = 0;
  @Input() size = 10;
  @Input() filters;
  @Input() sort;
  @Input() filterValueDefault;
  @Input() clearable;
  @Input() align;
  @Input() hasExport = false;
  @Input() hasButtonDelete = false;
  @Input() showSearchButton = true;
  isMobileLayout = false;
  @Output() changeFilter: EventEmitter<[number, string]> = new EventEmitter<[number, string]>();
  @Output() actionSelected: EventEmitter<[string, string, string]> = new EventEmitter<[string, string, string]>();
  @Output() changePage: EventEmitter<number> = new EventEmitter<number>();
  @Output() filterEvent: EventEmitter<boolean> = new EventEmitter<boolean>();
  @Output() sortEvent: EventEmitter<[string, boolean]> = new EventEmitter<[string, boolean]>();
  @Output() exportData: EventEmitter<string> = new EventEmitter<string>();
  @Output() deleteCheck: EventEmitter<any> = new EventEmitter<any>();

  editAction: any;
  deleteAction: any;
  awardsAction: any;
  statusAction: any;
  evaluateAction: any;
  staysAbroadAction: any;
  noAction: any;
  transferAction: any;
  duplicateAction: any;
  incidenceAction: any;
  approveAction: any;
  activateAction: any;
  showAction: any;
  changeLanguageEvent: Subscription;
  states = [];
  @Input() loading = false;
  @Input() loadingE = false;
  @Input() submitted = false;

  constructor(
    private adapter: DateAdapter<any>,
    private localStorageService: LocalStorageService
  ) { }

  ngOnInit(): void {
    // this.keys= ['table.key1', 'table.key2', 'table.key3'];
    // this.values = [
    //   {id: 'a', values: ['a2', 'a3', 'a4']},
    //   {id: 'b', values: ['b2', 'b3', 'b4']},
    //   {id: 'c', values: ['c2', 'c3', 'c4']}
    // ];
    // this.filters = [true, undefined, [{id: '1', text: 'table.select1'},{id: '2', text: 'table.select2'},{id: '3', text: 'table.select3'}]];
    // this.actions = [{action: 'edit', text: 'table.tooltip1'}, {action: 'delete', text: 'table.tooltip2'}];
    this.getActions();
    this.getStatesOrder();
    this.setDatepicker();

    this.isMobileLayout = window.innerWidth <= this.widthToCollapse;
    window.onresize = () => this.isMobileLayout = window.innerWidth <= this.widthToCollapse;
  }

  ngOnDestroy() {
    if (this.changeLanguageEvent) {
      this.changeLanguageEvent.unsubscribe();
    }
  }

  getWidth() {
    return 100 / (this.keys.length + 1);
  }

  getStatesOrder() {
    if (this.sort) {
      for (const item of this.sort) {
        if (item === false) { // no ordenación
          this.states.push(false);
        } else if (item.direction === 'up') {
          this.states.push(1);
        } else if (item.direction === 'down') {
          this.states.push(2);
        } else {
          this.states.push(0);
        }
      }
    }
  }

  setDatepicker() {
    // Componente fechas
    this.adapter.setLocale('es-ES');

  }

  getActions() {
    this.editAction = this.actions.find(item => item.action === 'edit');
    this.deleteAction = this.actions.find(item => item.action === 'delete');
    this.awardsAction = this.actions.find(item => item.action === 'awards');
    this.staysAbroadAction = this.actions.find(item => item.action === 'abroads');
    this.statusAction = this.actions.find(item => item.action === 'status');
    this.showAction = this.actions.find(item => item.action === 'show');
    this.evaluateAction = this.actions.find(item => item.action === 'evaluate');
    this.noAction = this.actions.find(item => item.action === 'noaction');
    this.transferAction = this.actions.find(item => item.action === 'transfer');
    this.duplicateAction = this.actions.find(item => item.action === 'duplicate');
    this.incidenceAction = this.actions.find(item => item.action === 'incidence');
    this.approveAction = this.actions.find(item => item.action === 'approve');
    this.activateAction = this.actions.find(item => item.action === 'activate');
  }

  filter(index, value) {
    this.changeFilter.emit([index, value]);
    this.filterBtn();
  }

  setValue(index, value) {
    this.changeFilter.emit([index, value]);
  }

  actionEvent(action, id, value?) {
    //console.log("action=>"+action);
    //console.log("id=>"+id);
    //console.log("value=>"+JSON.stringify(value));
    this.actionSelected.emit([action, id, value]);
  }

  setPageActive(setPage: number) {
    this.page = setPage;
    this.changePage.emit(setPage);
  }

  filterBtn() {
    this.filterEvent.emit(true);
  }

  isArray(data) {
    return Array.isArray(data);
  }

  getFilterType(filter) {
    if (!filter) { return 0; } // no filter
    if (filter === 'datepicker') { return 1; }  // datepicker
    if (Array.isArray(filter)) { return 2; } // select
    if (filter === 'datepickerRange') { return 4; }  // datepicker range
    return 3; // input
  }

  changeStatusSort(index, sort) {
    if (this.states[index] === 0) {
      this.states[index] = 1;
      this.setStates(index);
      this.sortEvent.emit([sort.key, true]);
    } else if (this.states[index] === 1) {
      this.states[index] = 2;
      this.setStates(index);
      this.sortEvent.emit([sort.key, false]);
    } else if (this.states[index] === 2) {
      this.states[index] = 0;
      this.setStates(index);
      this.sortEvent.emit();
    }
  }

  setStates(index) {
    for (let i = 0; i < this.states.length; i++) {
      if (this.states[i] !== false && i !== index) {
        this.states[i] = 0;
      }
    }
  }

  getValueType(value) {
    if (this.checkCheckbox(value)) { return 3; } // checkbox
    if (this.checkObject(value)) { return 0; } // object
    if (this.checkBool(value)) { return 1; }  // bool
    return 2; // text
  }

  checkObject(value) {
    return typeof value === 'object' && value != null && value.text !== 'iconcheck';
  }

  checkBool(value) {
    return typeof value === 'boolean';
  }

  checkCheckbox(value) {
    return typeof value === 'object' && value != null && value.text === 'iconcheck';
  }

  getBool(value) {
    if (value) return 'SÍ'; else return "NO";
  }

  hasAction() {
    return this.awardsAction || this.editAction || this.deleteAction || this.staysAbroadAction
    || this.statusAction || this.showAction || this.evaluateAction || this.noAction || this.transferAction
    || this.incidenceAction || this.approveAction || this.activateAction || this.duplicateAction;
  }

  exportPdf() {
    this.exportData.emit('pdf');
  }

  exportExcel() {
    this.exportData.emit('excel');
  }

  noCheckedRow() {
    if (this.values && this.values.length > 0) {
      const checks = this.values.map(item => item.values[0].value);
      const found = checks.find(item => item === true);
      if (found) {
        return false;
      }
    }
    return true;
  }

  checkRow(id, event) {
    const found = this.values.find(item => item.id === id);
    if (found) {
      found.values[0].value = event.srcElement.checked;
    }
  }

  deleteCheckRow() {
    if (this.values && this.values.length > 0) {
      const checks = this.values.map(item => ({id: item.id, value: item.values[0].value}));
      const selected = checks.filter(item => item.value === true);
      const idsproductions = selected.map(item => item.id);
      this.deleteCheck.emit(idsproductions);
    }
  }

}
