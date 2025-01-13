import { Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import {  parseDataExport, ActionDTO, CountryDTO, StateDTO, CityDTO, PatientDTO } from 'src/app/shared/models/models';
import { PatientsService } from 'src/app/core/services/patients.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DeleteConfirmComponent } from 'src/app/shared/components/delete-confirm/delete-confirm.component';
import { Subscription, finalize } from 'rxjs';
import { HttpEventType } from '@angular/common/http';
import { MediaService } from 'src/app/core/services/media.service';
import { MyPdfViewerComponent } from 'src/app/shared/components/my-pdf-viewer/my-pdf-viewer.component';
import { FrontValuesService } from 'src/app/core/services/front-values.service';
import { isPhone } from 'spain-phone';
import * as EmailValidator from 'email-validator';
import { RelativeDTO } from 'src/app/shared/models/models';
import { PDFDocument, StandardFonts, degrees, rgb } from 'pdf-lib'
import { saveAs } from 'file-saver';
import { MyHtmlViewerComponent } from 'src/app/shared/components/my-html-viewer/my-html-viewer.component';


@Component({
  selector: 'app-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss']
})
export class UsersListComponent  implements OnInit{

  

  @ViewChild('modalContent', { static: true }) modalContent: TemplateRef<any>;

  @ViewChild('modalUnregister', { static: true }) modalUnregister: TemplateRef<any>;

  requiredFileType:string  =".pdf";
  fileName = '';
  uploadProgress:number;
  uploadSub: Subscription;



  page : number = 0;
  size : number = 5;
  totalPages : number = 0;

  name_surnames : string = '';
  documentid : string;
  status : string = 'A';

