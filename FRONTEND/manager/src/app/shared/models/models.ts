import { CalendarEvent } from "angular-calendar";

// Respuesta del login:
export interface LoginResponse {
    token?: string;
    roles?: string[];
    userId?: string;
    username?: string;
    documentid?: string;
    fullname?: string;
    photo_url?: string;
    passworChanged? : boolean;
  
    requires2FA?: boolean;
    otpAuthUrl?: string;
    message?: string;

    
  }
  

export interface ActionDTO {
    action : string;
    text : string;
}

export interface UserDTO {
    id : string;
    fullname : string;
    roles : string[]
    passwordChanged : boolean
}

export interface PatientDTO {
    id : string;
    username : string;
    email : string;
    name : string;
    surname1 : string;
    surname2 : string;
    documentid : string;
    documenttype : string;
    phone : string;
    postaladdress : string;
    idcity : number;
    cityname : string;
    idstate : number;
    statename : string;
    idcountry : number;
    countryname : string;
    postalcode : string;
    signature : string;
    photo_url : string;
    gender : string;

    idrelative : string;
    relativefullname : string;
    is_principal_keeper : string;
    principal_keeper_fullname : string;
    principal_keeper_phone : string;


    routeDTO : RouteDTO;

    menu_type : string;
    breakfast_description : string;
    medication_description_morning : string;
    medication_description_evening : string;
    groupcode : string;

    birthdate : Date;
    relativerelation : string;

    servicetype : string;
    tallerpsico : boolean;

    transportservice : boolean;
    transportservice_text : string;

    comedorservice : boolean;
    comedorservice_text : string;

    ayudadomicilioservice : boolean;
    ayudadomicilioservice_text : string;

    duchaservice : boolean;
    duchaservice_text : string;

    //ALTA:

    register_document_url : string;
    register_document_url_signed : string;

    //PLAZA CONCERTADA:

    register19_document_url : string;
    register19_document_url_signed : string;

    register20_document_url : string;
    register20_document_url_signed : string;

    register24_document_url : string;
    register24_document_url_signed : string;

    register25_document_url : string;
    register25_document_url_signed : string;

    register26_document_url : string;
    register26_document_url_signed : string;

    register27_document_url : string;
    register27_document_url_signed : string;

    //PLAZA PRIVADA:

    register21_document_url : string;
    register21_document_url_signed : string;

    register22_document_url : string;
    register22_document_url_signed : string;

    register23_document_url : string;
    register23_document_url_signed : string;

    register28_document_url : string;
    register28_document_url_signed : string;

    //TALLER ESTIMULACIÓN:

    //register25_document_url : string;
    //register25_document_url_signed : string;

    //register26_document_url : string;
    //register26_document_url_signed : string;

    //register28_document_url : string;
    //register28_document_url_signed : string;

    register29_document_url : string;
    register29_document_url_signed : string;

    register30_document_url : string;
    register30_document_url_signed : string;

    //BAJA:

    unregister_document_url : string;
    unregister_document_url_signed : string;

    unregister_reason : string;

    status : string;

    fullname: string;

    language : string;

    num_contrato: string;

    //FICHA SOCIAL
    fs_url : string;

    fs_fecha_inscripcion : Date;

    fs_num_expediente : string;

    fs_num_ss : string;

    fs_estado_civil : string;

    fs_talleres_estimulacion : boolean;


    fs_gradior_stimmulus : boolean;

    fs_sad : boolean;

    fs_other : boolean;

    fs_other_text : string;

    fs_comer_solo : boolean;

    fs_lavarse_solo : boolean;

    fs_salir_sin_perderse : boolean;

    fs_reconocer_caras : boolean;

    fs_leer_y_escribir : boolean;

    fs_incontenencia_urinaria : boolean;

    fs_conversar : boolean;

    fs_reconocer_objetos_cotidianos : boolean;

    fs_sufrir_alucinaciones : boolean;

    fs_fases_agitacion : boolean;

    fs_dificultad_orientarse : boolean;

    fs_movilizarse : string;

    fs_datos_medicos : string;

    fs_grado_minusvalia : boolean;

    fs_grado_minusvalia_text : string;

    fs_grado_dependencia : boolean;

    fs_grado_dependencia_text : string;

    fs_incapacitacion_judicial : boolean;

