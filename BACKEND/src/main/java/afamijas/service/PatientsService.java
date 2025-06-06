package afamijas.service;

import afamijas.model.dto.MemberDTO;
import afamijas.model.dto.PatientDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
public interface PatientsService
{

    Page<PatientDTO> getPatients(String idpatient, String name_surnames, String documentid, String status,
                                 String gender, String servicetype, Boolean transportservice, Boolean tallerpsico,
                                 Boolean comedorservice, Boolean ayudadomicilioservice, Boolean hs_ley_dependencia_solicitada,
                                 Integer page, Integer size, String order, String orderasc);

    PatientDTO savePatient(String id, String name, String surname1, String surname2, LocalDate birthdate, String gender, String documentid, String documenttype,
                           String idrelative, String relativerelation,
                           String postaladdress, Integer idcity, Integer idstate, Integer idcountry, String postalcode,
                           String num_contrato, String fs_num_expediente, LocalDate fs_fecha_inscripcion, String fs_num_ss, String fs_estado_civil, String phone,
                           String servicetype, Boolean tallerpsico, Boolean transportservice, String transportservice_text, Boolean comedorservice, String comedorservice_text, Boolean ayudadomicilioservice, String ayudadomicilioservice_text, Boolean duchaservice, String duchaservice_text,
                           String register_document_url, String register_document_url_signed,
                           String register19_document_url, String register19_document_url_signed,
                           String register20_document_url, String register20_document_url_signed,
                           String register21_document_url, String register21_document_url_signed,
                           String register22_document_url, String register22_document_url_signed,
                           String register23_document_url, String register23_document_url_signed,
                           String register24_document_url, String register24_document_url_signed,
                           String register25_document_url, String register25_document_url_signed,
                           String register26_document_url, String register26_document_url_signed,
                           String register27_document_url, String register27_document_url_signed,
                           String register28_document_url, String register28_document_url_signed,
                           String register29_document_url, String register29_document_url_signed,
                           String register30_document_url, String register30_document_url_signed) throws Exception;


    PatientDTO savePatientFichaSocial(String id,

                                  Boolean transportservice,


                                 Boolean fs_talleres_estimulacion,

                                 Boolean fs_gradior_stimmulus,

                                 Boolean fs_sad,

                                 Boolean fs_other,

                                 String fs_other_text,

                                 Boolean fs_comer_solo,

                                 Boolean fs_lavarse_solo,

                                 Boolean fs_salir_sin_perderse,

                                 Boolean fs_reconocer_caras,

                                 Boolean fs_leer_y_escribir,

                                 Boolean fs_incontenencia_urinaria,

                                 Boolean fs_conversar,

                                 Boolean fs_reconocer_objetos_cotidianos,

                                 Boolean fs_sufrir_alucinaciones,

                                 Boolean fs_fases_agitacion,

                                 Boolean fs_dificultad_orientarse,

                                 String fs_movilizarse,

                                 String fs_datos_medicos,

                                 Boolean fs_grado_minusvalia,

                                 String fs_grado_minusvalia_text,

                                 Boolean fs_grado_dependencia,

                                 String fs_grado_dependencia_text,

                                 Boolean fs_incapacitacion_judicial,

                                 Boolean fs_ayudas_externas,

                                 String fs_ayudas_externas_text

    ) throws Exception;


