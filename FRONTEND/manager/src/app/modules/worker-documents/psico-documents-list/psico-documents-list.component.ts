import { Component, ElementRef, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { PatientDTO, parseDataExport, ActionDTO, rolName, DocDTO, DocPsicoDTO, MemberDTO } from 'src/app/shared/models/models';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DocsService } from 'src/app/core/services/docs.service';
import { Subscription, catchError, finalize, of } from 'rxjs';
import { MediaService } from 'src/app/core/services/media.service';
import { HttpClient, HttpEventType } from '@angular/common/http';
import { UsersService } from 'src/app/core/services/users.service';
import { PatientsService } from 'src/app/core/services/patients.service';
import { MyHtmlViewerComponent } from 'src/app/shared/components/my-html-viewer/my-html-viewer.component';

import pdfMake from 'pdfmake/build/pdfmake';
import pdfFonts from 'pdfmake/build/vfs_fonts';
pdfMake.vfs = pdfFonts.pdfMake.vfs;
import htmlToPdfmake from 'html-to-pdfmake';

@Component({
  selector: 'app-psico-documents-list',
  templateUrl: './psico-documents-list.component.html',
  styleUrls: ['./psico-documents-list.component.scss']
})
export class PsicoDocumentsListComponent implements OnInit {

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;
  modalRef?: NgbModalRef;
  @ViewChild('modalContent1', { static: true }) modalContent1: TemplateRef<any>;
  modalRef1?: NgbModalRef;
  @ViewChild('modalContent2', { static: true }) modalContent2: TemplateRef<any>;
  modalRef2?: NgbModalRef;

  @ViewChild('firstTextarea') firstTextarea!: ElementRef;
  

  //PARÁMETROS LISTADO
  theIdpatient : string = '';
  theType : string = '';
  theText : string = '';

  page : number = 0;
  size : number = 4;
  totalPages : number = 0;

  requiredFileType:string  = ".png,.jpg,.jpeg,.webp,.gif,.pdf,.doc,.docx,.xsl,.xslx";
  fileName = '';
  uploadProgress:number;
  uploadSub: Subscription;

  theDoc : DocPsicoDTO = {
    id: '',
    idworker: '',
    worker_fullname: '',
    description: '',
    url: '',
    idpatient: '',
    patient_fullname: '',
    type: '',
    created: undefined
  };
  
  
  hayreport : boolean = false;
  @ViewChild('idhtml') pdfTable!: ElementRef;