    fs_ayudas_externas : boolean;

    fs_ayudas_externas_text : string;



    //HISTORIA SOCIAL
    hs_url : string;

    hs_beca: boolean;

    hs_diagnostico: string;
   
    hs_autonomia: boolean;
   
    hs_ayuda_abd: boolean;
   
    hs_uc_solo: boolean;
   
    hs_uc_conyuge: boolean;
   
    hs_uc_hijos: boolean;
   
    hs_uc_other: boolean;
   
    hs_uc_other_text: string;
   
    hs_nivel_formativo: string;
   
    hs_interaccion_demas: string;
   
    hs_interaccion_profesioneales: string;
   
    hs_participacion_actividades: string;
   
    hs_integracion_dinamica: string;
   
    hs_grado_minusvalia_tipo: string;
   
    hs_grado_minusvalia_cuando: string;
   
    hs_ley_dependencia_solicitada: boolean;
   
    hs_ley_dependencia_grado: string;
   
    hs_recibe_servicio_administracion: boolean;
   
    hs_patologias: string;
   
    hs_diabetico: boolean;
   
    hs_hipertenso: boolean;
   
    hs_alimenta_bien: boolean;
   
    hs_duerme_bien: boolean;
   
    hs_fuma_bebe: boolean;
   
    hs_drogas: boolean;
   
    hs_drogas_text: string;
   
    hs_valoracion_salud: string;
   
    hs_fam_dificultades_convivencia: boolean;
   
    hs_fam_dificultades_economicas: boolean;
   
    hs_fam_dificultad_cuidados: boolean;
   
    hs_fam_sin_apoyo: boolean;
   
    hs_fam_agotamiento_cuidador: boolean;
   
    hs_viv_sin_domicilio: boolean;
   
    hs_viv_ruinas: boolean;
   
    hs_viv_barreras: boolean;
   
    hs_viv_inhabitabilidad: boolean;
   
    hs_alquiler_elevado: boolean;
   
    hs_escaleras_exteriores: boolean;
   
    hs_escaleras_interiores: boolean;
   
    hs_banera: boolean;
   
    hs_alfombras: boolean;
   
    hs_otros: boolean;
   
    hs_otros_text: string;
   
    hs_nombre1: string;
    hs_parentesco1: string;
    hs_edad1: number;
    hs_profesion1: string;
   
    hs_nombre2: string;
    hs_parentesco2: string;
    hs_edad2: number;
    hs_profesion2: string;
   
    hs_nombre3: string;
    hs_parentesco3: string;
    hs_edad3: number;
    hs_profesion3: string;
   
    hs_nombre4: string;
    hs_parentesco4: string;
    hs_edad4: number;
    hs_profesion4: string;
   
    hs_tiene_pareja: boolean;
   
    hs_relacion_pareja: string;
   
    hs_tiene_hijos: boolean;
   
    hs_relacion_hijos: string;
   
    hs_tiene_hermanos: boolean;
   
    hs_relacion_hermanos: string;
   
    hs_visitas_familiares: boolean;
   
    hs_visitas_cuanto: string;
   
    hs_apoyo_amigos: boolean;
   
    hs_relacion_familia: boolean;
   
    hs_acude_otras: boolean;
   
    hs_recibe_pension: boolean;
   
    hs_cuantia_pension: number;
   
    hs_otra_prestacion: string;
   
    hs_otros_ingresos: string;
   
    hs_otros_recursos: string;
   
    hs_valoracion_profesional: string;
   
    hs_observaciones: string;
   

    //INFORME SOCIAL
    is_url : string;
    is_tiempo_conoce_usuario: string;
    is_servicios_prestados: string;
    is_como_adaptado: string;
    is_acudio_voluntad_propia: boolean;
    is_quien_influyo_decision: string;
    is_que_actividades: string;
    is_como_relaciona: string;
    is_como_pasa_dia: string;

    is_problemas_psico: boolean;
    is_problemas_psico_text: string;

    is_recibe_tratamiento: boolean;
    is_recibe_tratamiento_text: string;

    is_familia_estru: boolean;
    is_familia_estru_text: string;

    is_recibe_ingresos_actividad_laboral: boolean;
    is_esta_buscando_empleo: boolean;

    is_vive_en: string;
    is_cubiertas_necesidades_diarias: boolean;