    PatientDTO savePatientHistoriaSocial(
            String id,
            Boolean hs_beca,
            String hs_diagnostico,
            Boolean hs_autonomia,
            Boolean hs_ayuda_abd,
            Boolean hs_uc_solo,
            Boolean hs_uc_conyuge,
            Boolean hs_uc_hijos,
            Boolean hs_uc_other,
            String hs_uc_other_text,
            String hs_nivel_formativo,
            String hs_interaccion_demas,
            String hs_interaccion_profesioneales,
            String hs_participacion_actividades,
            String hs_integracion_dinamica,
            String hs_grado_minusvalia_tipo,
            String hs_grado_minusvalia_cuando,
            Boolean hs_ley_dependencia_solicitada,
            String hs_ley_dependencia_grado,
            Boolean hs_recibe_servicio_administracion,
            String hs_patologias,
            Boolean hs_diabetico,
            Boolean hs_hipertenso,
            Boolean hs_alimenta_bien,
            Boolean hs_duerme_bien,
            Boolean hs_fuma_bebe,
            Boolean hs_drogas,
            String hs_drogas_text,
            String hs_valoracion_salud,
            Boolean hs_fam_dificultades_convivencia,
            Boolean hs_fam_dificultades_economicas,
            Boolean hs_fam_dificultad_cuidados,
            Boolean hs_fam_sin_apoyo,
            Boolean hs_fam_agotamiento_cuidador,
            Boolean hs_viv_sin_domicilio,
            Boolean hs_viv_ruinas,
            Boolean hs_viv_barreras,
            Boolean hs_viv_inhabitabilidad,
            Boolean hs_alquiler_elevado,
            Boolean hs_escaleras_exteriores,
            Boolean hs_escaleras_interiores,
            Boolean hs_banera,
            Boolean hs_alfombras,
            Boolean hs_otros,
            String hs_otros_text,
            String hs_nombre1,
            String hs_parentesco1,
            Integer hs_edad1,
            String hs_profesion1,
            String hs_nombre2,
            String hs_parentesco2,
            Integer hs_edad2,
            String hs_profesion2,
            String hs_nombre3,
            String hs_parentesco3,
            Integer hs_edad3,
            String hs_profesion3,
            String hs_nombre4,
            String hs_parentesco4,
            Integer hs_edad4,
            String hs_profesion4,
            Boolean hs_tiene_pareja,
            String hs_relacion_pareja,
            Boolean hs_tiene_hijos,
            String hs_relacion_hijos,
            Boolean hs_tiene_hermanos,
            String hs_relacion_hermanos,
            Boolean hs_visitas_familiares,
            String hs_visitas_cuanto,
            Boolean hs_apoyo_amigos,
            Boolean hs_relacion_familia,
            Boolean hs_acude_otras,
            Boolean hs_recibe_pension,
            Double hs_cuantia_pension,
            String hs_otra_prestacion,
            String hs_otros_ingresos,
            String hs_otros_recursos,
            String hs_valoracion_profesional,
            String hs_observaciones
    ) throws Exception;


    PatientDTO savePatientInformeSocial(
            String id,
            String is_tiempo_conoce_usuario,
            String is_servicios_prestados,
            String is_como_adaptado,
            Boolean is_acudio_voluntad_propia,
            String is_quien_influyo_decision,
            String is_que_actividades,
            String is_como_relaciona,
            String is_como_pasa_dia,
            Boolean is_problemas_psico,
            String is_problemas_psico_text,
            Boolean is_recibe_tratamiento,
            String is_recibe_tratamiento_text,
            Boolean is_familia_estru,
            String is_familia_estru_text,
            Boolean is_recibe_ingresos_actividad_laboral,
            Boolean is_esta_buscando_empleo,
            String is_vive_en,
            Boolean is_cubiertas_necesidades_diarias,
            String is_valoracion_profesional,
            String is_propuesta
    ) throws Exception;


    void unregisterPatient(String id, String unregister_reason, String unregister_document_url, boolean is_document_signed);

    String uploadRegisterDocument(String id, MultipartFile file) throws Exception;

    String uploadUnRegisterDocument(String id, MultipartFile file) throws Exception;


    void signDocumentRegister(String idpatient, String registerDocumentUrlSigned);

    void signDocumentUnRegister(String idpatient, String unregisterDocumentUrlSigned);

