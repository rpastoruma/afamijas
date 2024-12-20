
import { HttpClient } from '@angular/common/http';
import { Component, ElementRef, EventEmitter, Inject, Input, OnDestroy, OnInit, Optional, Output, ViewChild } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { NB_DIALOG_CONFIG, NbDialogRef } from '@nebular/theme';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { Editor, Toolbar } from 'ngx-editor';
import pdfMake from 'pdfmake/build/pdfmake';
import pdfFonts from 'pdfmake/build/vfs_fonts';
pdfMake.vfs = pdfFonts.pdfMake.vfs;
import htmlToPdfmake from 'html-to-pdfmake';


@Component({
  selector: 'app-my-html-viewer',
  templateUrl: './my-html-viewer.component.html',
  styleUrls: ['./my-html-viewer.component.scss']
})
export class MyHtmlViewerComponent implements OnInit, OnDestroy {

  @ViewChild('idhtml') pdfTable!: ElementRef;
  
  @Input() htmlSrc : string;
  @Input() thefilename : string;


  editor: Editor;
  toolbar: Toolbar = [
    // default value
    ['bold', 'italic'],
    ['underline', 'strike'],
    ['ordered_list', 'bullet_list'],
    [{ heading: ['h1', 'h2', 'h3', 'h4', 'h5', 'h6'] }],
  ];
  html = '';
  //displayHTML : SafeHtml;

  constructor(
    @Optional() private ref: NbDialogRef<MyHtmlViewerComponent>, @Inject(NB_DIALOG_CONFIG) config,
    private dialogService : NbDialogService,
    private http: HttpClient, 
    private sanitizer: DomSanitizer

  ) { }

  ngOnInit(): void {
    this.editor = new Editor({
      content: '',
      history: true,
      keyboardShortcuts: true,
      inputRules: true,
      plugins: [], //https://prosemirror.net/docs/guide/#state
      nodeViews: {}, //https://prosemirror.net/docs/guide/#state,
      attributes: {}, // https://prosemirror.net/docs/ref/#view.EditorProps.attributes
      linkValidationPattern: ''
    });

    this.http.get(this.htmlSrc , {responseType: 'text'}).subscribe(response => {
      this.html = response;
      if(!this.thefilename || this.thefilename.trim()=='') this.thefilename = 'documento';
      /*
       this.displayHTML = this.sanitizer.bypassSecurityTrustHtml(response);
       console.log("DISPLAY=" + response); 
      */
    });
  }



  // make sure to destory the editor
  ngOnDestroy(): void {
    this.editor.destroy();
  }

  goBack() {
    this.ref.close('close');
  }


  openHTMLExternal()
  {
    window.open(this.htmlSrc);
  }

  saveAsPDF(filename)
  {
    const pdfTable = this.pdfTable.nativeElement;

    const htmlSections = pdfTable.innerHTML.split('#PAGEBREAK#');

    let pdfMakeContent = [];
    htmlSections.forEach((section, index) => {
      const sectionContent = htmlToPdfmake(section);
      pdfMakeContent = pdfMakeContent.concat(sectionContent);

      if (index < htmlSections.length - 1) {
        pdfMakeContent.push({ text: '', pageBreak: 'after' });
      }
    });

    const documentDefinition = {
      content: pdfMakeContent
    };


    pdfMake.createPdf(documentDefinition).download(filename + ".pdf");
    this.ref.close('close');
  }


  /*
  saveAsPDF(filename)
  {
    const pdfTable = this.pdfTable.nativeElement;
    var html = htmlToPdfmake(pdfTable.innerHTML);
    const documentDefinition = { content: html };
    pdfMake.createPdf(documentDefinition).download(filename + ".pdf");
    this.ref.close('close');
  }*/

}