    is_valoracion_profesional: string;
    is_propuesta: string;


    //INFORME NEUROPSICOLOGICO
    ins_url : string;

    ins_fecha_informe: Date;

    ins_motivo_consulta: string;
  
    ins_antecedentes: string;
  
    ins_diagnostico: string;
  
    ins_texto_pre_puntuaciones: string;
  
    ins_fecha1: Date;
    ins_fecha2: Date;
    ins_fecha3: Date;
    ins_fecha4: Date;
  
    ins_orientacion1: number;
    ins_orientacion2: number;
    ins_orientacion3: number;
    ins_orientacion4: number;
  
    ins_lenguaje1: number;
    ins_lenguaje2: number;
    ins_lenguaje3: number;
    ins_lenguaje4: number;
  
    ins_memoria1: number;
    ins_memoria2: number;
    ins_memoria3: number;
    ins_memoria4: number;
  
    ins_atencalculo1: number;
    ins_atencalculo2: number;
    ins_atencalculo3: number;
    ins_atencalculo4: number;
  
    ins_praxis1: number;
    ins_praxis2: number;
    ins_praxis3: number;
    ins_praxis4: number;
  
    ins_pensabstracto1: number;
    ins_pensabstracto2: number;
    ins_pensabstracto3: number;
    ins_pensabstracto4: number;
  
    ins_percecpcion1: number;
    ins_percecpcion2: number;
    ins_percecpcion3: number;
    ins_percecpcion4: number;
  
    ins_total1: number;
    ins_total2: number;
    ins_total3: number;
    ins_total4: number;
  
    ins_fecha_mms1: Date;
    ins_fecha_mms2: Date;
    ins_fecha_mms3: Date;
    ins_fecha_mms4: Date;
  
    ins_mmse1: number;
    ins_mmse2: number;
    ins_mmse3: number;
    ins_mmse4: number;
  
    ins_texto_post_puntuaciones: string;
  
    ins_fecha_ind1: Date;
    ins_fecha_ind2: Date;
    ins_fecha_ind3: Date;
  
    ins_indbathel1: number;
    ins_indbathel2: number;
    ins_indbathel3: number;
  
    ins_indlawton1: number;
    ins_indlawton2: number;
    ins_indlawton3: number;
  
    ins_texto_eval_conductual: string;
  
    ins_texto_conclusion: string;    

    ips_url: string; 
    ips_fecha_informe: Date;
    ips_sanitarios: string; 
    ips_sociofamiliar: string; 
    ips_evalcognitiva: string; 
    ips_evalconductual: string; 
    ips_evalfuncional: string; 
    ips_situacioneconomica: string; 
    ips_observaciones: string; 


    //PAI FISIO:

    pai_fisio_fecha_valoracion: Date;
    pai_fisio_prob_salud: string;
    pai_fisio_dolres: string;
    pai_fisio_duerme: string;
    pai_fisio_nec_aliment: string;
    pai_fisio_hab_saludables: string;
    pai_fisio_atencion_preven: string;
    pai_fisio_acceso_atencion: string;
    pai_fisio_medicacion_requerida: string;
    pai_fisio_alergias: string;
    pai_fisio_upp: string;
    pai_fisio_autonomo: string;
    pai_fisio_ayudas_tecnicas: string;
    pai_fisio_movilidad_mmss: string;
    pai_fisio_movilidad_mmii: string;
    pai_fisio_movilidad_cuello: string;
    pai_fisio_movilida_tronco: string;
    pai_fisio_equilibrio: string;
    pai_fisio_bipedestacion: string;
    pai_fisio_marcha: string;
    pai_fisio_riesgo_caidas: string;
    pai_fisio_deformidades: string;
    pai_fisio_disfruta_ocio: string;
    pai_fisio_espacios_ocio: string;
    pai_fisio_relaciones_entorno: string;
    pai_fisio_objetivos: string;
    pai_fisio_tratamiento: string;
    pai_fisio_valoraciones: string;
    pai_fisio_actuaciones: string;
    pai_fisio_incidencias: string;
    pai_fisio_url : string;


    //PAI PSICOLOGÍA:

    pai_psico_acude: string;
    pai_psico_sintomas: string;
    pai_psico_diagnostico: string;
    pai_psico_quien_diagnostica: string;
    pai_psico_fecha_diagnostico: Date; 
    pai_psico_forma_evalucion: string;
    pai_psico_sintomatologia_actual: string;
    pai_psico_antecedentes: string;
    pai_psico_breve_historial: string;
    pai_psico_orientacion: string;
    pai_psico_lenguaje: string;
    pai_psico_memoria: string;
    pai_psico_atencion: string;
    pai_psico_praxi: string;
    pai_psico_pensamiento_abstracto: string;
    pai_psico_percepcion: string;
    pai_psico_funcion_ejecutiva: string;
    pai_psico_escala_folstein: string;
    pai_psico_evaluacion_conductual: string;
    pai_psico_plan_act_valoracion_s1: string;
    pai_psico_plan_act_valoracion_s2: string;
    pai_psico_plan_act_instrumentos_s1: string;
    pai_psico_plan_act_instrumentos_s2: string;
    pai_psico_plan_act_objetivos_s1: string;
    pai_psico_plan_act_objetivos_s2: string;
    pai_psico_plan_act_actividades_s1: string;
    pai_psico_plan_act_actividades_s2: string;
    pai_psico_plan_act_incidencias_s1: string;
    pai_psico_plan_act_incidencias_s2: string;
    pai_psico_valoraciones: string; // CUADRO RESUMEN
    pai_psico_actuaciones: string;  // CUADRO RESUMEN
    pai_psico_incidencias: string;  // CUADRO RESUMEN
    pai_psico_url: string;


    //PAI TOCUPA
    pai_tocupa_nivel_independencia: string;
    pai_tocupa_plan_motriz: string;
    pai_tocupa_alimentacion: string;
    pai_tocupa_wc: string;
    pai_tocupa_aseo: string;
    pai_tocupa_deambular: string;
    pai_tocupa_transferencias: string;
    pai_tocupa_vestido: string;
    pai_tocupa_bano: string;
    pai_tocupa_escaolones: string;
    pai_tocupa_esfinteres: string;
    pai_tocupa_dinero: string;
    pai_tocupa_compras: string;
    pai_tocupa_telefono: string;
    pai_tocupa_casa: string;
    pai_tocupa_calle: string;
    pai_tocupa_medicacion: string;
    pai_tocupa_indice_barthel: string;
    pai_tocupa_escala_actividad: string;
    pai_tocupa_disfruta_tiempo: string;
    pai_tocupa_espacios_ocio: string;
    pai_tocupa_disfruta_ocio: string;
    pai_tocupa_relacion_otros: string;
    pai_tocupa_propios_objetivos: string;
    pai_tocupa_participa_actividades: string;
    pai_tocupa_actividades_iniciativa_propia: string;
    pai_tocupa_valoraciones : string;
    pai_tocupa_actuaciones : string;
    pai_tocupa_incidencias : string;
    pai_tocupa_url: string;

    //PAI ENFER
    pai_enfer_diagnostico: string;
    pai_enfer_problemas_audio: string;
    pai_enfer_problemas_audio_text: string;
    pai_enfer_uso_audifono: string;
    pai_enfer_problemas_vision: string;
    pai_enfer_problemas_vision_text: string;
    pai_enfer_uso_gafas: string;
    pai_enfer_tension: string;
    pai_enfer_uso_medicacion: string;
    pai_enfer_diabetes: string;
    pai_enfer_diabetes_text: string;
    pai_enfer_alergias: string;
    pai_enfer_otras_enfermedades: string;
    
    pai_enfer_tratamiento_medicamento_1: string;
    pai_enfer_tratamiento_medicamento_2: string;
    pai_enfer_tratamiento_medicamento_3: string;
    pai_enfer_tratamiento_medicamento_4: string;
  
    pai_enfer_tratamiento_dosis_1: string;
    pai_enfer_tratamiento_dosis_2: string;
    pai_enfer_tratamiento_dosis_3: string;
    pai_enfer_tratamiento_dosis_4: string;
  
    pai_enfer_tratamiento_fecha_1: string;
    pai_enfer_tratamiento_fecha_2: string;
    pai_enfer_tratamiento_fecha_3: string;
    pai_enfer_tratamiento_fecha_4: string;
  
    pai_enfer_tratamiento_para_1: string;
    pai_enfer_tratamiento_para_2: string;
    pai_enfer_tratamiento_para_3: string;
    pai_enfer_tratamiento_para_4: string;
  