    PatientDTO saveInformeNeuroPsicologico(String id,
                                           LocalDate ins_fecha_informe,
                                           String ins_motivo_consulta,
                                           String ins_antecedentes,
                                           String ins_diagnostico,
                                           String ins_texto_pre_puntuaciones,
                                           LocalDate ins_fecha1, LocalDate ins_fecha2, LocalDate ins_fecha3, LocalDate ins_fecha4,
                                           Double ins_orientacion1, Double ins_orientacion2, Double ins_orientacion3, Double ins_orientacion4,
                                           Double ins_lenguaje1, Double ins_lenguaje2, Double ins_lenguaje3, Double ins_lenguaje4,
                                           Double ins_memoria1, Double ins_memoria2, Double ins_memoria3, Double ins_memoria4,
                                           Double ins_atencalculo1, Double ins_atencalculo2, Double ins_atencalculo3, Double ins_atencalculo4,
                                           Double ins_praxis1, Double ins_praxis2, Double ins_praxis3, Double ins_praxis4,
                                           Double ins_pensabstracto1, Double ins_pensabstracto2, Double ins_pensabstracto3, Double ins_pensabstracto4,
                                           Double ins_percepcion1, Double ins_percepcion2, Double ins_percepcion3, Double ins_percepcion4,
                                           Double ins_total1, Double ins_total2, Double ins_total3, Double ins_total4,
                                           LocalDate ins_fecha_mms1, LocalDate ins_fecha_mms2, LocalDate ins_fecha_mms3, LocalDate ins_fecha_mms4,
                                           Double ins_mmse1, Double ins_mmse2, Double ins_mmse3, Double ins_mmse4,
                                           String ins_texto_post_puntuaciones,
                                           LocalDate ins_fecha_ind1, LocalDate ins_fecha_ind2, LocalDate ins_fecha_ind3,
                                           Double ins_indbathel1, Double ins_indbathel2, Double ins_indbathel3,
                                           Double ins_indlawton1, Double ins_indlawton2, Double ins_indlawton3,
                                           String ins_texto_eval_conductual,
                                           String ins_texto_conclusion
                                           ) throws Exception;

    PatientDTO saveInformePsicoSocial(String id, LocalDate ipsFechaInforme, String ipsSanitarios, String ipsSociofamiliar, String ipsEvalcognitiva, String ipsEvalconductual, String ipsEvalfuncional, String ipsSituacioneconomica, String ipsObservaciones) throws Exception;

    public PatientDTO savePAIFisio(
            String id,
            LocalDate paiFisioFechaValoracion,
            String paiFisioProbSalud,
            String paiFisioDolores,
            String paiFisioDuerme,
            String paiFisioNecAliment,
            String paiFisioHabSaludables,
            String paiFisioAtencionPreven,
            String paiFisioAccesoAtencion,
            String paiFisioMedicacionRequerida,
            String paiFisioAlergias,
            String paiFisioUpp,
            String paiFisioAutonomo,
            String paiFisioAyudasTecnicas,
            String paiFisioMovilidadMmss,
            String paiFisioMovilidadMmii,
            String paiFisioMovilidadCuello,
            String paiFisioMovilidaTronco,
            String paiFisioEquilibrio,
            String paiFisioBipedestacion,
            String paiFisioMarcha,
            String paiFisioRiesgoCaidas,
            String paiFisioDeformidades,
            String paiFisioDisfrutaOcio,
            String paiFisioEspaciosOcio,
            String paiFisioRelacionesEntorno,
            String paiFisioObjetivos,
            String paiFisioTratamiento,
            String paiFisioValoraciones,
            String paiFisioActuaciones,
            String paiFisioIncidencias
    ) throws Exception;

    PatientDTO savePAIPsico(
            String id,
            String paiPsicoAcude,
            String paiPsicoSintomas,
            String paiPsicoDiagnostico,
            String paiPsicoQuienDiagnostica,
            LocalDate paiPsicoFechaDiagnostico,
            String paiPsicoFormaEvaluacion,
            String paiPsicoSintomatologiaActual,
            String paiPsicoAntecedentes,
            String paiPsicoBreveHistorial,
            String paiPsicoOrientacion,
            String paiPsicoLenguaje,
            String paiPsicoMemoria,
            String paiPsicoAtencion,
            String paiPsicoPraxi,
            String paiPsicoPensamientoAbstracto,
            String paiPsicoPercepcion,
            String paiPsicoFuncionEjecutiva,
            String paiPsicoEscalaFolstein,
            String paiPsicoEvaluacionConductual,
            String paiPsicoPlanActValoracionS1,
            String paiPsicoPlanActValoracionS2,
            String paiPsicoPlanActInstrumentosS1,
            String paiPsicoPlanActInstrumentosS2,
            String paiPsicoPlanActObjetivosS1,
            String paiPsicoPlanActObjetivosS2,
            String paiPsicoPlanActActividadesS1,
            String paiPsicoPlanActActividadesS2,
            String paiPsicoPlanActIncidenciasS1,
            String paiPsicoPlanActIncidenciasS2,
            String paiPsicoValoraciones,
            String paiPsicoActuaciones,
            String paiPsicoIncidencias
    ) throws Exception;