  patients: any[]  = []; // amy => formato del listado
  patientsObjects: PatientDTO[]  = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];

   
  allRelatives : RelativeDTO[];

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
    fs_fecha_inscripcion: new Date(),
    fs_num_expediente: '',
    fs_num_ss: '',
    fs_estado_civil: 'Soltero/a',
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
    fs_movilizarse: '',
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
    ins_fecha_informe: undefined,
    ins_motivo_consulta: '',
    ins_antecedentes: '',
    ins_diagnostico: '',
    ins_texto_pre_puntuaciones: '',
    ins_fecha1: undefined,
    ins_fecha2: undefined,
    ins_fecha3: undefined,
    ins_fecha4: undefined,
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
    ins_fecha_mms1: undefined,
    ins_fecha_mms2: undefined,
    ins_fecha_mms3: undefined,
    ins_fecha_mms4: undefined,
    ins_mmse1: 0,
    ins_mmse2: 0,
    ins_mmse3: 0,
    ins_mmse4: 0,
    ins_texto_post_puntuaciones: '',
    ins_fecha_ind1: undefined,
    ins_fecha_ind2: undefined,
    ins_fecha_ind3: undefined,
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
    ips_fecha_informe: undefined,
    ips_sanitarios: '',
    ips_sociofamiliar: '',
    ips_evalcognitiva: '',
    ips_evalconductual: '',
    ips_evalfuncional: '',
    ips_situacioneconomica: '',
    ips_observaciones: ''
  }

  theCountries : CountryDTO[] = [];
  theStates : StateDTO[] = [];
  theCities : CityDTO[] = [];



  getCity(id : number)
  {
    for(let i=0; i<this.theCities.length; i++)
      if(this.theCities[i].id == id) return this.theCities[i].name + ", " + this.theCities[i].state_name + " (" + this.theCities[i].country_name + ")";
  }


  getRelativeFullname(id : string)
  {
    for(let i=0; i<this.allRelatives.length; i++)
      if(this.allRelatives[i].id == id) return this.allRelatives[i].fullname;
  }

  getRelativeDocumentId(id : string)
  {
    for(let i=0; i<this.allRelatives.length; i++)
      if(this.allRelatives[i].id == id) return this.allRelatives[i].documentid;
  }


  theNewPatient : PatientDTO = { ...this.thePatient };

  constructor(
    public toastService: NbToastrService,
    private patientsService : PatientsService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private dialogService : NbDialogService,
    private mediaService : MediaService,
    private frontValuesService : FrontValuesService

  )
   {
    }

  ngOnInit(): void {
    this.getAllRelatives();
    this.getPatients(0);
    this.actions = [  {action: 'edit', text: 'Modificar datos del usuario'},  {action: 'show', text: 'Ver registro de pago'},    {action: 'delete', text: 'Dar de baja usuario'}  ];
  }

  getPatients(page? : number)
  {
      this.isProcessing = true;
      if(page) this.page = page;
      this.patientsService.getPatients(this.page, this.size, this.name_surnames, this.documentid, this.status).subscribe(
        res => {
          this.isProcessing = false;
          this.patients = res.content.map(item => { return {id: item.id, values: [item.fullname, item.documentid, item.relativefullname, item.servicetype]  }; });
          this.patientsObjects = res.content.map(item => this.convertDates(item));
          this.totalPages = res.totalPages;
        },
        error => 
        {
          this.isProcessing = false;
          console.error("getPatients():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los usuarios.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
  }
  

  getAllRelatives()
  {
      this.isProcessing = true;
      this.patientsService.getAllRelatives().subscribe(
        res => {
          this.isProcessing = false;
          this.allRelatives = res;
        },
        error => 
        {
          this.isProcessing = false;
          console.error("getAllRelatives():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los familiares.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
  }
  


  getCountries() 
  {
    this.isProcessing = true;
    this.frontValuesService.getCountries().subscribe(
      res => {
        this.isProcessing = false;
        this.theCountries = res;
        console.log("this.thePatient.idcountry="  + this.thePatient.idcountry);
        if(!this.thePatient.idcountry) this.thePatient.idcountry = 207; //ESPAÑA
        this.getStates();
      },
      error => {
        this.isProcessing = false;
        console.error("getCountries():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener los países.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }
  
  

  getStates() 
  {
    this.isProcessing = true;
    this.frontValuesService.getStates(this.thePatient.idcountry).subscribe(
      res => {
        this.isProcessing = false;
        this.theStates = res;
        if(this.theStates[0]) { if(!this.thePatient.idstate) this.thePatient.idstate = this.theStates[0].id; }
        else this.thePatient.idstate = undefined;
        this.getCities();
      },
      error => {
        this.isProcessing = false;
        console.error("getStates():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener los estados.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }
  

  getCities() 
  {
    this.isProcessing = true;
    this.frontValuesService.getCities(this.thePatient.idstate).subscribe(
      res => {
        this.isProcessing = false;
        this.theCities = res;
        if(this.theCities[0]) { if(!this.thePatient.idcity) this.thePatient.idcity = this.theCities[0].id; }
        else this.thePatient.idcity = undefined;
      },
      error => {
        this.isProcessing = false;
        console.error("getStates():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener las ciudades.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }
  
  

  getStateAndCitiesByPostalCodeAndCountryId() 
  {
    if(this.thePatient.idcountry!=207) return; //solo para españa
    this.isProcessing = true;
    this.frontValuesService.getStateAndCitiesByPostalCodeAndCountryId(this.thePatient.postalcode, this.thePatient.idcountry).subscribe(
      res => {
        this.isProcessing = false;
        if(res)
        {
          this.thePatient.idstate = res.state.id;
          this.theCities = res.cities;
          if(this.theCities[0]) this.thePatient.idcity = this.theCities[0].id;
          else this.thePatient.idcity = undefined;
        }
        else
        {
          this.thePatient.idstate = undefined;
          this.thePatient.idcity = undefined;
        }
      },
      error => {
        this.isProcessing = false;
        console.error("getStateAndCitiesByPostalCodeAndCountryId():"+JSON.stringify(error));
        /*
        this.toastService.show("No se han podido obtener las ciudades por código postal.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );*/
      }
    );
  }
  
  
  
  getExportData(format: string) 
  {
    if(format === 'excel') this.loadingExcel = true;
    else if(format === 'pdf') this.loadingPDF = true;

    this.patientsService.getPatients(0, 100000000, this.name_surnames, this.documentid, this.status).subscribe(
      res => {
        const header = {};
        const keys = ['Nombre', 'DNI', 'Email', 'Teléfono'];
        const fields = ['fullname', 'documentid', 'email', 'phone'];
        fields.forEach((key, i) => header[key] = keys[i]);
        this.exportData = res && res.content ? res.content.map(item => [item.fullname, item.documentid, item.relativefullname, item.servicetype]) : null;
        const final = parseDataExport(fields, this.exportData);
        const title = 'Listado de usuarios';

        if (format === 'excel') 
        {
          this.excelService.exportAsExcelFile(title, [header].concat(final), title, fields);
          this.loadingExcel = false;
          this.loadingPDF = false;
        } 
        else 
        {
          const minFields = ['40%','20%','20%','20%'];
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
    this.getPatients(page);
  }


  setPage(event) {
    this.page = event;
    this.getPatients(this.page);
  }

  action(event) 
  {
    if (event && event[0] === 'show') 
    {
      const selected = this.patientsObjects.find(item => item.id === event[1]);
      this.thePatient = selected;

      window.open('/invoices?idpatient=' + this.thePatient.id);
    } 
    else if (event && event[0] === 'edit') 
      {
        const selected = this.patientsObjects.find(item => item.id === event[1]);
        this.thePatient = selected;
  
        this.openSavePatientModal(false);
      } 
    else if (event && event[0] === 'delete') 
    {
      const selected = this.patientsObjects.find(item => item.id === event[1]);
      this.thePatient = selected;

      this.openUnregisterPatientModal();
    }
  }



  openSavePatientModal(isnew : boolean)
  {
    if(isnew) this.thePatient = {...this.theNewPatient};

    this.getCountries();


    this.modal.open(this.modalContent, { size: 'lg' });
  }

  
  openUnregisterPatientModal()
  {
    this.modal.open(this.modalUnregister, { size: 'lg' });
  } 



  
  savePatient(pending? : boolean)
  {
    
    this.patientsService.savePatient(this.thePatient).subscribe(
      res => {
        this.isProcessing = false;
        if(pending && pending==true)
        {
          this.thePatient = res;
          this.thePatient.birthdate = this.localDateTime2Date(res.birthdate);
          this.thePatient.fs_fecha_inscripcion = this.localDateTime2Date(res.fs_fecha_inscripcion);
        }
        else
        {
          this.getPatients(this.page);
          this.modal.dismissAll();
        }
      },
      error => {
        this.isProcessing = false;
        console.error("savePatient():"+JSON.stringify(error));
        this.toastService.show("No se ha podido grabar el usuario correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    ); 
  }





  

  unregisterPatient()
  {
    
    this.patientsService.unregisterPatient(this.thePatient.id, this.thePatient.unregister_reason, this.thePatient.unregister_document_url, this.thePatient.unregister_document_url_signed && this.thePatient.unregister_document_url_signed!='').subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Usuario dado de baja correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getPatients(this.page);
        this.modal.dismissAll();
      },
      error => {
        this.isProcessing = false;
        console.error("modifyFood():"+JSON.stringify(error));
        this.toastService.show("No se ha podido dar de baja el usuario correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    ); 
  }


  goStep(step :string)
  {
    if(step == 'step4') 
    { 
      if(!(this.thePatient.register_document_url_signed && this.thePatient.register_document_url_signed.startsWith("https:")))
      {
        this.thePatient.register_document_url = '';   
        this.createRegisterDocument(this.thePatient); 
      }
    }

    document.getElementById('step1').style.display = 'none';
    document.getElementById('step2').style.display = 'none';
    document.getElementById('step3').style.display = 'none';
    document.getElementById('step4').style.display = 'none';

    document.getElementById(step).style.display = 'block';
  }


  async createRegisterDocument(thePatient : PatientDTO)
  {
    const pdfDoc = await PDFDocument.create()
    const timesRomanFont = await pdfDoc.embedFont(StandardFonts.TimesRoman)
  
    const page = pdfDoc.addPage()
    const { width, height } = page.getSize()
    let fontSize = 30;

    // Fetch PNG image
    const pngUrl = '/assets/logo.png';
    const pngImageBytes = await fetch(pngUrl).then((res) => res.arrayBuffer());
      
    const pngImage = await pdfDoc.embedPng(pngImageBytes);
    const pngDims = pngImage.scale(0.9);

    page.drawImage(pngImage, {
      x: 50,
      y: height - 100,
      width: pngDims.width,
      height: pngDims.height,
    });
    
    page.drawText('Documento de alta de usuario', {
      x: 50,
      y: height - 150,
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0.53, 0.71),
    })

    fontSize = 12;
    page.drawText('Nombre del usuario: ' + this.thePatient.name + " " + this.thePatient.surname1 + " " + this.thePatient.surname2, {
      x: 50,
      y: height - 180,
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0 ,0),
    })

    page.drawText('DNI: ' + this.thePatient.documentid, {
      x: 50,
      y: height - 200,
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0 ,0),
    })

    page.drawText('Fecha de nacimiento: ' + this.formatDate2(this.thePatient.birthdate), {
      x: 50,
      y: height - 220,
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0 ,0),
    })

    page.drawText('Dirección: ' + this.thePatient.postaladdress + " - " + this.thePatient.postalcode + " - " + this.getCity(this.thePatient.idcity), {
      x: 50,
      y: height - 240,
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0 ,0),
    })

    page.drawText('Nombre del cuidador principal: ' + this.getRelativeFullname(this.thePatient.idrelative), {
      x: 50,
      y: height - 260,
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0 ,0),
    })

    page.drawText('DNI del cuidador principal: ' + this.getRelativeDocumentId(this.thePatient.idrelative), {
      x: 50,
      y: height - 280,
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0 ,0),
    })

    page.drawText('Relación con el usuario: ' + this.thePatient.relativerelation, {
      x: 50,
      y: height - 300,
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0 ,0),
    })

    page.drawText('Tipo de servicio: ' + this.thePatient.servicetype, {
      x: 50,
      y: height - 320,
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0 ,0),
    })

    let offset = 20;
    if(this.thePatient.tallerpsico)
    {
      page.drawText('Servicio de psicoestimulación a domicilio: SELECCIONADO', {
        x: 50,
        y: height - (320 + offset),
        size: fontSize,
        font: timesRomanFont,
        color: rgb(0, 0 ,0),
      })
      offset += 20;
    }

    if(this.thePatient.transportservice)
    {
      page.drawText('Servicio de transporte: ' + this.thePatient.transportservice_text, {
        x: 50,
        y: height - (320 + offset),
        size: fontSize,
        font: timesRomanFont,
        color: rgb(0, 0 ,0),
      })
      offset += 20;
    }
  
    if(this.thePatient.comedorservice)
    {
      page.drawText('Servicio de comedor: ' + this.thePatient.comedorservice_text, {
        x: 50,
        y: height - (320 + offset),
        size: fontSize,
        font: timesRomanFont,
        color: rgb(0, 0 ,0),
      })
      offset += 20;
    }
  
    if(this.thePatient.ayudadomicilioservice)
    {
      page.drawText('Servicio de ayuda a domicilio: ' + this.thePatient.ayudadomicilioservice_text, {
        x: 50,
        y: height - (320 + offset),
        size: fontSize,
        font: timesRomanFont,
        color: rgb(0, 0 ,0),
      })
      offset += 20;
    }
    
    if(this.thePatient.duchaservice)
    {
      page.drawText('Servicio de ducha: ' + this.thePatient.duchaservice_text, {
        x: 50,
        y: height - (320 + offset),
        size: fontSize,
        font: timesRomanFont,
        color: rgb(0, 0 ,0),
      })
      offset += 20;
    }

        
    page.drawText('Firmado: ' + this.getRelativeFullname(this.thePatient.idrelative) + ", en Mijas, a " + this.formatDate2(new Date()), {
      x: 200,
      y: height - (320 + offset + 200),
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0 ,0),
    })
    offset += 200;
  

    page.drawText('Con la firma de este documento, la persona arriba mencionada se encuentra de ALTA como usuario/a\ndel centro gestionado por la Asociación de familiares de enfermos de Alzheimer y otras demencias de Mijas,\ny reconoce haber sido informado de la normativa del centro, así como del os precios establecidos para cada\nuno de los servicios.', {
      x: 50,
      y: height - (320 + offset + 40),
      size: fontSize,
      font: timesRomanFont,
      color: rgb(0, 0 ,0),
    })
    offset += 20;
      
        
      
    const pdfBytes = await pdfDoc.save();

    // Trigger the browser to download the PDF document
    const file = new Blob([pdfBytes], {type: 'application/pdf'});

    
    this.mediaService.uploadFile("register", new File([file], "documento_de_alta.pdf")).subscribe(
      res => {
          /*this.toastService.show("Documento firmado correctamente",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );*/
          saveAs(file, "documento_de_alta_" + thePatient.documentid + ".pdf");
          thePatient.register_document_url = res.url;
          this.savePatient(true);
      },
      error => {
        if(error.status && error.status == 200)
        {
          /*this.toastService.show("Documento firmado correctamente",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );*/
          saveAs(file, "documento_de_alta_" + thePatient.documentid + ".pdf");
          this.savePatient(true);
        }
        else
        {
          console.error("createRegisterDocument():"+JSON.stringify(error));
          this.toastService.show("No se ha podido generar el fichero de alta.",
            "¡Ups!", 
            { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
           );
        }
      }
    );
  }


  async onFileRegisterSignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register_signed", file).pipe(finalize(() => this.reset())
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
                this.thePatient.register_document_url_signed = event.url;
                /*
                if(this.thePatient.servicetype == 'CENTRO_DIA_CONCERTADO')
                  this.createRegister19Document(this.thePatient);
                else if(this.thePatient.servicetype == 'CENTRO_DIA_PRIVADO')
                  this.createRegister21Document(this.thePatient);
                else if(this.thePatient.servicetype == 'TALLER')
                  this.createRegister25Document(this.thePatient);*/
            }
            else
              console.error("onFileRegisterSignedSelected1():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegisterSignedSelected2():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register_document_url_signed = event.url;
              /*
              if(this.thePatient.servicetype == 'CENTRO_DIA_CONCERTADO')
                this.createRegister19Document(this.thePatient);
              else if(this.thePatient.servicetype == 'CENTRO_DIA_PRIVADO')
                this.createRegister21Document(this.thePatient);
              else if(this.thePatient.servicetype == 'TALLER')
                this.createRegister25Document(this.thePatient);
              */
          }
            else
              console.error("onFileRegisterSignedSelected3():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  





  async onFileRegister19SignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register19_signed", file).pipe(finalize(() => this.reset())
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
                this.thePatient.register19_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister19SignedSelected():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegister19SignedSelected():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register19_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister19SignedSelected():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  

/*


  async onFileRegister20SignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register20_signed", file).pipe(finalize(() => this.reset())
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
                this.thePatient.register20_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister20SignedSelected():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegister20SignedSelected():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register20_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister20SignedSelected():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  
*/


  async onFileRegister24SignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register24_signed", file).pipe(finalize(() => this.reset())
    )
    ;
  
  
    this.uploadSub = upload$.subscribe(event => {
          if (event.type == HttpEventType.UploadProgress) 
          {
            this.uploadProgress = Math.round(100 * (event.loaded / event.total));
          }
          else if(event.type == HttpEventType.Response && event.status == 240 )
          {
            if(event.url && event.url.startsWith("https://") )
            {
                this.thePatient.register24_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister24SignedSelected():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegister24SignedSelected():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register24_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister24SignedSelected():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  




  async onFileRegister25SignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register25_signed", file).pipe(finalize(() => this.reset())
    )
    ;
  
  
    this.uploadSub = upload$.subscribe(event => {
          if (event.type == HttpEventType.UploadProgress) 
          {
            this.uploadProgress = Math.round(100 * (event.loaded / event.total));
          }
          else if(event.type == HttpEventType.Response && event.status == 250 )
          {
            if(event.url && event.url.startsWith("https://") )
            {
                this.thePatient.register25_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister25SignedSelected():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegister25SignedSelected():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register25_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister25SignedSelected():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  


  

  async onFileRegister26SignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register26_signed", file).pipe(finalize(() => this.reset())
    )
    ;
  
  
    this.uploadSub = upload$.subscribe(event => {
          if (event.type == HttpEventType.UploadProgress) 
          {
            this.uploadProgress = Math.round(100 * (event.loaded / event.total));
          }
          else if(event.type == HttpEventType.Response && event.status == 260 )
          {
            if(event.url && event.url.startsWith("https://") )
            {
                this.thePatient.register26_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister26SignedSelected():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegister26SignedSelected():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register26_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister26SignedSelected():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  


  

  async onFileRegister27SignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register27_signed", file).pipe(finalize(() => this.reset())
    )
    ;
  
  
    this.uploadSub = upload$.subscribe(event => {
          if (event.type == HttpEventType.UploadProgress) 
          {
            this.uploadProgress = Math.round(100 * (event.loaded / event.total));
          }
          else if(event.type == HttpEventType.Response && event.status == 270 )
          {
            if(event.url && event.url.startsWith("https://") )
            {
                this.thePatient.register27_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister27SignedSelected():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegister27SignedSelected():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register27_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister27SignedSelected():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  


  


  

  async onFileRegister21SignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register21_signed", file).pipe(finalize(() => this.reset())
    )
    ;
  
  
    this.uploadSub = upload$.subscribe(event => {
          if (event.type == HttpEventType.UploadProgress) 
          {
            this.uploadProgress = Math.round(100 * (event.loaded / event.total));
          }
          else if(event.type == HttpEventType.Response && event.status == 210 )
          {
            if(event.url && event.url.startsWith("https://") )
            {
                this.thePatient.register21_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister21SignedSelected():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegister21SignedSelected():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register21_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister21SignedSelected():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  




  /*
  

  async onFileRegister22SignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register22_signed", file).pipe(finalize(() => this.reset())
    )
    ;
  
  
    this.uploadSub = upload$.subscribe(event => {
          if (event.type == HttpEventType.UploadProgress) 
          {
            this.uploadProgress = Math.round(100 * (event.loaded / event.total));
          }
          else if(event.type == HttpEventType.Response && event.status == 220 )
          {
            if(event.url && event.url.startsWith("https://") )
            {
                this.thePatient.register22_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister22SignedSelected():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegister22SignedSelected():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register22_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister22SignedSelected():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  */



  async onFileRegister23SignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register23_signed", file).pipe(finalize(() => this.reset())
    )
    ;
  
  
    this.uploadSub = upload$.subscribe(event => {
          if (event.type == HttpEventType.UploadProgress) 
          {
            this.uploadProgress = Math.round(100 * (event.loaded / event.total));
          }
          else if(event.type == HttpEventType.Response && event.status == 230 )
          {
            if(event.url && event.url.startsWith("https://") )
            {
                this.thePatient.register23_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister23SignedSelected():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegister23SignedSelected():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register23_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister23SignedSelected():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  


  

  async onFileRegister28SignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register28_signed", file).pipe(finalize(() => this.reset())
    )
    ;
  
  
    this.uploadSub = upload$.subscribe(event => {
          if (event.type == HttpEventType.UploadProgress) 
          {
            this.uploadProgress = Math.round(100 * (event.loaded / event.total));
          }
          else if(event.type == HttpEventType.Response && event.status == 280 )
          {
            if(event.url && event.url.startsWith("https://") )
            {
                this.thePatient.register28_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister28SignedSelected():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegister28SignedSelected():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register28_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister28SignedSelected():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  


  

  

  async onFileRegister29SignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register29_signed", file).pipe(finalize(() => this.reset())
    )
    ;
  
  
    this.uploadSub = upload$.subscribe(event => {
          if (event.type == HttpEventType.UploadProgress) 
          {
            this.uploadProgress = Math.round(100 * (event.loaded / event.total));
          }
          else if(event.type == HttpEventType.Response && event.status == 290 )
          {
            if(event.url && event.url.startsWith("https://") )
            {
                this.thePatient.register29_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister29SignedSelected():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegister29SignedSelected():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register29_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister29SignedSelected():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  

  /*


  async onFileRegister30SignedSelected(event) {

    const file:File = event.target.files[0];
  
    if (file) 
    {
      
      const upload$ = this.mediaService.uploadFile("register30_signed", file).pipe(finalize(() => this.reset())
    )
    ;
  
  
    this.uploadSub = upload$.subscribe(event => {
          if (event.type == HttpEventType.UploadProgress) 
          {
            this.uploadProgress = Math.round(100 * (event.loaded / event.total));
          }
          else if(event.type == HttpEventType.Response && event.status == 300 )
          {
            if(event.url && event.url.startsWith("https://") )
            {
                this.thePatient.register30_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister30SignedSelected():"+JSON.stringify(event));
          }
          else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
          {
            console.error("onFileRegister30SignedSelected():"+JSON.stringify(event));
            this.toastService.show("No se ha podido subir el fichero.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
            );
          }
          else 
          {
            if(event.url && event.url.startsWith("https://") ) 
            {
              this.thePatient.register30_document_url_signed = event.url;
            }
            else
              console.error("onFileRegister30SignedSelected():"+JSON.stringify(event));
          }
        }
      )
    }
  }
  

*/




async onFileUnRegister(event) {

  const file:File = event.target.files[0];

  if (file) 
  {
    
    const upload$ = this.mediaService.uploadFile("unregister", file).pipe(finalize(() => this.reset())
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
              this.thePatient.register19_document_url_signed = event.url;
          }
          else
            console.error("onFileUnRegister():"+JSON.stringify(event));
        }
        else if(event.type == HttpEventType.Response && (event.status == 403 || event.status == 500) ) //mod_evasive
        {
          console.error("onFileUnRegister():"+JSON.stringify(event));
          this.toastService.show("No se ha podido subir el fichero.",
            "¡Ups!", 
            { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        }
        else 
        {
          if(event.url && event.url.startsWith("https://") ) 
          {
            this.thePatient.register19_document_url_signed = event.url;
          }
          else
            console.error("onFileUnRegister():"+JSON.stringify(event));
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


  getFullName()
  {
    return (this.thePatient.name.trim() + " " + this.thePatient.surname1.trim() + " " + this.thePatient.surname2.trim()).trim();
  }



  openDocumentRegister()
  {
    this.dialogService.open(MyPdfViewerComponent, {
      hasScroll: true,
      closeOnBackdropClick: false,
      closeOnEsc: false,
      context: {
        pdfSrc: this.thePatient.register_document_url,
        fullname : this.getFullName(),
        forSigning : true,
        openExternal : true
      }
    }).onClose.subscribe(
      res => {
          if(res != 'close') 
          {
            if(res.url && res.url.startsWith('https'))
              this.signDocumentRegister(this.thePatient.id, res.url);
            else
            {
              this.toastService.show("No se ha podido firmar el documento correctamente.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
              );
            }
          }
            
      }
    );
  }



  signDocumentRegister(idpatient: string, register_document_url_signed : string)
  {
    this.patientsService.signDocumentRegister(idpatient, register_document_url_signed).subscribe(
      res => {
        this.isProcessing = false;
        this.thePatient.register_document_url_signed = register_document_url_signed;
        this.toastService.show("Documento firmado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getPatients(0);
      },
      error => {
        this.isProcessing = false;
        console.error("signDocumentRegister():"+JSON.stringify(error));
        this.toastService.show("No se ha podido firmar el documento correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }



  

  openDocumentUnRegister()
  {
    this.dialogService.open(MyPdfViewerComponent, {
      hasScroll: true,
      closeOnBackdropClick: false,
      closeOnEsc: false,
      context: {
        pdfSrc: this.thePatient.unregister_document_url,
        fullname : this.getFullName(),
        forSigning : true,
        openExternal : true
      }
    }).onClose.subscribe(
      res => {
          if(res != 'close') 
          {
            if(res.url && res.url.startsWith('https'))
              this.signDocumentUnRegister(this.thePatient.id, res.url);
            else
            {
              this.toastService.show("No se ha podido firmar el documento correctamente.",
              "¡Ups!", 
              { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
              );
            }
          }
            
      }
    );
  }



  signDocumentUnRegister(idpatient: string, unregister_document_url_signed : string)
  {
    this.patientsService.signDocumentUnRegister(idpatient, unregister_document_url_signed).subscribe(
      res => {
        this.isProcessing = false;
        this.thePatient.unregister_document_url_signed = unregister_document_url_signed;
        this.toastService.show("Documento firmado correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getPatients(0);
      },
      error => {
        this.isProcessing = false;
        console.error("signDocumentUnRegister():"+JSON.stringify(error));
        this.toastService.show("No se ha podido firmar el documento correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }



  

  openDocumentRegisterHTML(thedocument : string, thefilename : string)
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



  validEmail(email :string)
  {
    return EmailValidator.validate(email);
  }
  
  validPhone(phone : string)
  {
    return isPhone(phone);
  }


  validDocumentId(documentId: string, documentType: string)
  {
    documentId = documentId.toUpperCase();
    this.thePatient.documentid = documentId;

    const dni_letters = "TRWAGMYFPDXBNJZSQVHLCKE";
    if(documentType == 'DNI')
    {
      let letter = dni_letters.charAt( parseInt( documentId, 10 ) % 23 );
      if(letter == documentId.charAt(8) && documentId.length==9) return true;
      else return false;
    }
    else if(documentType == 'NIE')
    {
      if(this.validDocumentId(documentId, 'DNI')) return false;

      let nie_prefix = documentId.charAt( 0 );
      switch (nie_prefix)
      {
          case 'X': nie_prefix = '0'; break;
          case 'Y': nie_prefix = '1'; break;
          case 'Z': nie_prefix = '2'; break;
      }
      const dni = nie_prefix + documentId.substring(1);
      let letter = dni_letters.charAt( parseInt( dni, 10 ) % 23 );
  
      if(letter == dni.charAt(8)  && documentId.length==9) return true;
      else return false;
    }
    else
      return true;
  }

  validRelative(thePatient : PatientDTO)
  {
    return thePatient.idrelative && thePatient.relativerelation.trim()!='';
  }

  validPostalAddress(thePatient : PatientDTO)
  {
    return thePatient.postalcode && thePatient.postalcode.length>0 && thePatient.postaladdress  && thePatient.postaladdress.length>0 && thePatient.idstate && thePatient.idcity && thePatient.phone && thePatient.phone.trim()!=''
  }
  

  validService(thePatient : PatientDTO)
  {
    if(thePatient.servicetype == 'TALLER' || thePatient.servicetype == 'CENTRO_DIA_PRIVADO' || thePatient.servicetype == 'CENTRO_DIA_CONCERTADO') return true;
    else return false;
  }

  /*
  validDoc(thePatient : PatientDTO)
  {
    if(thePatient.servicetype == 'CENTRO_DIA_CONCERTADO')
      return (thePatient.register_document_url_signed && thePatient.register_document_url_signed.startsWith("https://") )  &&
              (thePatient.register19_document_url_signed && thePatient.register19_document_url_signed.startsWith("https://") )  &&
              (thePatient.register20_document_url_signed && thePatient.register20_document_url_signed.startsWith("https://") )  &&
              (thePatient.register24_document_url_signed && thePatient.register24_document_url_signed.startsWith("https://") )  &&
              (thePatient.register25_document_url_signed && thePatient.register25_document_url_signed.startsWith("https://") )  &&
              (thePatient.register26_document_url_signed && thePatient.register26_document_url_signed.startsWith("https://") )  &&
              (thePatient.register27_document_url_signed && thePatient.register27_document_url_signed.startsWith("https://") )  

      else if(thePatient.servicetype == 'CENTRO_DIA_PRIVADO')
        return (thePatient.register_document_url_signed && thePatient.register_document_url_signed.startsWith("https://") )  &&
                (thePatient.register21_document_url_signed && thePatient.register21_document_url_signed.startsWith("https://") )  &&
                (thePatient.register22_document_url_signed && thePatient.register22_document_url_signed.startsWith("https://") )  &&
                (thePatient.register23_document_url_signed && thePatient.register23_document_url_signed.startsWith("https://") )  &&
                (thePatient.register28_document_url_signed && thePatient.register28_document_url_signed.startsWith("https://") )  
    
      else if(thePatient.servicetype == 'TALLER')
        return (thePatient.register_document_url_signed && thePatient.register_document_url_signed.startsWith("https://") )  &&
                (thePatient.register25_document_url_signed && thePatient.register25_document_url_signed.startsWith("https://") )  &&
                (thePatient.register26_document_url_signed && thePatient.register26_document_url_signed.startsWith("https://") )  &&
                (thePatient.register28_document_url_signed && thePatient.register28_document_url_signed.startsWith("https://") )  &&
                ((thePatient.register29_document_url_signed && thePatient.register29_document_url_signed.startsWith("https://") )  ||
                (thePatient.register30_document_url_signed && thePatient.register30_document_url_signed.startsWith("https://") )  )
              

  }

*/

validDoc(thePatient : PatientDTO)
{
  if(thePatient.servicetype == 'CENTRO_DIA_PRIVADO')
      return (thePatient.register_document_url_signed && thePatient.register_document_url_signed.startsWith("https://") )  &&
            (thePatient.register21_document_url_signed && thePatient.register21_document_url_signed.startsWith("https://") )  &&
            //(thePatient.register22_document_url_signed && thePatient.register22_document_url_signed.startsWith("https://") )  &&
            (thePatient.register23_document_url_signed && thePatient.register23_document_url_signed.startsWith("https://") )  &&
            (thePatient.register28_document_url_signed && thePatient.register28_document_url_signed.startsWith("https://") )  
    else if(thePatient.servicetype == 'CENTRO_DIA_CONCERTADO')
      return (thePatient.register_document_url_signed && thePatient.register_document_url_signed.startsWith("https://") )  &&
             (thePatient.register19_document_url_signed && thePatient.register19_document_url_signed.startsWith("https://") )  &&
             //(thePatient.register20_document_url_signed && thePatient.register20_document_url_signed.startsWith("https://") )  &&
             (thePatient.register24_document_url_signed && thePatient.register24_document_url_signed.startsWith("https://") )  &&
             (thePatient.register25_document_url_signed && thePatient.register25_document_url_signed.startsWith("https://") )  &&
             (thePatient.register26_document_url_signed && thePatient.register26_document_url_signed.startsWith("https://") )  &&
             (thePatient.register27_document_url_signed && thePatient.register27_document_url_signed.startsWith("https://") )  
    else
      return (thePatient.register_document_url_signed && thePatient.register_document_url_signed.startsWith("https://") )  ;
}



  formatDate2(thedate : Date)
  {
    return (this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + thedate.getFullYear());
  }

  completeZeros(x : number) : string
  {
    if(x<=9) return "0" + x;
    else return ""+x;
  }


  
  

  //[2024,3,1,13,0,...] --> Date
  localDateTime2Date(thedate : number[]) : Date
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
    return new Date(Date.parse(sdate));
  }

  /*
  date2Text(thelocaldatetime : number[])
  {
    let thedate : Date = this.localDateTime2Date(thelocaldatetime);
    if(!thedate) return '';
    let daysOfWeek = [ 'domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado' ];
    const dayOfWeek = daysOfWeek[thedate.getDay()];
    return 'El ' + daysOfWeek[thedate.getDay()] + " " + this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"") + " a las " +  this.completeZeros(thedate.getHours()) + ":" + this.completeZeros(thedate.getMinutes()) + " h.";
  }*/




  convertDates(patient: any)
  {
    patient.birthdate = this.localDateTime2Date(patient.birthdate);
    patient.fs_fecha_inscripcion = this.localDateTime2Date(patient.fs_fecha_inscripcion);
    return patient;
  }


  disabledDay(date: Date): boolean {
    // Crear una fecha que sea 20 años antes de la fecha actual
    const twentyYearsAgo = new Date();
    twentyYearsAgo.setFullYear(twentyYearsAgo.getFullYear() - 20);
  
    // Ajustar la fecha pasada por parámetro sumándole un día
    const modifiedDate = new Date(date);
    modifiedDate.setDate(modifiedDate.getDate() + 1);
  
    // Comparar si la fecha modificada es menor que hace 20 años
    return modifiedDate > twentyYearsAgo;
  }
  

  disabledDay2(date) {
    date.setDate(date.getDate());
   return date >= new Date();
  }

  removeDocument(documentname : string)
  {
    this.thePatient[documentname]='';
    console.log('this.thePatient.register19_document_url_signed='+this.thePatient.register19_document_url_signed);
  }

}