    pai_enfer_medicacion_centro: string;
    pai_enfer_medicacion_centro_text: string;
    pai_enfer_medicacion_puntual: string;
  
    pai_enfer_wc_esfinteres: string;
    pai_enfer_wc_retencion: string;
    pai_enfer_wc_estrenimiento: string;
    pai_enfer_wc_acompanam: string;
  
    pai_enfer_alim_alergias: string;
    pai_enfer_alim_alergias_text: string;
    pai_enfer_alim_dieta: string;
    pai_enfer_alim_dieta_text: string;
    pai_enfer_alim_problemas_deglucion: string;
    pai_enfer_alim_espesantes: string;
    pai_enfer_alim_ayuda: string;
    pai_enfer_alim_observaciones: string;
  
    pai_enfer_valoraciones: string;
    pai_enfer_actuaciones: string;
    pai_enfer_incidencias: string;
  
    pai_enfer_url: string;


    //PAI SOCIAL

    pai_social_historia: string;
  pai_social_informes: string;
  pai_social_informes_text: string;
  pai_social_valoracion_disca: string;
  pai_social_valoracion_disca_fecha: string;
  pai_social_valoracion_disca_ca: string;
  pai_social_valoracion_disca_grado: string;
  pai_social_3_persona: string;
  pai_social_ayudas_tecnicas: string;
  pai_social_ayudas_tecnicas_text: string;
  pai_social_movilidad: string;
  pai_social_ley_dependencai: string;
  pai_social_grado_y_nivel: string;
  pai_social_cuidador: string;
  pai_social_relacion_cuidador: string;
  pai_social_indicadores: string;
  pai_social_apoyos: string;
  pai_social_vive: string;
  pai_social_domicilio_obstaculos: string;
  pai_social_domicilio_ayudas_tecnicas: string;
  pai_social_domicilio_confort: string;
  pai_social_domicilio_actual_quiere: string;
  pai_social_domicilio_actual_otro: string;
  pai_social_domicilio_actula_residencia: string;
  pai_social_apoyo_tipo1: string;
  pai_social_apoyo_tipo2: string;
  pai_social_apoyo_tipo3: string;
  pai_social_apoyo_tipo4: string;
  pai_social_apoyo_titularidad1: string;
  pai_social_apoyo_titularidad2: string;
  pai_social_apoyo_titularidad3: string;
  pai_social_apoyo_titularidad4: string;
  pai_social_apoyo_coste1: string;
  pai_social_apoyo_coste2: string;
  pai_social_apoyo_coste3: string;
  pai_social_apoyo_coste4: string;
  pai_social_apoyo_aportacion1: string;
  pai_social_apoyo_aportacion2: string;
  pai_social_apoyo_aportacion3: string;
  pai_social_apoyo_aportacion4: string;
  pai_social_apoyo_dom_prestacion1: string;
  pai_social_apoyo_dom_prestacion2: string;
  pai_social_apoyo_dom_prestacion3: string;
  pai_social_apoyo_dom_prestacion4: string;
  pai_social_apoyo_dom_intensidad1: string;
  pai_social_apoyo_dom_intensidad2: string;
  pai_social_apoyo_dom_intensidad3: string;
  pai_social_apoyo_dom_intensidad4: string;
  pai_social_apoyo_dom_coste1: string;
  pai_social_apoyo_dom_coste2: string;
  pai_social_apoyo_dom_coste3: string;
  pai_social_apoyo_dom_coste4: string;
  pai_social_apoyo_dom_aportacion1: string;
  pai_social_apoyo_dom_aportacion2: string;
  pai_social_apoyo_dom_aportacion3: string;
  pai_social_apoyo_dom_aportacion4: string;
  pai_social_apoyo_otras_prestacion1: string;
  pai_social_apoyo_otras_prestacion2: string;
  pai_social_apoyo_otras_prestacion3: string;
  pai_social_apoyo_otras_prestacion4: string;
  pai_social_apoyo_otras_titularidad1: string;
  pai_social_apoyo_otras_titularidad2: string;
  pai_social_apoyo_otras_titularidad3: string;
  pai_social_apoyo_otras_titularidad4: string;
  pai_social_apoyo_otras_intensidad1: string;
  pai_social_apoyo_otras_intensidad2: string;
  pai_social_apoyo_otras_intensidad3: string;
  pai_social_apoyo_otras_intensidad4: string;
  pai_social_ingresos: string;
  pai_social_ingresos_familia: string;
  pai_social_ingresos_cubre: string;
  pai_social_nivel_estudios: string;
  pai_social_relaciones: string;
  pai_social_necesidades: string;
  pai_social_objetivos: string;
  pai_social_valoraciones: string;
  pai_social_actuaciones: string;
  pai_social_incidencias: string;
  pai_social_url: string;