    PatientDTO savePAITocupa(
            String id,
            String paiTocupaNivelIndependencia,
            String paiTocupaPlanMotriz,
            String paiTocupaAlimentacion,
            String paiTocupaWc,
            String paiTocupaAseo,
            String paiTocupaDeambular,
            String paiTocupaTransferencias,
            String paiTocupaVestido,
            String paiTocupaBano,
            String paiTocupaEscalones,
            String paiTocupaEsfinteres,
            String paiTocupaDinero,
            String paiTocupaCompras,
            String paiTocupaTelefono,
            String paiTocupaCasa,
            String paiTocupaCalle,
            String paiTocupaMedicacion,
            String paiTocupaIndiceBarthel,
            String paiTocupaEscalaActividad,
            String paiTocupaDisfrutaTiempo,
            String paiTocupaEspaciosOcio,
            String paiTocupaDisfrutaOcio,
            String paiTocupaRelacionOtros,
            String paiTocupaPropiosObjetivos,
            String paiTocupaParticipaActividades,
            String paiTocupaActividadesIniciativaPropia,
            String pai_tocupa_valoraciones,
            String pai_tocupa_actuaciones,
            String pai_tocupa_incidencias

    ) throws Exception;


    public PatientDTO savePAIEnfer(
            String id,
            String pai_enfer_diagnostico,
            String pai_enfer_problemas_audio,
            String pai_enfer_problemas_audio_text,
            String pai_enfer_uso_audifono,
            String pai_enfer_problemas_vision,
            String pai_enfer_problemas_vision_text,
            String pai_enfer_uso_gafas,
            String pai_enfer_tension,
            String pai_enfer_uso_medicacion,
            String pai_enfer_diabetes,
            String pai_enfer_diabetes_text,
            String pai_enfer_alergias,
            String pai_enfer_otras_enfermedades,
            String pai_enfer_tratamiento_medicamento_1,
            String pai_enfer_tratamiento_medicamento_2,
            String pai_enfer_tratamiento_medicamento_3,
            String pai_enfer_tratamiento_medicamento_4,
            String pai_enfer_tratamiento_dosis_1,
            String pai_enfer_tratamiento_dosis_2,
            String pai_enfer_tratamiento_dosis_3,
            String pai_enfer_tratamiento_dosis_4,
            String pai_enfer_tratamiento_fecha_1,
            String pai_enfer_tratamiento_fecha_2,
            String pai_enfer_tratamiento_fecha_3,
            String pai_enfer_tratamiento_fecha_4,
            String pai_enfer_tratamiento_para_1,
            String pai_enfer_tratamiento_para_2,
            String pai_enfer_tratamiento_para_3,
            String pai_enfer_tratamiento_para_4,
            String pai_enfer_medicacion_centro,
            String pai_enfer_medicacion_centro_text,
            String pai_enfer_medicacion_puntual,
            String pai_enfer_wc_esfinteres,
            String pai_enfer_wc_retencion,
            String pai_enfer_wc_estrenimiento,
            String pai_enfer_wc_acompanam,
            String pai_enfer_alim_alergias,
            String pai_enfer_alim_alergias_text,
            String pai_enfer_alim_dieta,
            String pai_enfer_alim_dieta_text,
            String pai_enfer_alim_problemas_deglucion,
            String pai_enfer_alim_espesantes,
            String pai_enfer_alim_ayuda,
            String pai_enfer_alim_observaciones,
            String pai_enfer_valoraciones,
            String pai_enfer_actuaciones,
            String pai_enfer_incidencias
    ) throws Exception;