  hayreport2 : boolean = false;
  @ViewChild('idhtml2') pdfTable2!: ElementRef;
  
  
  thePatient : PatientDTO = {
    id: '',
    username: '',
    email: '',
    name: '',
    surname1: '',
    surname2: '',
    documentid: '',
    documenttype: 'DNI',
    phone: '',
    postaladdress: '',
    idcity: undefined,
    cityname: '',
    idstate: undefined,
    statename: '',
    postalcode: '',
    signature: '',
    photo_url: '',
    gender: '',
    idrelative: '',
    relativefullname: '',
    is_principal_keeper: '',
    principal_keeper_fullname: '',
    principal_keeper_phone: '',
    routeDTO: undefined,
    menu_type: '',
    breakfast_description: '',
    medication_description_morning: '',
    medication_description_evening: '',
    groupcode: '',
    birthdate: undefined,
    servicetype: '',
    relativerelation: '',
    idcountry: undefined,
    countryname: '',
    tallerpsico: false,
    transportservice: false,
    transportservice_text: '',
    comedorservice: false,
    comedorservice_text: '',
    ayudadomicilioservice: false,
    ayudadomicilioservice_text: '',
    duchaservice: false,
    duchaservice_text: '',
    register_document_url: '',
    register_document_url_signed: '',
    unregister_document_url: '',
    unregister_document_url_signed: '',
    unregister_reason: '',
    status: '',
    fullname: '',
    register19_document_url: '',
    register19_document_url_signed: '',
    register20_document_url: '',
    register20_document_url_signed: '',
    register24_document_url: '',
    register24_document_url_signed: '',
    register25_document_url: '',
    register25_document_url_signed: '',
    register26_document_url: '',
    register26_document_url_signed: '',
    register27_document_url: '',
    register27_document_url_signed: '',
    register21_document_url: '',
    register21_document_url_signed: '',
    register22_document_url: '',
    register22_document_url_signed: '',
    register23_document_url: '',
    register23_document_url_signed: '',
    register28_document_url: '',
    register28_document_url_signed: '',
    register29_document_url: '',
    register29_document_url_signed: '',
    register30_document_url: '',
    register30_document_url_signed: '',
    language: 'ES',
    fs_fecha_inscripcion: undefined,
    fs_num_expediente: '',
    fs_num_ss: '',
    fs_estado_civil: '',
    fs_talleres_estimulacion: false,
    fs_gradior_stimmulus: false,
    fs_sad: false,
    fs_other: false,
    fs_other_text: '',
    fs_comer_solo: false,
    fs_lavarse_solo: false,
    fs_salir_sin_perderse: false,
    fs_reconocer_caras: false,
    fs_leer_y_escribir: false,
    fs_incontenencia_urinaria: false,
    fs_conversar: false,
    fs_reconocer_objetos_cotidianos: false,
    fs_sufrir_alucinaciones: false,
    fs_fases_agitacion: false,
    fs_dificultad_orientarse: false,
    fs_movilizarse: 'Autónomo',
    fs_datos_medicos: '',
    fs_grado_minusvalia: false,
    fs_grado_minusvalia_text: '',
    fs_grado_dependencia: false,
    fs_grado_dependencia_text: '',
    fs_incapacitacion_judicial: false,
    fs_ayudas_externas: false,
    fs_ayudas_externas_text: '',
    fs_url: '',
    hs_beca: false,
    hs_diagnostico: '',
    hs_autonomia: false,
    hs_ayuda_abd: false,
    hs_uc_solo: false,
    hs_uc_conyuge: false,
    hs_uc_hijos: false,
    hs_uc_other: false,
    hs_uc_other_text: '',
    hs_nivel_formativo: '',
    hs_interaccion_demas: '',
    hs_interaccion_profesioneales: '',
    hs_participacion_actividades: '',
    hs_integracion_dinamica: '',
    hs_grado_minusvalia_tipo: '',
    hs_grado_minusvalia_cuando: '',
    hs_ley_dependencia_solicitada: false,
    hs_ley_dependencia_grado: '',
    hs_recibe_servicio_administracion: false,
    hs_patologias: '',
    hs_diabetico: false,
    hs_hipertenso: false,
    hs_alimenta_bien: false,
    hs_duerme_bien: false,
    hs_fuma_bebe: false,
    hs_drogas: false,
    hs_drogas_text: '',
    hs_valoracion_salud: '',
    hs_fam_dificultades_convivencia: false,
    hs_fam_dificultades_economicas: false,
    hs_fam_dificultad_cuidados: false,
    hs_fam_sin_apoyo: false,
    hs_fam_agotamiento_cuidador: false,
    hs_viv_sin_domicilio: false,
    hs_viv_ruinas: false,
    hs_viv_barreras: false,
    hs_viv_inhabitabilidad: false,
    hs_alquiler_elevado: false,
    hs_escaleras_exteriores: false,
    hs_escaleras_interiores: false,
    hs_banera: false,
    hs_alfombras: false,
    hs_otros: false,
    hs_otros_text: '',
    hs_nombre1: '',
    hs_parentesco1: '',
    hs_edad1: 0,
    hs_profesion1: '',
    hs_nombre2: '',
    hs_parentesco2: '',
    hs_edad2: 0,
    hs_profesion2: '',
    hs_nombre3: '',
    hs_parentesco3: '',
    hs_edad3: 0,
    hs_profesion3: '',
    hs_nombre4: '',
    hs_parentesco4: '',
    hs_edad4: 0,
    hs_profesion4: '',
    hs_tiene_pareja: false,
    hs_relacion_pareja: '',
    hs_tiene_hijos: false,
    hs_relacion_hijos: '',
    hs_tiene_hermanos: false,
    hs_relacion_hermanos: '',
    hs_visitas_familiares: false,
    hs_visitas_cuanto: '',
    hs_apoyo_amigos: false,
    hs_relacion_familia: false,
    hs_acude_otras: false,
    hs_recibe_pension: false,
    hs_cuantia_pension: 0,
    hs_otra_prestacion: '',
    hs_otros_ingresos: '',
    hs_otros_recursos: '',
    hs_valoracion_profesional: '',
    hs_observaciones: '',
    hs_url: '',
    is_url: '',
    is_tiempo_conoce_usuario: '',
    is_servicios_prestados: '',
    is_como_adaptado: '',
    is_acudio_voluntad_propia: false,
    is_quien_influyo_decision: '',
    is_que_actividades: '',
    is_como_relaciona: '',
    is_como_pasa_dia: '',
    is_problemas_psico: false,
    is_problemas_psico_text: '',
    is_recibe_tratamiento: false,
    is_recibe_tratamiento_text: '',
    is_familia_estru: false,
    is_familia_estru_text: '',
    is_recibe_ingresos_actividad_laboral: false,
    is_esta_buscando_empleo: false,
    is_vive_en: '',
    is_cubiertas_necesidades_diarias: false,
    is_valoracion_profesional: '',
    is_propuesta: '',
    ins_fecha_informe: new Date(),
    ins_motivo_consulta: '',
    ins_antecedentes: '',
    ins_diagnostico: '',
    ins_texto_pre_puntuaciones: '',
    ins_fecha1: new Date(),
    ins_fecha2: new Date(),
    ins_fecha3: new Date(),
    ins_fecha4: new Date(),
    ins_orientacion1: 0,
    ins_orientacion2: 0,
    ins_orientacion3: 0,
    ins_orientacion4: 0,
    ins_lenguaje1: 0,
    ins_lenguaje2: 0,
    ins_lenguaje3: 0,
    ins_lenguaje4: 0,
    ins_memoria1: 0,
    ins_memoria2: 0,
    ins_memoria3: 0,
    ins_memoria4: 0,
    ins_atencalculo1: 0,
    ins_atencalculo2: 0,
    ins_atencalculo3: 0,
    ins_atencalculo4: 0,
    ins_praxis1: 0,
    ins_praxis2: 0,
    ins_praxis3: 0,
    ins_praxis4: 0,
    ins_pensabstracto1: 0,
    ins_pensabstracto2: 0,
    ins_pensabstracto3: 0,
    ins_pensabstracto4: 0,
    ins_percecpcion1: 0,
    ins_percecpcion2: 0,
    ins_percecpcion3: 0,
    ins_percecpcion4: 0,
    ins_total1: 0,
    ins_total2: 0,
    ins_total3: 0,
    ins_total4: 0,
    ins_fecha_mms1: new Date(),
    ins_fecha_mms2: new Date(),
    ins_fecha_mms3: new Date(),
    ins_fecha_mms4: new Date(),
    ins_mmse1: 0,
    ins_mmse2: 0,
    ins_mmse3: 0,
    ins_mmse4: 0,
    ins_texto_post_puntuaciones: '',
    ins_fecha_ind1: new Date(),
    ins_fecha_ind2: new Date(),
    ins_fecha_ind3: new Date(),
    ins_indbathel1: 0,
    ins_indbathel2: 0,
    ins_indbathel3: 0,
    ins_indlawton1: 0,
    ins_indlawton2: 0,
    ins_indlawton3: 0,
    ins_texto_eval_conductual: '',
    ins_texto_conclusion: '',
    ins_url: '',
    ips_url: '',
    ips_fecha_informe: new Date(),
    ips_sanitarios: '',
    ips_sociofamiliar: '',
    ips_evalcognitiva: '',
    ips_evalconductual: '',
    ips_evalfuncional: '',
    ips_situacioneconomica: '',
    ips_observaciones: '',
    num_contrato: '',
    pai_fisio_fecha_valoracion: undefined,
    pai_fisio_prob_salud: '',
    pai_fisio_dolres: '',
    pai_fisio_duerme: '',
    pai_fisio_nec_aliment: '',
    pai_fisio_hab_saludables: '',
    pai_fisio_atencion_preven: '',
    pai_fisio_acceso_atencion: '',
    pai_fisio_medicacion_requerida: '',
    pai_fisio_alergias: '',
    pai_fisio_upp: '',
    pai_fisio_autonomo: '',
    pai_fisio_ayudas_tecnicas: '',
    pai_fisio_movilidad_mmss: '',
    pai_fisio_movilidad_mmii: '',
    pai_fisio_movilidad_cuello: '',
    pai_fisio_movilida_tronco: '',
    pai_fisio_equilibrio: '',
    pai_fisio_bipedestacion: '',
    pai_fisio_marcha: '',
    pai_fisio_riesgo_caidas: '',
    pai_fisio_deformidades: '',
    pai_fisio_disfruta_ocio: '',
    pai_fisio_espacios_ocio: '',
    pai_fisio_relaciones_entorno: '',
    pai_fisio_objetivos: '',
    pai_fisio_tratamiento: '',
    pai_fisio_valoraciones: '',
    pai_fisio_actuaciones: '',
    pai_fisio_incidencias: '',
    pai_fisio_url: ''
  }