  pai_portada_fecha: string;
  pai_portada_representante_guardador: string;
  pai_portada_cuidador_nombre: string;
  pai_portada_cuidador_edad: string;
  pai_portada_cuidador_dni: string;
  pai_portada_cuidador_domicilio: string;
  pai_portada_cuidador_estado_civil: string;
  pai_portada_cuidador_profesion: string;
  pai_portada_cuidador_relacion: string;
  pai_portada_cuidador_convive_otros: string;
  pai_portada_nss: string;
  pai_portada_seguro_medico: string;
  pai_portada_datos_medicos_enfermedades: string;
  pai_portada_datos_medicos_grado_minusvalida: string;
  pai_portada_datos_medicos_grado_dependencia: string;
  pai_portada_profesional_1: string;
  pai_portada_categoria_1: string;
  pai_portada_profesional_2: string;
  pai_portada_categoria_2: string;
  pai_portada_profesional_3: string;
  pai_portada_categoria_3: string;
  pai_portada_profesional_4: string;
  pai_portada_categoria_4: string;
  pai_portada_profesional_5: string;
  pai_portada_categoria_5: string;
  pai_portada_url: string;
  
}


export interface RelativeDTO {
    id : string;
    username : string;
    email : string;
    name : string;
    surname1 : string;
    surname2 : string;
    documentid : string;
    fullname : string;

}



export interface MemberDTO {
    id : string;
    membernumber : string;
    email : string;
    name : string;
    surname1 : string;
    surname2 : string;
    fullname : string;
    documentid : string;
    documenttype : string;
    phone : string;
    postaladdress : string;
    idcity : number;
    cityname : string;
    idstate : number;
    statename : string;
    idcountry : number;
    countryname : string;
    postalcode : string;

    fee_euros : number;
    fee_period : string;
    fee_payment : string;

    bank_name : string;
    bank_account_holder_fullname : string;
    bank_account_holder_dni : string;
    bank_account_iban : string;

    register_document_url : string;
    register_document_url_signed : string;

    unregister_document_url : string;
    unregister_document_url_signed : string;

    unregister_reason : string;

    status : string
}

export interface WorkerDTO {
    id: string;
    email: string;
    password: string;
    name: string;
    surname1: string;
    surname2: string;
    fullname: string;
    documentid: string;
    documenttype: string;
    phone: string;
    postaladdress: string;
    idcity: number;
    cityname: string;
    idstate: number;
    statename: string;
    idcountry: number;
    countryname: string;
    postalcode: string;
    roles: string[];
    status: string;
    nss: string;
    categoria_profesional: string;
    tipo_contrato: string;
    jornada_laboral : string;
    horario : string;
    events? : CalendarEvent[];
  }

  
export interface RouteDTO {
    idroute : string;
    route_name : string;
    idpatient : string;
    patient_fullname : string;
    routestops : RouteStopDTO[];
    idroutestop_selected_today : string;
    idroutestop_selected_tomorrow : string;
    routestop_especial_from : string;
    routestop_especial_to : string;
}


export interface RouteStopDTO {
    idroutestop : string;
    order : number;
    name : string;
    hour : string;
    postaladdress : string;
    idcity : string;
    idstate : string;
    postalcode : string;
    lat : number;
    lon : number;
}



export interface RelativeAbsenceDTO
{
    id : string;

    idpatient : string;

    idrelative  : string;
    
    patient_fullname  : string

    transport  : string;

    comment  : string;

    allday : boolean;

    from : Date;

    to : Date;
}

export interface WorkerAbsenceDTO 
{
    id: string;

    idpatient: string;

    patient_fullname: string;

    idworker: string;

    worker_fullname: string;

    idroutestop: string;

    routestop_name: string;

    comment: string;

    when: Date;  
}