    PatientDTO savePAISocial(
            String id,
            String pai_social_historia,
            String pai_social_informes,
            String pai_social_informes_text,
            String pai_social_valoracion_disca,
            String pai_social_valoracion_disca_fecha,
            String pai_social_valoracion_disca_ca,
            String pai_social_valoracion_disca_grado,
            String pai_social_3_persona,
            String pai_social_ayudas_tecnicas,
            String pai_social_ayudas_tecnicas_text,
            String pai_social_movilidad,
            String pai_social_ley_dependencai,
            String pai_social_grado_y_nivel,
            String pai_social_cuidador,
            String pai_social_relacion_cuidador,
            String pai_social_indicadores,
            String pai_social_apoyos,
            String pai_social_vive,
            String pai_social_domicilio_obstaculos,
            String pai_social_domicilio_ayudas_tecnicas,
            String pai_social_domicilio_confort,
            String pai_social_domicilio_actual_quiere,
            String pai_social_domicilio_actual_otro,
            String pai_social_domicilio_actula_residencia,
            String pai_social_apoyo_tipo1,
            String pai_social_apoyo_tipo2,
            String pai_social_apoyo_tipo3,
            String pai_social_apoyo_tipo4,
            String pai_social_apoyo_titularidad1,
            String pai_social_apoyo_titularidad2,
            String pai_social_apoyo_titularidad3,
            String pai_social_apoyo_titularidad4,
            String pai_social_apoyo_coste1,
            String pai_social_apoyo_coste2,
            String pai_social_apoyo_coste3,
            String pai_social_apoyo_coste4,
            String pai_social_apoyo_aportacion1,
            String pai_social_apoyo_aportacion2,
            String pai_social_apoyo_aportacion3,
            String pai_social_apoyo_aportacion4,
            String pai_social_apoyo_dom_prestacion1,
            String pai_social_apoyo_dom_prestacion2,
            String pai_social_apoyo_dom_prestacion3,
            String pai_social_apoyo_dom_prestacion4,
            String pai_social_apoyo_dom_intensidad1,
            String pai_social_apoyo_dom_intensidad2,
            String pai_social_apoyo_dom_intensidad3,
            String pai_social_apoyo_dom_intensidad4,
            String pai_social_apoyo_dom_coste1,
            String pai_social_apoyo_dom_coste2,
            String pai_social_apoyo_dom_coste3,
            String pai_social_apoyo_dom_coste4,
            String pai_social_apoyo_dom_aportacion1,
            String pai_social_apoyo_dom_aportacion2,
            String pai_social_apoyo_dom_aportacion3,
            String pai_social_apoyo_dom_aportacion4,
            String pai_social_apoyo_otras_prestacion1,
            String pai_social_apoyo_otras_prestacion2,
            String pai_social_apoyo_otras_prestacion3,
            String pai_social_apoyo_otras_prestacion4,
            String pai_social_apoyo_otras_titularidad1,
            String pai_social_apoyo_otras_titularidad2,
            String pai_social_apoyo_otras_titularidad3,
            String pai_social_apoyo_otras_titularidad4,
            String pai_social_apoyo_otras_intensidad1,
            String pai_social_apoyo_otras_intensidad2,
            String pai_social_apoyo_otras_intensidad3,
            String pai_social_apoyo_otras_intensidad4,
            String pai_social_ingresos,
            String pai_social_ingresos_familia,
            String pai_social_ingresos_cubre,
            String pai_social_nivel_estudios,
            String pai_social_relaciones,
            String pai_social_necesidades,
            String pai_social_objetivos,
            String pai_social_valoraciones,
            String pai_social_actuaciones,
            String pai_social_incidencias
    ) throws Exception;

    PatientDTO savePaiPortada(
            String id,
            String pai_portada_fecha,
            String pai_portada_representante_guardador,
            String pai_portada_cuidador_nombre,
            String pai_portada_cuidador_edad,
            String pai_portada_cuidador_dni,
            String pai_portada_cuidador_domicilio,
            String pai_portada_cuidador_estado_civil,
            String pai_portada_cuidador_profesion,
            String pai_portada_cuidador_relacion,
            String pai_portada_cuidador_convive_otros,
            String pai_portada_nss,
            String pai_portada_seguro_medico,
            String pai_portada_datos_medicos_enfermedades,
            String pai_portada_datos_medicos_grado_minusvalida,
            String pai_portada_datos_medicos_grado_dependencia,
            String pai_portada_profesional_1,
            String pai_portada_categoria_1,
            String pai_portada_profesional_2,
            String pai_portada_categoria_2,
            String pai_portada_profesional_3,
            String pai_portada_categoria_3,
            String pai_portada_profesional_4,
            String pai_portada_categoria_4,
            String pai_portada_profesional_5,
            String pai_portada_categoria_5
    ) throws Exception;

    PatientDTO generatePAIResumen(String id) throws Exception;
}
