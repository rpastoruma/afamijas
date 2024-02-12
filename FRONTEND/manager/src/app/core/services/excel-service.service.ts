import { Injectable } from '@angular/core';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';
const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

@Injectable({
  providedIn: 'root'
})
export class ExcelService {

  constructor() { }

  public exportAsExcelFile(titulo: string, json: any[], excelFileName: string, fields: string[]): void {
    const wscols = [];
    for (const i of fields) {
      wscols.push({wpx: 200});
    }

    const worksheetTitle: XLSX.WorkSheet = XLSX.utils.aoa_to_sheet([[titulo]]);
    worksheetTitle['!cols'] = wscols;
    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(json);

    XLSX.utils.sheet_add_json(worksheetTitle, json, {origin: 1, skipHeader: true});
    const workbook: XLSX.WorkBook = { Sheets: { data: worksheetTitle }, SheetNames: ['data'] };
    const excelBuffer: any = XLSX.write(workbook, { bookType: 'xlsx', type: 'array'});

    this.saveAsExcelFile(excelBuffer, excelFileName);
  }

  private saveAsExcelFile(excelBuffer: any, fileName: string): void {
    const data: Blob = new Blob([excelBuffer], {
      type: EXCEL_TYPE
    });
    FileSaver.saveAs(data, fileName + EXCEL_EXTENSION);
  }
}