  docs: any[]  = []; // amy => formato del listado
  docsObjects: DocPsicoDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];

  allPatients : MemberDTO[] = [];
  

  constructor(
    public toastService: NbToastrService,
    private mediaService : MediaService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private docsService : DocsService,
    private usersService : UsersService,
    private dialogService : NbDialogService,
    private patientsService : PatientsService,
    private http: HttpClient

  ) {
   }

  ngOnInit(): void {
    if(!this.canModify()) this.actions = [{action: 'show', text: 'Abrir documento'}];
    if(this.canModify()) this.actions = [{action: 'show', text: 'Abrir documento'}, {action: 'edit', text: 'Modificar documento'}, {action: 'delete', text: 'Eliminar documento'}];
    this.getAllPatients();
  }


  roleName(therole) { return rolName(therole); }
  

  
  getAllPatients()
  {
      this.isProcessing = true;
      this.usersService.getAllPatients().subscribe(
        res => {
          this.allPatients = res;
          this.getDocsPsico(0);
          this.isProcessing = false;
        },
        error => 
        {
          console.error("getAllPatients():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los usuarios.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
  }


    
  getPatientById()
  {
      this.isProcessing = true;
      this.usersService.getPatientById(this.theIdpatient).subscribe(
        res => {
          this.thePatient = res;

          this.thePatient.ins_fecha_informe = this.localDateTime2Date(res.ins_fecha_informe);
          this.thePatient.ins_fecha1 = this.localDateTime2Date(res.ins_fecha1);
          this.thePatient.ins_fecha2 = this.localDateTime2Date(res.ins_fecha2);
          this.thePatient.ins_fecha3 = this.localDateTime2Date(res.ins_fecha3);
          this.thePatient.ins_fecha4 = this.localDateTime2Date(res.ins_fecha4);
          this.thePatient.ins_fecha_mms1 = this.localDateTime2Date(res.ins_fecha_mms1);
          this.thePatient.ins_fecha_mms2 = this.localDateTime2Date(res.ins_fecha_mms2);
          this.thePatient.ins_fecha_mms3 = this.localDateTime2Date(res.ins_fecha_mms3);
          this.thePatient.ins_fecha_mms4 = this.localDateTime2Date(res.ins_fecha_mms4);
          this.thePatient.ins_fecha_ind1 = this.localDateTime2Date(res.ins_fecha_ind1);
          this.thePatient.ins_fecha_ind2 = this.localDateTime2Date(res.ins_fecha_ind2);
          this.thePatient.ins_fecha_ind3 = this.localDateTime2Date(res.ins_fecha_ind3);
          this.thePatient.ips_fecha_informe = this.localDateTime2Date(res.ips_fecha_informe);


          this.isProcessing = false;
        },
        error => 
        {
          console.error("getPatientById():"+JSON.stringify(error));
        }
      );
  }


  getDocsPsico(page :number) 
  {
    this.hayreport = false;
    this.hayreport2 = false;

    this.isProcessing = true;
    //if(!this.dayfrom) this.dayfrom = null;
    //if(!this.dayto) this.dayto = null;
    this.page = page
    this.docsService.getDocsPsico(this.page, this.size, this.theIdpatient, this.theType, this.theText).subscribe(
      res => {
        this.isProcessing = false;
        this.docs = res.content.map(item => { return {id: item.id, values: [item.patient_fullname, this.translateType(item.type), item.description, this.date2Text1(item.created), item.worker_fullname ] }; });
        this.docsObjects = res.content;

        this.totalPages = res.totalPages;

        if(this.theIdpatient!='')
          this.getPatientById();
      },
      error => {
        this.isProcessing = false;
        console.error("getDocs():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener los documentos.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  translateType(type :string)
  {
    if(type=='PSICO_CONGNITIVA') return 'Evaluación Cognitiva';
    else if(type=='PSICO_CONDUCTUAL') return 'Evaluación Conductual';
    else if(type=='PSICO_FUNCIONAL') return 'Evaluación Funcional';
    else return type;
  }

  

  
  getExportData(format: string) 
  {
    if(format === 'excel') this.loadingExcel = true;
    else if(format === 'pdf') this.loadingPDF = true;



    this.docsService.getDocsPsico(0, 100000000, this.theIdpatient, this.theType, this.theText).subscribe(
      res => {
        const header = {};
        const keys = ['Usuario:', 'Tipo documento', 'Descripción:',  'Subido:',  'Subido por:'];
        const fields = ['patient_fullname', 'type', 'description', 'created', 'worker_fullname'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.patient_fullname, this.translateType(item.type), item.description, this.date2Text1(item.created), item.worker_fullname  ]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Documentos Psicología';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['20%','10%','40%','10%','20%'];
          const keystranslate = keys.map(item => item);
          this.pdfService.exportDataToPDF(title, keystranslate, fields, title, final, minFields);
          this.loadingPDF = false;
        }
      },
      _ => {
        this.loadingExcel = false;
        this.loadingPDF = false;
      }
    );
  }


  filter(page: number) {
    this.getDocsPsico(page);
  }


  setPage(event) {
    this.page = event;
    this.getDocsPsico(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'edit') 
    {
      const selected = { ...this.docsObjects.find(item => item.id === event[1])};
      this.openAddDocumentModal(selected);
    } 
    else if (event && event[0] === 'delete') 
    {
      this.deleteDoc(event[1]);
    }
    else if (event && event[0] === 'show') 
    {
      const selected =  {...this.docsObjects.find(item => item.id === event[1])};
      window.open(selected.url);
    } 

  }


  canModify() : boolean
  {
    return this.authService.isManager() || this.authService.isAdmin();
  }



  openAddDocumentModal(selected? : DocPsicoDTO)
  {
    console.warn(JSON.stringify(selected));
    if(!selected)
    {
      this.fileName = undefined;
      this.theDoc.idpatient = '';
      this.theDoc.id =''; 
      this.theDoc.url =''; 
      this.theDoc.type =''; 
      this.theDoc.description ='';
    }
    else
    {
      this.theDoc = selected;
    }



    this.modalRef = this.modal.open(this.modalContent, { size: 'lg' });
  }


  closeModal(): void {
    if (this.modalRef) {
      this.modalRef.close(); // Cierra el modal
    }
  }
  

  openAddDocumentModal1()
  {



    this.modalRef1 = this.modal.open(this.modalContent1, { size: 'lg' });
  }

  
  openAddDocumentModal2()
  {

    this.modalRef2 = this.modal.open(this.modalContent2, { size: 'lg' });

  }

  closeModal1(): void {
    if (this.modalRef1) {
      this.modalRef1.close(); // Cierra el modal
    }
  }

  closeModal2(): void {
    if (this.modalRef2) {
      this.modalRef2.close(); // Cierra el modal
    }
  }


  saveDoc()
  {

    if(this.theDoc.url == '') 
    {
      this.toastService.show("Debes seleccionar el documento a subir primero.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return;
    }

    this.theDoc.idpatient = this.theIdpatient;


    this.docsService.saveDocPsico(this.theDoc).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Documento grabado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getDocsPsico(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("saveDoc():"+JSON.stringify(error));
        this.toastService.show("No se ha podido añadir el documento.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


  deleteDoc(id : string)
  {
    this.docsService.deleteDocPsico(id).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Documento eliminado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getDocsPsico(this.page);
      },
      error => {
        this.isProcessing = false;
        console.error("deleteFeeding():"+JSON.stringify(error));
        this.toastService.show("No se ha podido eliminar el documento.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }



  


async onFileSelected(event) {

  const file:File = event.target.files[0];

  if (file) 
  {
    const upload$ = this.mediaService.uploadFile("document", file).pipe(finalize(() => this.reset())
  )
  ;


  this.uploadSub = upload$.subscribe(event => {
        if (event.type == HttpEventType.UploadProgress) 
        {
          this.uploadProgress = Math.round(100 * (event.loaded / event.total));
        }
        else if(event.type == HttpEventType.Response && event.status == 200 )
        {
          if(event.url && event.url.startsWith("https://") )
          {
            this.theDoc.url = event.url;
          }
          else
            console.error("onFileSelected1():"+JSON.stringify(event));
        }
        else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
        {
          console.error("onFileSelected2():"+JSON.stringify(event));
          this.toastService.show("No se ha podido subir el fichero.",
            "¡Ups!", 
            { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        }
        else 
        {
          if(event.url && event.url.startsWith("https://") ) 
            this.theDoc.url = event.url;
          else
            console.error("onFileSelected3():"+JSON.stringify(event));
        }
      }
    )
  }
}


cancelUpload() {
this.uploadSub.unsubscribe();
this.reset();
}

reset() {
this.uploadProgress = null;
this.uploadSub = null;

}


  disabledDay(date) {
    return false;
    /*
    date.setDate(date.getDate());
   return date > new Date();
   */
  }

  
  //[2024,3,1,13,0,...] --> Date
  localDateTime2Date(thedate : any) : Date
  {
    if(!thedate || thedate.length==0) return null;
    let sdate = '';
    if(thedate[0]) sdate += thedate[0];
    if(thedate[1]) sdate += ('-' + this.completeZeros(thedate[1]));
    if(thedate[2]) sdate += ('-' + this.completeZeros(thedate[2]));
    if(thedate[3] || thedate[3]==0) sdate += ('T' + this.completeZeros(thedate[3]));
    if(thedate[4] || thedate[4]==0) sdate += (':' + this.completeZeros(thedate[4]));
    if(thedate[5] || thedate[5]==0) sdate += (':' + this.completeZeros(thedate[5]));
    if(thedate[6] || thedate[6]==0) sdate += ('.' + thedate[6]);

    const result : Date = new Date(Date.parse(sdate));
    if(result.getFullYear()==2100) return null;
    return result;
  }

  date2Text(thedate : any)
  {
    if(!thedate) return '';
    if(thedate instanceof Date) return this.date2Text2(thedate);
    else return this.date2Text1(thedate);
  }

  date2Text1(thelocaldatetime : number[])
  {
    if(!thelocaldatetime) return '';
    let thedate : Date = this.localDateTime2Date(thelocaldatetime);
    if(!thedate) return '';
    let daysOfWeek = [ 'domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado' ];
    const dayOfWeek = daysOfWeek[thedate.getDay()];
    
    if(thedate.getFullYear()==2100) return 'No expira';

    return 'El ' + daysOfWeek[thedate.getDay()] + " " + this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"");
  }


  date2Text2(thedate : Date)
  {
    if(!thedate) return '';
    let daysOfWeek = [ 'domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado' ];
    const dayOfWeek = daysOfWeek[thedate.getDay()];
    //return 'el ' + daysOfWeek[thedate.getDay()] + " " + this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"") + " a las " +  this.completeZeros(thedate.getHours()) + ":" + this.completeZeros(thedate.getMinutes()) + " h.";
    return 'el ' + daysOfWeek[thedate.getDay()] + " " + this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"");
  }




  completeZeros(x : number) : string
  {
    if(x<=9) return "0" + x;
    else return ""+x;
    
  }
  goStep(step :string)
  {

    document.getElementById('step1').style.display = 'none';
    document.getElementById('step2').style.display = 'none';
    document.getElementById('step3').style.display = 'none';
    document.getElementById('step4').style.display = 'none';
    document.getElementById('step5').style.display = 'none';
    document.getElementById('step6').style.display = 'none';
    document.getElementById('step7').style.display = 'none';
    document.getElementById('step8').style.display = 'none';

    document.getElementById(step).style.display = 'block';
  }

  goStepB(step :string)
  {

    document.getElementById('stepB1').style.display = 'none';
    document.getElementById('stepB2').style.display = 'none';
    document.getElementById('stepB3').style.display = 'none';
    document.getElementById('stepB4').style.display = 'none';
    document.getElementById('stepB5').style.display = 'none';
    document.getElementById('stepB6').style.display = 'none';
    document.getElementById('stepB7').style.display = 'none';

    document.getElementById(step).style.display = 'block';
  }


updateTotal1() {
    this.thePatient.ins_total1 = 
        (this.thePatient.ins_orientacion1 ?? 0) + 
        (this.thePatient.ins_lenguaje1 ?? 0) + 
        (this.thePatient.ins_memoria1 ?? 0) + 
        (this.thePatient.ins_atencalculo1 ?? 0) + 
        (this.thePatient.ins_praxis1 ?? 0) + 
        (this.thePatient.ins_pensabstracto1 ?? 0) + 
        (this.thePatient.ins_percecpcion1 ?? 0);
}

updateTotal2() {
  this.thePatient.ins_total2 = 
      (this.thePatient.ins_orientacion2 ?? 0) + 
      (this.thePatient.ins_lenguaje2 ?? 0) + 
      (this.thePatient.ins_memoria2 ?? 0) + 
      (this.thePatient.ins_atencalculo2 ?? 0) + 
      (this.thePatient.ins_praxis2 ?? 0) + 
      (this.thePatient.ins_pensabstracto2 ?? 0) + 
      (this.thePatient.ins_percecpcion2 ?? 0);
}

updateTotal3() {
  this.thePatient.ins_total3 = 
      (this.thePatient.ins_orientacion3 ?? 0) + 
      (this.thePatient.ins_lenguaje3 ?? 0) + 
      (this.thePatient.ins_memoria3 ?? 0) + 
      (this.thePatient.ins_atencalculo3 ?? 0) + 
      (this.thePatient.ins_praxis3 ?? 0) + 
      (this.thePatient.ins_pensabstracto3 ?? 0) + 
      (this.thePatient.ins_percecpcion3 ?? 0);
}

updateTotal4() {
  this.thePatient.ins_total4 = 
      (this.thePatient.ins_orientacion4 ?? 0) + 
      (this.thePatient.ins_lenguaje4 ?? 0) + 
      (this.thePatient.ins_memoria4 ?? 0) + 
      (this.thePatient.ins_atencalculo4 ?? 0) + 
      (this.thePatient.ins_praxis4 ?? 0) + 
      (this.thePatient.ins_pensabstracto4 ?? 0) + 
      (this.thePatient.ins_percecpcion4 ?? 0);
}


disabledDay2(date) 
{
   date.setDate(date.getDate());
   return date >= new Date();
}


openDocumentRegisterHTML(thedocument : string, thefilename : string, pre : boolean)
  {
    this.dialogService.open(MyHtmlViewerComponent, { 
      hasScroll: true, 
      closeOnBackdropClick: false,
      closeOnEsc: false,
      autoFocus : true,
      context: {
        htmlSrc: thedocument,
        thefilename : thefilename
      },
      //dialogClass: 'full-screen-dialog',
      
    }
  
  ).onClose.subscribe(
      res => {
          if(res != 'close') 
          {
            if(res.url && res.url.startsWith('https'))
            {
                  //
            }
            else
            {
              /*this.toastService.show("No se ha podido firmar el documento correctamente.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
              );*/
            }
          }
            
      }
    );
  }



saveInformeNeuroPsicologico()
{
  this.hayreport2 = false;

  this.patientsService.saveInformeNeuroPsicologico(this.thePatient).subscribe(
    res => {
      this.isProcessing = false;
      this.thePatient = res;
// Suponiendo que `res` es el objeto con las fechas en formato LocalDate

this.thePatient.ins_fecha_informe = this.localDateTime2Date(res.ins_fecha_informe);
this.thePatient.ins_fecha1 = this.localDateTime2Date(res.ins_fecha1);
this.thePatient.ins_fecha2 = this.localDateTime2Date(res.ins_fecha2);
this.thePatient.ins_fecha3 = this.localDateTime2Date(res.ins_fecha3);
this.thePatient.ins_fecha4 = this.localDateTime2Date(res.ins_fecha4);
this.thePatient.ins_fecha_mms1 = this.localDateTime2Date(res.ins_fecha_mms1);
this.thePatient.ins_fecha_mms2 = this.localDateTime2Date(res.ins_fecha_mms2);
this.thePatient.ins_fecha_mms3 = this.localDateTime2Date(res.ins_fecha_mms3);
this.thePatient.ins_fecha_mms4 = this.localDateTime2Date(res.ins_fecha_mms4);
this.thePatient.ins_fecha_ind1 = this.localDateTime2Date(res.ins_fecha_ind1);
this.thePatient.ins_fecha_ind2 = this.localDateTime2Date(res.ins_fecha_ind2);
this.thePatient.ins_fecha_ind3 = this.localDateTime2Date(res.ins_fecha_ind3);

this.hayreport = true;
this.http.get(this.thePatient.ins_url , {responseType: 'text'}).pipe(
  catchError(error => {
    this.hayreport = false;
    return of('ERROR.');
  })
)
.subscribe({
  next: response => {
    this.pdfTable.nativeElement.innerHTML = response;
    this.closeModal1();
  },
  error: error => {
    this.hayreport = false;
    console.error('Error en la suscripción:', error);
  }
});


    },
    error => {
      this.isProcessing = false;
      console.error("saveInformeNeuroPsicologico():"+JSON.stringify(error));
      this.toastService.show("No se ha podido grabar el informe neuropsicológico correctamente.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
       );
    }
  );


}





saveInformePsicoSocial()
{
  this.hayreport = false;
  this.patientsService.saveInformePsicoSocial(this.thePatient).subscribe(
    res => {
      this.isProcessing = false;
      this.thePatient = res;
// Suponiendo que `res` es el objeto con las fechas en formato LocalDate

this.thePatient.ips_fecha_informe = this.localDateTime2Date(res.ips_fecha_informe);

this.hayreport2 = true;
this.http.get(this.thePatient.ips_url , {responseType: 'text'}).pipe(
  catchError(error => {
    this.hayreport2 = false;
    return of('ERROR.');
  })
)
.subscribe({
  next: response => {
    this.pdfTable2.nativeElement.innerHTML = response;
    this.closeModal2();
  },
  error: error => {
    this.hayreport2 = false;
    console.error('Error en la suscripción:', error);
  }
});


    },
    error => {
      this.isProcessing = false;
      console.error("saveInformePsicoSocial():"+JSON.stringify(error));
      this.toastService.show("No se ha podido grabar el informe psico-social correctamente.",
        "¡Ups!", 
        { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
       );
    }
  );


}


imprimirInformeNeuro()
{
 const pdfTable = this.pdfTable.nativeElement;

    const htmlSections = pdfTable.innerHTML.split('<!--#PAGEBREAK#-->');

    let pdfMakeContent = [];
    htmlSections.forEach((section, index) => 
      {
        const cleanedSection = section.replace(/-->/g, '<!-- -->');

        const sectionContent = htmlToPdfmake(cleanedSection);

        // Agrega estilos de texto y tabla a cada sección
        sectionContent.forEach((content: any) => {
          if (content.table) {
            // Ajusta el estilo de la tabla, como tamaño de fuente
            content.style = 'tableStyle';
          }
        });

          pdfMakeContent = pdfMakeContent.concat(sectionContent);

          if (index < htmlSections.length - 1) {
            pdfMakeContent.push({ text: '', pageBreak: 'after' });
          }
        });

      const documentDefinition = {
        content: pdfMakeContent,
        styles: {
          tableStyle: {
            fontSize: 10, // Tamaño de fuente para las tablas
            margin: [0, 5, 0, 5] // Márgenes alrededor de las tablas
          }
        },
        defaultStyle: {
          fontSize: 12 // Tamaño de fuente predeterminado
        }
      };


      pdfMake.createPdf(documentDefinition).download(this.thePatient.documentid + "-informe-neuropsicologico" + ".pdf");
  }


  

  
imprimirInformePsicoSocial()
{
 const pdfTable = this.pdfTable2.nativeElement;

    const htmlSections = pdfTable.innerHTML.split('<!--#PAGEBREAK#-->');

    let pdfMakeContent = [];
    htmlSections.forEach((section, index) => 
      {
        const cleanedSection = section.replace(/-->/g, '<!-- -->');

        const sectionContent = htmlToPdfmake(cleanedSection);

        // Agrega estilos de texto y tabla a cada sección
        sectionContent.forEach((content: any) => {
          if (content.table) {
            // Ajusta el estilo de la tabla, como tamaño de fuente
            content.style = 'tableStyle';
          }
        });

          pdfMakeContent = pdfMakeContent.concat(sectionContent);

          if (index < htmlSections.length - 1) {
            pdfMakeContent.push({ text: '', pageBreak: 'after' });
          }
        });

      const documentDefinition = {
        content: pdfMakeContent,
        styles: {
          tableStyle: {
            fontSize: 10, // Tamaño de fuente para las tablas
            margin: [0, 5, 0, 5] // Márgenes alrededor de las tablas
          }
        },
        defaultStyle: {
          fontSize: 12 // Tamaño de fuente predeterminado
        }
      };


      pdfMake.createPdf(documentDefinition).download(this.thePatient.documentid + "-informe-psicosocial" + ".pdf");
  }


  copiarHtml() {
    const range = document.createRange();
    const selection = window.getSelection();

    // Seleccionar el contenido del elemento
    range.selectNodeContents(this.pdfTable.nativeElement);
    selection?.removeAllRanges(); // Limpiar cualquier selección existente
    selection?.addRange(range);

    // Usar la API de portapapeles para copiar con formato
    try {
      const success = document.execCommand('copy');
      if (success) {
        console.log('Contenido copiado con formato.');
      } else {
        console.error('Error al copiar el contenido.');
      }
    } catch (err) {
      console.error('Error al intentar copiar:', err);
    }

    // Limpiar la selección después de copiar
    selection?.removeAllRanges();
  }


  copiarHtml2() {
    const range = document.createRange();
    const selection = window.getSelection();

    // Seleccionar el contenido del elemento
    range.selectNodeContents(this.pdfTable2.nativeElement);
    selection?.removeAllRanges(); // Limpiar cualquier selección existente
    selection?.addRange(range);

    // Usar la API de portapapeles para copiar con formato
    try {
      const success = document.execCommand('copy');
      if (success) {
        console.log('Contenido copiado con formato.');
      } else {
        console.error('Error al copiar el contenido.');
      }
    } catch (err) {
      console.error('Error al intentar copiar:', err);
    }

    // Limpiar la selección después de copiar
    selection?.removeAllRanges();
  }


}
