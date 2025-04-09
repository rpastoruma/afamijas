
import { Component, ElementRef, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import {  parseDataExport, ActionDTO, CountryDTO, StateDTO, CityDTO, PatientDTO, MemberDTO } from 'src/app/shared/models/models';
import { PatientsService } from 'src/app/core/services/patients.service';
import { PdfService } from 'src/app/core/services/pdf-service.service';
import { ExcelService } from 'src/app/core/services/excel-service.service';
import { AuthService } from 'src/app/core/services/auth.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DeleteConfirmComponent } from 'src/app/shared/components/delete-confirm/delete-confirm.component';
import { Subscription, catchError, finalize, of } from 'rxjs';
import { HttpClient, HttpEventType } from '@angular/common/http';
import { MediaService } from 'src/app/core/services/media.service';
import { MyPdfViewerComponent } from 'src/app/shared/components/my-pdf-viewer/my-pdf-viewer.component';
import { FrontValuesService } from 'src/app/core/services/front-values.service';
import { isPhone } from 'spain-phone';
import * as EmailValidator from 'email-validator';
import { RelativeDTO } from 'src/app/shared/models/models';
import { PDFDocument, StandardFonts, degrees, rgb } from 'pdf-lib'
import { saveAs } from 'file-saver';
import { MyHtmlViewerComponent } from 'src/app/shared/components/my-html-viewer/my-html-viewer.component';
import { UsersService } from 'src/app/core/services/users.service';

import pdfMake from 'pdfmake/build/pdfmake';
import pdfFonts from 'pdfmake/build/vfs_fonts';
pdfMake.vfs = pdfFonts.pdfMake.vfs;
import htmlToPdfmake from 'html-to-pdfmake';

@Component({
  selector: 'app-pai-enfer-list',
  templateUrl: './pai-enfer-list.component.html',
  styleUrls: ['./pai-enfer-list.component.scss']
})
export class PaiEnferListComponent implements OnInit{

  

  @ViewChild('modalContent1', { static: true }) modalContent1: TemplateRef<any>;

    //PARÁMETROS LISTADO
    theIdpatient : string = '';


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
  
  allPatients : MemberDTO[] = [];

  loadingExcel : boolean = false;
  loadingPDF : boolean = false;
  isProcessing : boolean = true;

  exportData: any[];

  actions : ActionDTO[] = [];
   
  allRelatives : RelativeDTO[];

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
    ins_fecha_informe: undefined,
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
    pai_fisio_url: '',
    pai_psico_acude: '',
    pai_psico_sintomas: '',
    pai_psico_diagnostico: '',
    pai_psico_quien_diagnostica: '',
    pai_psico_fecha_diagnostico: undefined,
    pai_psico_forma_evalucion: '',
    pai_psico_sintomatologia_actual: '',
    pai_psico_antecedentes: '',
    pai_psico_breve_historial: '',
    pai_psico_orientacion: '',
    pai_psico_lenguaje: '',
    pai_psico_memoria: '',
    pai_psico_atencion: '',
    pai_psico_praxi: '',
    pai_psico_pensamiento_abstracto: '',
    pai_psico_percepcion: '',
    pai_psico_funcion_ejecutiva: '',
    pai_psico_escala_folstein: '',
    pai_psico_evaluacion_conductual: '',
    pai_psico_plan_act_valoracion_s1: '',
    pai_psico_plan_act_valoracion_s2: '',
    pai_psico_plan_act_instrumentos_s1: '',
    pai_psico_plan_act_instrumentos_s2: '',
    pai_psico_plan_act_objetivos_s1: '',
    pai_psico_plan_act_objetivos_s2: '',
    pai_psico_plan_act_actividades_s1: '',
    pai_psico_plan_act_actividades_s2: '',
    pai_psico_plan_act_incidencias_s1: '',
    pai_psico_plan_act_incidencias_s2: '',
    pai_psico_valoraciones: '',
    pai_psico_actuaciones: '',
    pai_psico_incidencias: '',
    pai_psico_url: '',
    pai_tocupa_nivel_independencia: '',
    pai_tocupa_plan_motriz: '',
    pai_tocupa_alimentacion: '',
    pai_tocupa_wc: '',
    pai_tocupa_aseo: '',
    pai_tocupa_deambular: '',
    pai_tocupa_transferencias: '',
    pai_tocupa_vestido: '',
    pai_tocupa_bano: '',
    pai_tocupa_escaolones: '',
    pai_tocupa_esfinteres: '',
    pai_tocupa_dinero: '',
    pai_tocupa_compras: '',
    pai_tocupa_telefono: '',
    pai_tocupa_casa: '',
    pai_tocupa_calle: '',
    pai_tocupa_medicacion: '',
    pai_tocupa_indice_barthel: '',
    pai_tocupa_escala_actividad: '',
    pai_tocupa_disfruta_tiempo: '',
    pai_tocupa_espacios_ocio: '',
    pai_tocupa_disfruta_ocio: '',
    pai_tocupa_relacion_otros: '',
    pai_tocupa_propios_objetivos: '',
    pai_tocupa_participa_actividades: '',
    pai_tocupa_actividades_iniciativa_propia: '',
    pai_tocupa_url: '',
    pai_tocupa_valoraciones: '',
    pai_tocupa_actuaciones: '',
    pai_tocupa_incidencias: '',
    pai_enfer_diagnostico: '',
    pai_enfer_problemas_audio: '',
    pai_enfer_problemas_audio_text: '',
    pai_enfer_uso_audifono: '',
    pai_enfer_problemas_vision: '',
    pai_enfer_problemas_vision_text: '',
    pai_enfer_uso_gafas: '',
    pai_enfer_tension: '',
    pai_enfer_uso_medicacion: '',
    pai_enfer_diabetes: '',
    pai_enfer_diabetes_text: '',
    pai_enfer_alergias: '',
    pai_enfer_otras_enfermedades: '',
    pai_enfer_tratamiento_medicamento_1: '',
    pai_enfer_tratamiento_medicamento_2: '',
    pai_enfer_tratamiento_medicamento_3: '',
    pai_enfer_tratamiento_medicamento_4: '',
    pai_enfer_tratamiento_dosis_1: '',
    pai_enfer_tratamiento_dosis_2: '',
    pai_enfer_tratamiento_dosis_3: '',
    pai_enfer_tratamiento_dosis_4: '',
    pai_enfer_tratamiento_fecha_1: '',
    pai_enfer_tratamiento_fecha_2: '',
    pai_enfer_tratamiento_fecha_3: '',
    pai_enfer_tratamiento_fecha_4: '',
    pai_enfer_tratamiento_para_1: '',
    pai_enfer_tratamiento_para_2: '',
    pai_enfer_tratamiento_para_3: '',
    pai_enfer_tratamiento_para_4: '',
    pai_enfer_medicacion_centro: '',
    pai_enfer_medicacion_centro_text: '',
    pai_enfer_medicacion_puntual: '',
    pai_enfer_wc_esfinteres: '',
    pai_enfer_wc_retencion: '',
    pai_enfer_wc_estrenimiento: '',
    pai_enfer_wc_acompanam: '',
    pai_enfer_alim_alergias: '',
    pai_enfer_alim_alergias_text: '',
    pai_enfer_alim_dieta: '',
    pai_enfer_alim_dieta_text: '',
    pai_enfer_alim_problemas_deglucion: '',
    pai_enfer_alim_espesantes: '',
    pai_enfer_alim_ayuda: '',
    pai_enfer_alim_observaciones: '',
    pai_enfer_valoraciones: '',
    pai_enfer_actuaciones: '',
    pai_enfer_incidencias: '',
    pai_enfer_url: '',
    pai_social_historia: '',
    pai_social_informes: '',
    pai_social_informes_text: '',
    pai_social_valoracion_disca: '',
    pai_social_valoracion_disca_fecha: '',
    pai_social_valoracion_disca_ca: '',
    pai_social_valoracion_disca_grado: '',
    pai_social_3_persona: '',
    pai_social_ayudas_tecnicas: '',
    pai_social_ayudas_tecnicas_text: '',
    pai_social_movilidad: '',
    pai_social_ley_dependencai: '',
    pai_social_grado_y_nivel: '',
    pai_social_cuidador: '',
    pai_social_relacion_cuidador: '',
    pai_social_indicadores: '',
    pai_social_apoyos: '',
    pai_social_vive: '',
    pai_social_domicilio_obstaculos: '',
    pai_social_domicilio_ayudas_tecnicas: '',
    pai_social_domicilio_confort: '',
    pai_social_domicilio_actual_quiere: '',
    pai_social_domicilio_actual_otro: '',
    pai_social_domicilio_actula_residencia: '',
    pai_social_apoyo_tipo1: '',
    pai_social_apoyo_tipo2: '',
    pai_social_apoyo_tipo3: '',
    pai_social_apoyo_tipo4: '',
    pai_social_apoyo_titularidad1: '',
    pai_social_apoyo_titularidad2: '',
    pai_social_apoyo_titularidad3: '',
    pai_social_apoyo_titularidad4: '',
    pai_social_apoyo_coste1: '',
    pai_social_apoyo_coste2: '',
    pai_social_apoyo_coste3: '',
    pai_social_apoyo_coste4: '',
    pai_social_apoyo_aportacion1: '',
    pai_social_apoyo_aportacion2: '',
    pai_social_apoyo_aportacion3: '',
    pai_social_apoyo_aportacion4: '',
    pai_social_apoyo_dom_prestacion1: '',
    pai_social_apoyo_dom_prestacion2: '',
    pai_social_apoyo_dom_prestacion3: '',
    pai_social_apoyo_dom_prestacion4: '',
    pai_social_apoyo_dom_intensidad1: '',
    pai_social_apoyo_dom_intensidad2: '',
    pai_social_apoyo_dom_intensidad3: '',
    pai_social_apoyo_dom_intensidad4: '',
    pai_social_apoyo_dom_coste1: '',
    pai_social_apoyo_dom_coste2: '',
    pai_social_apoyo_dom_coste3: '',
    pai_social_apoyo_dom_coste4: '',
    pai_social_apoyo_dom_aportacion1: '',
    pai_social_apoyo_dom_aportacion2: '',
    pai_social_apoyo_dom_aportacion3: '',
    pai_social_apoyo_dom_aportacion4: '',
    pai_social_apoyo_otras_prestacion1: '',
    pai_social_apoyo_otras_prestacion2: '',
    pai_social_apoyo_otras_prestacion3: '',
    pai_social_apoyo_otras_prestacion4: '',
    pai_social_apoyo_otras_titularidad1: '',
    pai_social_apoyo_otras_titularidad2: '',
    pai_social_apoyo_otras_titularidad3: '',
    pai_social_apoyo_otras_titularidad4: '',
    pai_social_apoyo_otras_intensidad1: '',
    pai_social_apoyo_otras_intensidad2: '',
    pai_social_apoyo_otras_intensidad3: '',
    pai_social_apoyo_otras_intensidad4: '',
    pai_social_ingresos: '',
    pai_social_ingresos_familia: '',
    pai_social_ingresos_cubre: '',
    pai_social_nivel_estudios: '',
    pai_social_relaciones: '',
    pai_social_necesidades: '',
    pai_social_objetivos: '',
    pai_social_valoraciones: '',
    pai_social_actuaciones: '',
    pai_social_incidencias: '',
    pai_social_url: '',
    pai_portada_fecha: '',
    pai_portada_representante_guardador: '',
    pai_portada_cuidador_nombre: '',
    pai_portada_cuidador_edad: '',
    pai_portada_cuidador_dni: '',
    pai_portada_cuidador_domicilio: '',
    pai_portada_cuidador_estado_civil: '',
    pai_portada_cuidador_profesion: '',
    pai_portada_cuidador_relacion: '',
    pai_portada_cuidador_convive_otros: '',
    pai_portada_nss: '',
    pai_portada_seguro_medico: '',
    pai_portada_datos_medicos_enfermedades: '',
    pai_portada_datos_medicos_grado_minusvalida: '',
    pai_portada_datos_medicos_grado_dependencia: '',
    pai_portada_profesional_1: '',
    pai_portada_categoria_1: '',
    pai_portada_profesional_2: '',
    pai_portada_categoria_2: '',
    pai_portada_profesional_3: '',
    pai_portada_categoria_3: '',
    pai_portada_profesional_4: '',
    pai_portada_categoria_4: '',
    pai_portada_profesional_5: '',
    pai_portada_categoria_5: '',
    pai_portada_url: ''
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


  

  constructor(
    public toastService: NbToastrService,
    private patientsService : PatientsService,
    private pdfService : PdfService,
    private excelService : ExcelService,
    private authService : AuthService,
    private modal: NgbModal, 
    private dialogService : NbDialogService,
    private mediaService : MediaService,
    private frontValuesService : FrontValuesService,
    private usersService : UsersService,
    private http: HttpClient

  )
   {
    }

  ngOnInit(): void {
    this.getPatients(0);
    this.actions = [   {action: 'show', text: 'Ver/modificar datos del PAI'}  ];
  }

  getPatients(page? : number)
  {
      this.isProcessing = true;
      if(page) this.page = page;
      this.patientsService.getPatients(this.page, this.size,  this.theIdpatient,  this.name_surnames, this.documentid, this.status).subscribe(
        res => {
          this.isProcessing = false;
          this.allPatients = res.content;
          this.patients = res.content.map(item => { return {id: item.id, values: [item.fullname, item.documentid, item.relativefullname, item.servicetype]  }; });
          this.patientsObjects = res.content.map(item => this.convertDates(item));
          this.totalPages = res.totalPages;

          if(this.theIdpatient && this.theIdpatient!='')
            this.getPatientById();
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
  

  
  
  getExportData(format: string) 
  {
    if(format === 'excel') this.loadingExcel = true;
    else if(format === 'pdf') this.loadingPDF = true;

    this.patientsService.getPatients(0, 100000000, this.theIdpatient, this.name_surnames, this.documentid, this.status).subscribe(
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
    if(event && event[0] === 'show') 
    {
        const selected = this.patientsObjects.find(item => item.id === event[1]);
        this.thePatient = selected;
  
        this.openPAIEnfer();
    } 
  }



  openPAIEnfer()
  {


    this.modal.open(this.modalContent1, { size: 'lg' });
  }

  


  
  savePAIEnfer()
  {
    
    this.patientsService.savePAIEnfer(this.thePatient).subscribe(
      res => {
        this.isProcessing = false;
        this.thePatient = res;
        this.thePatient.pai_psico_fecha_diagnostico = this.localDateTime2Date(res.pai_psico_fecha_diagnostico);

        this.openDocumentRegisterHTML(this.thePatient.pai_enfer_url, 'PAI-ENFERMERIA-' + this.thePatient.documentid)
      },
      error => {
        this.isProcessing = false;
        console.error("savePAIEnfer():"+JSON.stringify(error));
        this.toastService.show("No se ha podido grabar el PAI de ENFERMERÍA.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    ); 
  }


  

  goStep(step :string)
  {

    document.getElementById('step1').style.display = 'none';
    document.getElementById('step2').style.display = 'none';
    document.getElementById('step3').style.display = 'none';
    document.getElementById('step4').style.display = 'none';
    document.getElementById('step5').style.display = 'none';

    document.getElementById(step).style.display = 'block';
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
    patient.pai_psico_fecha_diagnostico = this.localDateTime2Date(patient.pai_psico_fecha_diagnostico);
    return patient;
  }


  disabledDay(date) {
    date.setDate(date.getDate() + 1);
   return date > new Date();
  }


  removeDocument(documentname : string)
  {
    this.thePatient[documentname]='';
  }

  closeModal()
  {
    this.getPatients(this.page);
  }


  
  canModify() : boolean
  {
    return this.authService.isManager() || this.authService.isAdmin();
  }


  disabledDay2(date) 
  {
     date.setDate(date.getDate());
     return date >= new Date();
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
        this.thePatient.pai_fisio_fecha_valoracion = this.localDateTime2Date(res.pai_fisio_fecha_valoracion);
        this.thePatient.pai_psico_fecha_diagnostico = this.localDateTime2Date(res.pai_psico_fecha_diagnostico);

        this.isProcessing = false;
      },
      error => 
      {
        console.error("getPatientById():"+JSON.stringify(error));
      }
    );
}

  
}
