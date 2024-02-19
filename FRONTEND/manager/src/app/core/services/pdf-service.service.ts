import { Injectable } from '@angular/core';
import * as pdfMake from 'pdfmake/build/pdfmake.js';
import * as pdfFonts from 'pdfmake/build/vfs_fonts.js';
import { HttpClient } from '@angular/common/http';
pdfMake.vfs = pdfFonts.pdfMake.vfs;
import { formatDate } from '@angular/common';
import { registerLocaleData } from '@angular/common';
import localeES from '@angular/common/locales/es';


@Injectable({
  providedIn: 'root'
})
export class PdfService {
  public logo;
  logo2;
  datepdf;

  constructor(
    public httpClient: HttpClient,
  ) {
    this.logo2 = '../../assets/logo.png';
    registerLocaleData(localeES);
  }

  exportDataToPDF(titulo: string, fields, fieldsNT, titleFinal: string, result, minFields) {
    this.httpClient.get<Blob>(this.logo2, { responseType: 'blob' as 'json' }).subscribe(
      res => {
        // // img to base64
        const reader = new FileReader();
        let base64data;
        reader.readAsDataURL(res);
        reader.onloadend = () => {
          base64data = reader.result;
           // console.log(base64data);
          this.logo = base64data;

          const pdf = pdfMake;

          const currClassRef = this;

          const date = new Date();
          this.datepdf = formatDate(date, 'dd/MM/yyyy', 'es');
          function buildTableBody(data, columns, field) {
            const body = [];
            body.push(field);

            // tslint:disable-next-line:only-arrow-functions
            data.forEach(function(row) {
              const dataRow = [];
              // tslint:disable-next-line:only-arrow-functions
              columns.forEach(function(column) {
                if (row[column]) {
                  console.log(row[column]);
                  dataRow.push(row[column].toString());
                } else {
                  dataRow.push('');
                }
              });
              body.push(dataRow);
            });
            return body;
        }

        const docDefinition = {
          pageSize: 'A4',
          pageOrientation: 'landscape',
          pageMargins: [40, 80, 40, 50],

          header: {
            stack: [
              {image: this.logo, width: 100}
            ],
            margin: [40, 30, 0, 0],
          },
          footer: (currentPage, pageCount) => {
            return { text: (currentPage.toString() + ' de ' + pageCount), alignment: 'center' };
          },
          content: [
            { text: this.datepdf, style: 'dateG'},
            { text: titulo, style: 'header' },
            {
              layout:  'lightHorizontalLines', // optional
              table: {
                // headers are automatically repeated if the table spans over multiple pages
                // you can declare how many rows should be treated as headers
                headerRows: 1,
                widths: minFields,
                margins: [0, 100],
                body: buildTableBody(result, fieldsNT, fields)
              }
            }
          ],
          styles: {
            header: {
              fontSize: 18,
              alignment: 'center',
              bold: true,
              margin: [0, 20, 0, 10]
            },
            dateG: {
              alignment: 'right',
            }
          }
        };
        pdf.createPdf(docDefinition).download(titleFinal + '.pdf');
      };
    });
  }
}