export interface MenuDTO
{
    id : string;

    type : string;

    description  : string;
    
    menu_url  : string;

    from : Date;

    to : Date;
}




export interface PermissionDTO
{
    idpermission : string;

    idpatient : string;

    patient_fullname  : string;
    
    patient_dni  : string

    idrelative : string;

    relative_fullname : string;

    relative_dni : string;

    type : string;

    comment : string;

    permission_url : string;

    permission_signed_url : string;

}



export interface MedicationDTO
{
    idpatient : string;

    patient_fullname : string;

    medication_description_morning : string;

    medication_description_evening  : string;
    
}


export interface FoodDTO
{
    idpatient : string;

    patient_fullname : string;

    menu_type : string;

    breakfast_description  : string;
    
}



export interface FeedingDTO
{
    id : string;

    idpatient : string;

    patient_fullname : string;

    idworker : string;

    worker_fullname : string;

    day : Date;

    daymeal : string; // DESAYUNO, COMIDA

    dish : string;  //PRIMERO, SEGUNDO, POSTRE

    result : string; // COMPLETO, PARCIAL, NADA

    indications : string;

    incidences : string;
    
}



export interface TempFridgeDTO
{
    id : string;

    idworker : string;

    worker_fullname : string;

    day : Date;

    temperature : number; 

    isOk : boolean;  
    
}



export interface TempServiceDTO
{
    id : string;

    idworker : string;

    worker_fullname : string;

    day : Date;

    dish : string;

    dish_type: string; // FRÍO, CALIENTE
    
    temperature_reception: number;
    
    temperature_service: number;

    isOk : boolean;  
    
}

export interface MealSampleDTO
{
    id : string;

    idworker : string;

    worker_fullname : string;

    day : Date;

    dish : string;

    orgenolepticoOk: boolean; 
    
    cuerposExtraOk: boolean;
    
    comments: string;


    
}



export interface LegionellaLogDTO
{
    id : string;

    idworker : string;

    worker_fullname : string;

    day : Date;

    point : string;

    value: number; 
    
    temperature: number;
    
    isOk: boolean;

    
}




export interface WCLogDTO
{
    id : string;

    idworker : string;

    worker_fullname : string;

    day : Date;

    point : string;

    hour: string; 
    
    
}

export interface HealthLogDTO
{
    id : string;

    idpatient : string;

    patient_fullname : string;

    idworker : string;

    worker_fullname : string;

    day : Date;

    low_pressure : number;

    high_pressure : number;

    hour_presure: string;

    sugar : number;

    hour_sugar: string;

    
}


export interface DocDTO
{
    id : string;

    idworker : string;

    worker_fullname : string;

    title : string

    description : string;

    url : string;

    dayfrom : Date;

    dayto : Date;

    roles : string[];

    created : Date;
}

export interface ProjectDTO {
    id: string;
    idworker: string;
    worker_fullname: string;
    nombre: string;
    fecha_presentacion: Date; 
    fecha_resolucion: Date;  
    plazo_ejecucion: string;
    subvencion_concedida: boolean;
    importe_solicitado: number;
    importe_concedido: number;
    documentos: DocDTO[];
}


export interface DocPsicoDTO
{
    id : string;

    idworker : string;

    worker_fullname : string;

    idpatient : string;

    patient_fullname : string;

    type : string

    description : string;

    url : string;

    created : Date;
}






export interface ReceiptDTO
{
    id : string;

    idmember : string;

    member_fullname : string;

    total : number

    url : string;

    duedate : Date;

    paiddate : Date;

    status : string;
}


export interface NominaDTO
{
    id : string;

    idworker : string;

    worker_fullname : string;

    url : string;

    duedate : Date;

    status : string;
}


export interface InvoiceDTO
{
    id : string;

    idpatient : string;

    patient_fullname : string;

    total : number

    url : string;

    duedate : Date;

    paiddate : Date;

    status : string;
}



export interface AtencionDTO
{
    id : string;

    idworker : string;

    workerfullname : string;

    number : string

    datedone : Date;

    clientfullname : string;

    sex : string;

    nationality : string;

    relationship : string;

    why : string;

    via : string;

    professional : string;

    observations : string;
}


//TODO: Revisar si se está haciendo bien ya que es copiada de proyectos antiguos
//Parseo como fecha: 
export function reviver(key: string, value: any): any {
    if ((key.startsWith('date') || key.indexOf('Date') > -1) && typeof value === 'string') {
        return new Date(value);
    }
    return value;
}

// Gestión de roles:
type RolesT = {
    [key: string]: string
}
/*

export const RoleTranslate: RolesT = {
    ROOT: 'Super Administrador',
    RELATIVE: 'Familiar',
    WORKER : 'Trabajador/a',
    ADMIN :'Administrador/a',
    SOCIALWORKER : 'Trabajador/a social',
    PSYCHO : 'Psicólogo/a'
} */

export enum RoleCode 
{
    ROOT = 'ROOT',
    RELATIVE = 'RELATIVE',
    TRANSPORT = 'TRANSPORT',
    ADMIN = 'ADMIN',
    CLEANING = 'CLEANING',
    NURSING = 'NURSING',
    NURSING_ASSISTANT = 'NURSING_ASSISTANT',
    LEGIONELLA_CONTROL = 'LEGIONELLA_CONTROL',
    KITCHEN = 'KITCHEN',
    MONITOR = 'MONITOR',
    SOCIAL_WORKER = 'SOCIAL_WORKER',
    PSYCHOLOGIST = 'PSYCHOLOGIST',
    MANAGER = 'MANAGER',
    PHYSIOTHERAPIST = 'PHYSIOTHERAPIST',
    OCCUPATIONAL_THERAPIST = 'OCCUPATIONAL_THERAPIST',
    OPERATOR_EXTRA_1 = 'OPERATOR_EXTRA_1'
}


export function rolName(theRole)  
{
    const roleNames = new Map();
    //roleNames.set("ROOT", "SuperAdmin");
    roleNames.set("RELATIVE", "Familiar");
    roleNames.set("WORKER", "Todos los trabajadores");
    roleNames.set("TRANSPORT", "Transporte");
    roleNames.set("ADMIN", "Administración");
    roleNames.set("CLEANING", "Limpieza");
    roleNames.set("NURSING", "Enfermería");
    roleNames.set("NURSING_ASSISTANT", "Auxiliar de enfermería");
    roleNames.set("LEGIONELLA_CONTROL", "Control de Legionella");
    roleNames.set("KITCHEN", "Cocina");
    roleNames.set("MONITOR", "Monitor/a");
    roleNames.set("SOCIAL_WORKER", "Trabajo social");
    roleNames.set("PSYCHOLOGIST", "Psicología");
    roleNames.set("MANAGER", "Dirección");
    roleNames.set("PHYSIOTHERAPIST", "Fisioterapia");
    roleNames.set("OCCUPATIONAL_THERAPIST", "Terapia ocupacional");
    roleNames.set("OPERATOR_EXTRA_1", "Operaciones extra (1)");

    return roleNames.get(theRole);
}




export function hasRole(userRoles: string[], roleCompare: RoleCode) {
    return userRoles.includes(roleCompare);
}




export function parseDataExport(fields: any[], exportData: any[]) {
    const final = [];
    if (exportData) {
        for (const value of exportData) {
        const res = {};
        fields.forEach((key, i) => res[key] = value[i]);
        final.push(res);
        }
    }

    return final;
}



export interface CountryDTO {
    id : number;
    name : string;
    iso2 : string;
    emoji : string;
}



export interface StateDTO {
    id : number;
    name : string;
    country_code : string;
}


export interface CityDTO {
    id : number;
    name : string;
    state_code : string;
    country_code : string;
    state_name : string;
    country_name : string;
}



export interface AddressBookDTO {
    id: string;
    type: string;           // relative, user, volunteer, worker, member, other
    iduser?: string;
    fullname: string;
    phone?: string;
    email?: string;
    observations?: string;
  }
  

  /*

  export interface Pageable {
    pageNumber: number;
    pageSize: number;
    sort: {
        empty: boolean;
        sorted: boolean;
        unsorted: boolean;
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
}

export interface PaginationResult<T> {
    content: T[];
    pageable: Pageable;
    last: boolean;
    totalPages: number;
    totalElements: number;
    size: number;
    number: number;
    sort: {
        empty: boolean;
        sorted: boolean;
        unsorted: boolean;
    };
    first: boolean;
    numberOfElements: number;
    empty: boolean;
}
    */

