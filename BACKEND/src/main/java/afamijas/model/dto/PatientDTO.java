package afamijas.model.dto;


import afamijas.model.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PatientDTO
{


    @Id
    private String id;

    private String username;

    private String email;

    private String name;

    private String surname1;

    private String surname2;



    private String documentid;

    private String documenttype;

    private String phone;

    private String postaladdress;

    private Integer idcity;

    private String cityname;

    private Integer idstate;

    private Integer idcountry;

    private String countryname;

    private String statename;

    private String postalcode;

    private String signature;

    private String photo_url;

    private String gender;

    private String nationality;

    private String idrelative;

    private String relativefullname;

    private Boolean is_principal_keeper;

    private String principal_keeper_fullname;

    private String principal_keeper_phone;

    private String num_contrato;

    private RouteDTO routeDTO;



    private String menu_type;

    private String breakfast_description;

    private String medication_description_morning;

    private String medication_description_evening;

    private String groupcode;


    private String ocupacionAnterior;



    //ALTA PACIENTE:

    private LocalDate birthdate;
    private String relativerelation;

    private String servicetype;
    private Boolean tallerpsico;

    private Boolean transportservice;
    private String transportservice_text;

    private Boolean comedorservice;
    private String comedorservice_text;

    private Boolean ayudadomicilioservice;
    private String ayudadomicilioservice_text;

    private Boolean duchaservice;
    private String duchaservice_text;

    //ALTA:

    private String register_document_url;
    private String register_document_url_signed;

    //PLAZA CONCERTADA:

    private String register19_document_url;
    private String register19_document_url_signed;

    private String register20_document_url;
    private String register20_document_url_signed;

    private String register24_document_url;
    private String register24_document_url_signed;

    private String register25_document_url;
    private String register25_document_url_signed;

    private String register26_document_url;
    private String register26_document_url_signed;

    private String register27_document_url;
    private String register27_document_url_signed;

    //PLAZA PRIVADA:

    private String register21_document_url;
    private String register21_document_url_signed;

    private String register22_document_url;
    private String register22_document_url_signed;

    private String register23_document_url;
    private String register23_document_url_signed;

    private String register28_document_url;
    private String register28_document_url_signed;

    //TALLER ESTIMULACIÓN:

    //register25_document_url;
    //register25_document_url_signed;

    //register26_document_url;
    //register26_document_url_signed;

    //register28_document_url;
    //register28_document_url_signed;

    private String register29_document_url;
    private String register29_document_url_signed;

    private String register30_document_url;
    private String register30_document_url_signed;

    //BAJA:

    private String unregister_document_url;
    private String unregister_document_url_signed;

    private String unregister_reason;



    private String language;






    //FICHA SOCIAL

    private String fs_url;

    private LocalDate fs_fecha_inscripcion;

    private String fs_num_expediente;

    private String fs_num_ss;

    private String fs_estado_civil;

    private Boolean fs_talleres_estimulacion;

    //private Boolean fs_servicio_transporte;  --> transportservice

    private Boolean fs_gradior_stimmulus;

    private Boolean fs_sad;

    private Boolean fs_other;

    private String fs_other_text;

    private Boolean fs_comer_solo;

    private Boolean fs_lavarse_solo;

    private Boolean fs_salir_sin_perderse;

    private Boolean fs_reconocer_caras;

    private Boolean fs_leer_y_escribir;

    private Boolean fs_incontenencia_urinaria;

    private Boolean fs_conversar;

    private Boolean fs_reconocer_objetos_cotidianos;

    private Boolean fs_sufrir_alucinaciones;

    private Boolean fs_fases_agitacion;

    private Boolean fs_dificultad_orientarse;

    private String fs_movilizarse;

    private String fs_datos_medicos;

    private Boolean fs_grado_minusvalia;

    private String fs_grado_minusvalia_text;

    private Boolean fs_grado_dependencia;

    private String fs_grado_dependencia_text;

    private Boolean fs_incapacitacion_judicial;

    private Boolean fs_ayudas_externas;

    private String fs_ayudas_externas_text;







    // HISTORIA SOCIAL

    private String hs_url;

    private Boolean hs_beca;

    private String hs_diagnostico;

    private Boolean hs_autonomia;

    private Boolean hs_ayuda_abd;

    private Boolean hs_uc_solo;

    private Boolean hs_uc_conyuge;

    private Boolean hs_uc_hijos;

    private Boolean hs_uc_other;

    private String hs_uc_other_text;


    private String hs_nivel_formativo;

    private String hs_interaccion_demas;

    private String hs_interaccion_profesioneales;

    private String hs_participacion_actividades;

    private String hs_integracion_dinamica;

    private String hs_grado_minusvalia_tipo;

    private String hs_grado_minusvalia_cuando;

    private Boolean hs_ley_dependencia_solicitada;

    private String hs_ley_dependencia_grado;

    private Boolean hs_recibe_servicio_administracion;

    private String hs_patologias;

    private Boolean hs_diabetico;

    private Boolean hs_hipertenso;

    private Boolean hs_alimenta_bien;

    private Boolean hs_duerme_bien;

    private Boolean hs_fuma_bebe;

    private Boolean hs_drogas;

    private String hs_drogas_text;

    private String hs_valoracion_salud;


    private Boolean hs_fam_dificultades_convivencia;

    private Boolean hs_fam_dificultades_economicas;

    private Boolean hs_fam_dificultad_cuidados;

    private Boolean hs_fam_sin_apoyo;

    private Boolean hs_fam_agotamiento_cuidador;

    private Boolean hs_viv_sin_domicilio;

    private Boolean hs_viv_ruinas;

    private Boolean hs_viv_barreras;

    private Boolean hs_viv_inhabitabilidad;

    private Boolean hs_alquiler_elevado;

    private Boolean hs_escaleras_exteriores;

    private Boolean hs_escaleras_interiores;

    private Boolean hs_banera;

    private Boolean hs_alfombras;

    private Boolean hs_otros;

    private String hs_otros_text;


    private String hs_nombre1;
    private String hs_parentesco1;
    private Integer hs_edad1;
    private String hs_profesion1;
    private String hs_nombre2;
    private String hs_parentesco2;
    private Integer hs_edad2;
    private String hs_profesion2;

    private String hs_nombre3;
    private String hs_parentesco3;
    private Integer hs_edad3;
    private String hs_profesion3;

    private String hs_nombre4;
    private String hs_parentesco4;
    private Integer hs_edad4;
    private String hs_profesion4;

    private Boolean hs_tiene_pareja;

    private String hs_relacion_pareja;

    private Boolean hs_tiene_hijos;

    private String hs_relacion_hijos;

    private Boolean hs_tiene_hermanos;

    private String hs_relacion_hermanos;

    private Boolean hs_visitas_familiares;

    private String hs_visitas_cuanto;

    private Boolean hs_apoyo_amigos;

    private Boolean hs_relacion_familia;

    private Boolean hs_acude_otras;


    private Boolean hs_recibe_pension;

    private String hs_otra_prestacion;

    private Double hs_cuantia_pension;

    private String hs_otros_ingresos;

    private String hs_otros_recursos;

    private String hs_valoracion_profesional;

    private String hs_observaciones;


    //INFORME SOCIAL

    private String is_url;
    private String is_tiempo_conoce_usuario;
    private String is_servicios_prestados;
    private String is_como_adaptado;
    private Boolean is_acudio_voluntad_propia;
    private String is_quien_influyo_decision;
    private String is_que_actividades;
    private String is_como_relaciona;
    private String is_como_pasa_dia;

    private Boolean is_problemas_psico;
    private String is_problemas_psico_text;

    private Boolean is_recibe_tratamiento;

    private String is_recibe_tratamiento_text;

    private Boolean is_familia_estru;

    private String is_familia_estru_text;

    private Boolean is_recibe_ingresos_actividad_laboral;

    private Boolean is_esta_buscando_empleo;

    private String is_vive_en;

    private Boolean is_cubiertas_necesidades_diarias;

    private String is_valoracion_profesional;

    private String is_propuesta;


    //INFORME NEUROPSICOLÓGICO

    private String ins_url;


    private LocalDate ins_fecha_informe;

    private String ins_motivo_consulta;

    private String ins_antecedentes;

    private String ins_diagnostico;

    private String ins_texto_pre_puntuaciones;


    private LocalDate ins_fecha1;
    private LocalDate ins_fecha2;
    private LocalDate ins_fecha3;
    private LocalDate ins_fecha4;

    private Double ins_orientacion1;
    private Double ins_orientacion2;
    private Double ins_orientacion3;
    private Double ins_orientacion4;

    private Double ins_lenguaje1;
    private Double ins_lenguaje2;
    private Double ins_lenguaje3;
    private Double ins_lenguaje4;

    private Double ins_memoria1;
    private Double ins_memoria2;
    private Double ins_memoria3;
    private Double ins_memoria4;


    private Double ins_atencalculo1;
    private Double ins_atencalculo2;
    private Double ins_atencalculo3;
    private Double ins_atencalculo4;


    private Double ins_praxis1;
    private Double ins_praxis2;
    private Double ins_praxis3;
    private Double ins_praxis4;


    private Double ins_pensabstracto1;
    private Double ins_pensabstracto2;
    private Double ins_pensabstracto3;
    private Double ins_pensabstracto4;


    private Double ins_percecpcion1;
    private Double ins_percecpcion2;
    private Double ins_percecpcion3;
    private Double ins_percecpcion4;


    private Double ins_total1;
    private Double ins_total2;
    private Double ins_total3;
    private Double ins_total4;


    private LocalDate ins_fecha_mms1;
    private LocalDate ins_fecha_mms2;
    private LocalDate ins_fecha_mms3;
    private LocalDate ins_fecha_mms4;

    private Double ins_mmse1;
    private Double ins_mmse2;
    private Double ins_mmse3;
    private Double ins_mmse4;

    private String ins_texto_post_puntuaciones;

    private LocalDate ins_fecha_ind1;
    private LocalDate ins_fecha_ind2;
    private LocalDate ins_fecha_ind3;

    private Double ins_indbathel1;
    private Double ins_indbathel2;
    private Double ins_indbathel3;

    private Double ins_indlawton1;
    private Double ins_indlawton2;
    private Double ins_indlawton3;

    private String ins_texto_eval_conductual;

    private String ins_texto_conclusion;


    // INFORME PSICO-SOCIAL

    private String ips_url;
    private LocalDate ips_fecha_informe;
    private String ips_sanitarios;
    private String ips_sociofamiliar;
    private String ips_evalcognitiva;
    private String ips_evalconductual;
    private String ips_evalfuncional;
    private String ips_situacioneconomica;
    private String ips_observaciones;




    public PatientDTO(User user, City city, State state, Country country, User relative, RouteDTO routeDTO)
    {
        this.id = user.get_id();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.surname1 = user.getSurname1();
        this.surname2 = user.getSurname2();
        this.documentid = user.getDocumentid();
        this.documenttype = user.getDocumenttype();
        this.phone = user.getPhone();
        this.postaladdress = user.getPostaladdress();

        this.num_contrato = user.getNum_contrato();

        this.idcity  = user.getIdcity();
        if(city!=null) this.cityname = city.getName();

        this.idstate = user.getIdstate();
        if(state!=null) this.statename = state.getName();

        this.idcountry = user.getIdcountry();
        if(country!=null) this.countryname = country.getName();

        this.postalcode = user.getPostalcode();

        this.signature = user.getSignature();
        this.photo_url = user.getPhoto_url();
        this.gender = user.getGender();

        this.nationality = user.getNationality();

        this.idrelative = user.getIdrelative();
        if(relative!=null)
        {
            this.relativefullname = relative.getFullname();
            if(relative.getIs_principal_keeper()== null || !relative.getIs_principal_keeper())
            {
                this.principal_keeper_fullname = relative.getPrincipal_keeper_fullname();
                this.principal_keeper_phone = relative.getPrincipal_keeper_phone();
            }
        }

        this.routeDTO = routeDTO;

        this.menu_type = user.getMenu_type();
        this.breakfast_description = user.getBreakfast_description();
        this.medication_description_morning = user.getMedication_description_morning();
        this.medication_description_evening = user.getMedication_description_evening();
        this.groupcode = user.getGroupcode();

        //ALTA:

        this.birthdate = user.getBirthdate();
        this.relativerelation = user.getRelativerelation();

        this.servicetype = user.getServicetype();
        this.tallerpsico = user.getTallerpsico();

        this.transportservice = user.getTransportservice();
        this.transportservice_text = user.getTransportservice_text();

        this.comedorservice = user.getComedorservice();
        this.comedorservice_text = user.getComedorservice_text();

        this.ayudadomicilioservice = user.getAyudadomicilioservice();
        this.ayudadomicilioservice_text = user.getAyudadomicilioservice_text();

        this.duchaservice = user.getDuchaservice();
        this.duchaservice_text = user.getDuchaservice_text();

        this.register_document_url = user.getRegister_document_url();
        this.register_document_url_signed = user.getRegister_document_url_signed();



        //PLAZA CONCERTADA:

        this.register19_document_url = user.getRegister19_document_url();
        this.register19_document_url_signed = user.getRegister19_document_url_signed();

        this.register20_document_url = user.getRegister20_document_url();
        this.register20_document_url_signed = user.getRegister20_document_url_signed();

        this.register24_document_url = user.getRegister24_document_url();
        this.register24_document_url_signed = user.getRegister24_document_url_signed();

        this.register25_document_url = user.getRegister25_document_url();
        this.register25_document_url_signed = user.getRegister25_document_url_signed();

        this.register26_document_url = user.getRegister26_document_url();
        this.register26_document_url_signed = user.getRegister26_document_url_signed();

        this.register27_document_url = user.getRegister27_document_url();
        this.register27_document_url_signed = user.getRegister27_document_url_signed();

        //PLAZA PRIVADA:

        this.register21_document_url = user.getRegister21_document_url();
        this.register21_document_url_signed = user.getRegister21_document_url_signed();

        this.register22_document_url = user.getRegister22_document_url();
        this.register22_document_url_signed = user.getRegister22_document_url_signed();

        this.register23_document_url = user.getRegister23_document_url();
        this.register23_document_url_signed = user.getRegister23_document_url_signed();

        this.register28_document_url = user.getRegister28_document_url();
        this.register28_document_url_signed = user.getRegister28_document_url_signed();

        //TALLER ESTIMULACIÓN:

        //register25_document_url = user.
        //register25_document_url_signed = user.

        //register26_document_url = user.
        //register26_document_url_signed = user.

        //register28_document_url = user.
        //register28_document_url_signed = user.

        this.register29_document_url = user.getRegister29_document_url();
        this.register29_document_url_signed = user.getRegister29_document_url_signed();

        this.register30_document_url = user.getRegister30_document_url();
        this.register30_document_url_signed = user.getRegister30_document_url_signed();

        this.ocupacionAnterior = user.getOcupacionAnterior();

        //BAJA:

        this.unregister_document_url = user.getUnregister_document_url();
        this.unregister_document_url_signed = user.getUnregister_document_url_signed();

        this.unregister_reason = user.getUnregister_reason();

        this.language = user.getLanguage();

        //FICHA SOCIAL
        this.fs_fecha_inscripcion = user.getFs_fecha_inscripcion();
        this.fs_num_expediente = user.getFs_num_expediente();
        this.fs_num_ss = user.getFs_num_ss();
        this.fs_estado_civil = user.getFs_estado_civil();
        this.fs_talleres_estimulacion = user.getFs_talleres_estimulacion();
        this.fs_gradior_stimmulus = user.getFs_gradior_stimmulus();
        this.fs_sad = user.getFs_sad();
        this.fs_other = user.getFs_other();
        this.fs_other_text = user.getFs_other_text();
        this.fs_comer_solo = user.getFs_comer_solo();
        this.fs_lavarse_solo = user.getFs_lavarse_solo();
        this.fs_salir_sin_perderse = user.getFs_salir_sin_perderse();
        this.fs_reconocer_caras = user.getFs_reconocer_caras();
        this.fs_leer_y_escribir = user.getFs_leer_y_escribir();
        this.fs_incontenencia_urinaria = user.getFs_incontenencia_urinaria();
        this.fs_conversar = user.getFs_conversar();
        this.fs_reconocer_objetos_cotidianos = user.getFs_reconocer_objetos_cotidianos();
        this.fs_sufrir_alucinaciones = user.getFs_sufrir_alucinaciones();
        this.fs_fases_agitacion = user.getFs_fases_agitacion();
        this.fs_dificultad_orientarse = user.getFs_dificultad_orientarse();
        this.fs_movilizarse = user.getFs_movilizarse();
        this.fs_datos_medicos = user.getFs_datos_medicos();
        this.fs_grado_minusvalia = user.getFs_grado_minusvalia();
        this.fs_grado_minusvalia_text = user.getFs_grado_minusvalia_text();
        this.fs_grado_dependencia = user.getFs_grado_dependencia();
        this.fs_grado_dependencia_text = user.getFs_grado_dependencia_text();
        this.fs_incapacitacion_judicial = user.getFs_incapacitacion_judicial();
        this.fs_ayudas_externas = user.getFs_ayudas_externas();
        this.fs_ayudas_externas_text = user.getFs_ayudas_externas_text();

        //HISTORIA SOCIAL
        this.hs_beca = user.getHs_beca();
        this.hs_diagnostico = user.getHs_diagnostico();
        this.hs_autonomia = user.getHs_autonomia();
        this.hs_ayuda_abd = user.getHs_ayuda_abd();

        this.hs_uc_solo = user.getHs_uc_solo();
        this.hs_uc_conyuge = user.getHs_uc_conyuge();
        this.hs_uc_hijos = user.getHs_uc_hijos();
        this.hs_uc_other = user.getHs_uc_other();
        this.hs_uc_other_text = user.getHs_uc_other_text();

        this.hs_nivel_formativo = user.getHs_nivel_formativo();
        this.hs_interaccion_demas = user.getHs_interaccion_demas();
        this.hs_interaccion_profesioneales = user.getHs_interaccion_profesioneales();
        this.hs_participacion_actividades = user.getHs_participacion_actividades();
        this.hs_integracion_dinamica = user.getHs_integracion_dinamica();
        this.hs_grado_minusvalia_tipo = user.getHs_grado_minusvalia_tipo();
        this.hs_grado_minusvalia_cuando = user.getHs_grado_minusvalia_cuando();
        this.hs_ley_dependencia_solicitada = user.getHs_ley_dependencia_solicitada();
        this.hs_ley_dependencia_grado = user.getHs_ley_dependencia_grado();
        this.hs_recibe_servicio_administracion = user.getHs_recibe_servicio_administracion();
        this.hs_patologias = user.getHs_patologias();
        this.hs_diabetico = user.getHs_diabetico();
        this.hs_hipertenso = user.getHs_hipertenso();
        this.hs_alimenta_bien = user.getHs_alimenta_bien();
        this.hs_duerme_bien = user.getHs_duerme_bien();
        this.hs_fuma_bebe = user.getHs_fuma_bebe();
        this.hs_drogas = user.getHs_drogas();
        this.hs_drogas_text = user.getHs_drogas_text();
        this.hs_valoracion_salud = user.getHs_valoracion_salud();
        this.hs_fam_dificultades_convivencia = user.getHs_fam_dificultades_convivencia();
        this.hs_fam_dificultades_economicas = user.getHs_fam_dificultades_economicas();
        this.hs_fam_dificultad_cuidados = user.getHs_fam_dificultad_cuidados();
        this.hs_fam_sin_apoyo = user.getHs_fam_sin_apoyo();
        this.hs_fam_agotamiento_cuidador = user.getHs_fam_agotamiento_cuidador();
        this.hs_viv_sin_domicilio = user.getHs_viv_sin_domicilio();
        this.hs_viv_ruinas = user.getHs_viv_ruinas();
        this.hs_viv_barreras = user.getHs_viv_barreras();
        this.hs_viv_inhabitabilidad = user.getHs_viv_inhabitabilidad();
        this.hs_alquiler_elevado = user.getHs_alquiler_elevado();
        this.hs_escaleras_exteriores = user.getHs_escaleras_exteriores();
        this.hs_escaleras_interiores = user.getHs_escaleras_interiores();
        this.hs_banera = user.getHs_banera();
        this.hs_alfombras = user.getHs_alfombras();
        this.hs_otros = user.getHs_otros();
        this.hs_otros_text = user.getHs_otros_text();
        this.hs_nombre1 = user.getHs_nombre1();
        this.hs_parentesco1 = user.getHs_parentesco1();
        this.hs_edad1 = user.getHs_edad1();
        this.hs_profesion1 = user.getHs_profesion1();
        this.hs_nombre2 = user.getHs_nombre2();
        this.hs_parentesco2 = user.getHs_parentesco2();
        this.hs_edad2 = user.getHs_edad2();
        this.hs_profesion2 = user.getHs_profesion2();
        this.hs_nombre3 = user.getHs_nombre3();
        this.hs_parentesco3 = user.getHs_parentesco3();
        this.hs_edad3 = user.getHs_edad3();
        this.hs_profesion3 = user.getHs_profesion3();
        this.hs_nombre4 = user.getHs_nombre4();
        this.hs_parentesco4 = user.getHs_parentesco4();
        this.hs_edad4 = user.getHs_edad4();
        this.hs_profesion4 = user.getHs_profesion4();
        this.hs_tiene_pareja = user.getHs_tiene_pareja();
        this.hs_relacion_pareja = user.getHs_relacion_pareja();
        this.hs_tiene_hijos = user.getHs_tiene_hijos();
        this.hs_relacion_hijos = user.getHs_relacion_hijos();
        this.hs_tiene_hermanos = user.getHs_tiene_hermanos();
        this.hs_relacion_hermanos = user.getHs_relacion_hermanos();
        this.hs_visitas_familiares = user.getHs_visitas_familiares();
        this.hs_visitas_cuanto = user.getHs_visitas_cuanto();
        this.hs_apoyo_amigos = user.getHs_apoyo_amigos();
        this.hs_relacion_familia = user.getHs_relacion_familia();
        this.hs_acude_otras = user.getHs_acude_otras();
        this.hs_recibe_pension = user.getHs_recibe_pension();
        this.hs_otra_prestacion = user.getHs_otra_prestacion();
        this.hs_cuantia_pension = user.getHs_cuantia_pension();
        this.hs_otros_ingresos = user.getHs_otros_ingresos();
        this.hs_otros_recursos = user.getHs_otros_recursos();
        this.hs_valoracion_profesional = user.getHs_valoracion_profesional();
        this.hs_observaciones = user.getHs_observaciones();


        //INFORME SOCIAL
        this.is_tiempo_conoce_usuario = user.getIs_tiempo_conoce_usuario();
        this.is_servicios_prestados = user.getIs_servicios_prestados();
        this.is_como_adaptado = user.getIs_como_adaptado();
        this.is_acudio_voluntad_propia = user.getIs_acudio_voluntad_propia();
        this.is_quien_influyo_decision = user.getIs_quien_influyo_decision();
        this.is_que_actividades = user.getIs_que_actividades();
        this.is_como_relaciona = user.getIs_como_relaciona();
        this.is_como_pasa_dia = user.getIs_como_pasa_dia();

        this.is_problemas_psico = user.getIs_problemas_psico();
        this.is_problemas_psico_text = user.getIs_problemas_psico_text();

        this.is_familia_estru = user.getIs_familia_estru();
        this.is_familia_estru_text = user.getIs_familia_estru_text();

        this.is_recibe_ingresos_actividad_laboral = user.getIs_recibe_ingresos_actividad_laboral();
        this.is_esta_buscando_empleo = user.getIs_esta_buscando_empleo();

        this.is_vive_en = user.getIs_vive_en();

        this.is_cubiertas_necesidades_diarias = user.getIs_cubiertas_necesidades_diarias();

        this.is_valoracion_profesional = user.getIs_valoracion_profesional();
        this.is_propuesta = user.getIs_propuesta();

        this.is_recibe_tratamiento = user.getIs_recibe_tratamiento();
        this.is_recibe_tratamiento_text = user.getIs_recibe_tratamiento_text();

        //INFOMKE NEUROPSICOLOGICO

        this.ins_fecha_informe = user.getIns_fecha_informe();

        this.ins_motivo_consulta = user.getIns_motivo_consulta();
        this.ins_antecedentes = user.getIns_antecedentes();
        this.ins_diagnostico = user.getIns_diagnostico();
        this.ins_texto_pre_puntuaciones = user.getIns_texto_pre_puntuaciones();

        this.ins_fecha1 = user.getIns_fecha1();
        this.ins_fecha2 = user.getIns_fecha2();
        this.ins_fecha3 = user.getIns_fecha3();
        this.ins_fecha4 = user.getIns_fecha4();

        this.ins_orientacion1 = user.getIns_orientacion1();
        this.ins_orientacion2 = user.getIns_orientacion2();
        this.ins_orientacion3 = user.getIns_orientacion3();
        this.ins_orientacion4 = user.getIns_orientacion4();

        this.ins_lenguaje1 = user.getIns_lenguaje1();
        this.ins_lenguaje2 = user.getIns_lenguaje2();
        this.ins_lenguaje3 = user.getIns_lenguaje3();
        this.ins_lenguaje4 = user.getIns_lenguaje4();

        this.ins_memoria1 = user.getIns_memoria1();
        this.ins_memoria2 = user.getIns_memoria2();
        this.ins_memoria3 = user.getIns_memoria3();
        this.ins_memoria4 = user.getIns_memoria4();

        this.ins_atencalculo1 = user.getIns_atencalculo1();
        this.ins_atencalculo2 = user.getIns_atencalculo2();
        this.ins_atencalculo3 = user.getIns_atencalculo3();
        this.ins_atencalculo4 = user.getIns_atencalculo4();

        this.ins_praxis1 = user.getIns_praxis1();
        this.ins_praxis2 = user.getIns_praxis2();
        this.ins_praxis3 = user.getIns_praxis3();
        this.ins_praxis4 = user.getIns_praxis4();

        this.ins_pensabstracto1 = user.getIns_pensabstracto1();
        this.ins_pensabstracto2 = user.getIns_pensabstracto2();
        this.ins_pensabstracto3 = user.getIns_pensabstracto3();
        this.ins_pensabstracto4 = user.getIns_pensabstracto4();

        this.ins_percecpcion1 = user.getIns_percecpcion1();
        this.ins_percecpcion2 = user.getIns_percecpcion2();
        this.ins_percecpcion3 = user.getIns_percecpcion3();
        this.ins_percecpcion4 = user.getIns_percecpcion4();

        this.ins_total1 = user.getIns_total1();
        this.ins_total2 = user.getIns_total2();
        this.ins_total3 = user.getIns_total3();
        this.ins_total4 = user.getIns_total4();

        this.ins_fecha_mms1 = user.getIns_fecha_mms1();
        this.ins_fecha_mms2 = user.getIns_fecha_mms2();
        this.ins_fecha_mms3 = user.getIns_fecha_mms3();
        this.ins_fecha_mms4 = user.getIns_fecha_mms4();

        this.ins_mmse1 = user.getIns_mmse1();
        this.ins_mmse2 = user.getIns_mmse2();
        this.ins_mmse3 = user.getIns_mmse3();
        this.ins_mmse4 = user.getIns_mmse4();

        this.ins_texto_post_puntuaciones = user.getIns_texto_post_puntuaciones();

        this.ins_fecha_ind1 = user.getIns_fecha_ind1();
        this.ins_fecha_ind2 = user.getIns_fecha_ind2();
        this.ins_fecha_ind3 = user.getIns_fecha_ind3();

        this.ins_indbathel1 = user.getIns_indbathel1();
        this.ins_indbathel2 = user.getIns_indbathel2();
        this.ins_indbathel3 = user.getIns_indbathel3();

        this.ins_indlawton1 = user.getIns_indlawton1();
        this.ins_indlawton2 = user.getIns_indlawton2();
        this.ins_indlawton3 = user.getIns_indlawton3();

        this.ins_texto_eval_conductual = user.getIns_texto_eval_conductual();

        this.ins_texto_conclusion = user.getIns_texto_conclusion();

        this.ips_fecha_informe = user.getIps_fecha_informe();
        this.ips_evalcognitiva = user.getIps_evalcognitiva();
        this.ips_evalconductual = user.getIps_evalconductual();
        this.ips_evalfuncional = user.getIps_evalfuncional();
        this.ips_sanitarios = user.getIps_sanitarios();
        this.ips_situacioneconomica = user.getIps_situacioneconomica();
        this.ips_sociofamiliar = user.getIps_sociofamiliar();
        this.ips_observaciones = user.getIps_observaciones();


    }


    @Transient
    public String getFullname()
    {
        String n = this.name == null?"": this.name;
        String s1 = this.surname1 == null?"": this.surname1;
        String s2 = this.surname2 == null?"": this.surname2;
        String fullname = ((n + " " + s1).trim() + " " + s2).trim();
        return fullname.equals("")?this.username:fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupcode() {
        return groupcode;
    }

    public void setGroupcode(String groupcode) {
        this.groupcode = groupcode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname1() {
        return surname1;
    }

    public void setSurname1(String surname1) {
        this.surname1 = surname1;
    }

    public String getSurname2() {
        return surname2;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPostaladdress() {
        return postaladdress;
    }

    public void setPostaladdress(String postaladdress) {
        this.postaladdress = postaladdress;
    }



    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdrelative() {
        return idrelative;
    }

    public void setIdrelative(String idrelative) {
        this.idrelative = idrelative;
    }

    public Boolean getIs_principal_keeper() {
        return is_principal_keeper;
    }

    public void setIs_principal_keeper(Boolean is_principal_keeper) {
        this.is_principal_keeper = is_principal_keeper;
    }

    public String getPrincipal_keeper_fullname() {
        return principal_keeper_fullname;
    }

    public void setPrincipal_keeper_fullname(String principal_keeper_fullname) {
        this.principal_keeper_fullname = principal_keeper_fullname;
    }

    public String getPrincipal_keeper_phone() {
        return principal_keeper_phone;
    }

    public void setPrincipal_keeper_phone(String principal_keeper_phone) {
        this.principal_keeper_phone = principal_keeper_phone;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String statename) {
        this.statename = statename;
    }

    public String getRelativefullname() {
        return relativefullname;
    }

    public void setRelativefullname(String relativefullname) {
        this.relativefullname = relativefullname;
    }

    public RouteDTO getRouteDTO() {
        return routeDTO;
    }

    public void setRouteDTO(RouteDTO routeDTO) {
        this.routeDTO = routeDTO;
    }

    public String getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(String menu_type) {
        this.menu_type = menu_type;
    }

    public String getBreakfast_description() {
        return breakfast_description;
    }

    public void setBreakfast_description(String breakfast_description) {
        this.breakfast_description = breakfast_description;
    }

    public String getMedication_description_morning() {
        return medication_description_morning;
    }

    public void setMedication_description_morning(String medication_description_morning) {
        this.medication_description_morning = medication_description_morning;
    }

    public String getMedication_description_evening() {
        return medication_description_evening;
    }

    public void setMedication_description_evening(String medication_description_evening) {
        this.medication_description_evening = medication_description_evening;
    }


    public Integer getIdcity() {
        return idcity;
    }

    public void setIdcity(Integer idcity) {
        this.idcity = idcity;
    }

    public Integer getIdstate() {
        return idstate;
    }

    public void setIdstate(Integer idstate) {
        this.idstate = idstate;
    }

    public Integer getIdcountry() {
        return idcountry;
    }

    public void setIdcountry(Integer idcountry) {
        this.idcountry = idcountry;
    }

    public String getCountryname() {
        return countryname;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }


    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getRelativerelation() {
        return relativerelation;
    }

    public void setRelativerelation(String relativerelation) {
        this.relativerelation = relativerelation;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public Boolean getTallerpsico() {
        return tallerpsico;
    }

    public void setTallerpsico(Boolean tallerpsico) {
        this.tallerpsico = tallerpsico;
    }

    public Boolean getTransportservice() {
        return transportservice;
    }

    public void setTransportservice(Boolean transportservice) {
        this.transportservice = transportservice;
    }

    public String getTransportservice_text() {
        return transportservice_text;
    }

    public void setTransportservice_text(String transportservice_text) {
        this.transportservice_text = transportservice_text;
    }

    public Boolean getComedorservice() {
        return comedorservice;
    }

    public void setComedorservice(Boolean comedorservice) {
        this.comedorservice = comedorservice;
    }

    public String getComedorservice_text() {
        return comedorservice_text;
    }

    public void setComedorservice_text(String comedorservice_text) {
        this.comedorservice_text = comedorservice_text;
    }

    public Boolean getAyudadomicilioservice() {
        return ayudadomicilioservice;
    }

    public void setAyudadomicilioservice(Boolean ayudadomicilioservice) {
        this.ayudadomicilioservice = ayudadomicilioservice;
    }

    public String getAyudadomicilioservice_text() {
        return ayudadomicilioservice_text;
    }

    public void setAyudadomicilioservice_text(String ayudadomicilioservice_text) {
        this.ayudadomicilioservice_text = ayudadomicilioservice_text;
    }

    public Boolean getDuchaservice() {
        return duchaservice;
    }

    public void setDuchaservice(Boolean duchaservice) {
        this.duchaservice = duchaservice;
    }

    public String getDuchaservice_text() {
        return duchaservice_text;
    }

    public void setDuchaservice_text(String duchaservice_text) {
        this.duchaservice_text = duchaservice_text;
    }

    public String getRegister_document_url() {
        return register_document_url;
    }

    public void setRegister_document_url(String register_document_url) {
        this.register_document_url = register_document_url;
    }

    public String getRegister_document_url_signed() {
        return register_document_url_signed;
    }

    public void setRegister_document_url_signed(String register_document_url_signed) {
        this.register_document_url_signed = register_document_url_signed;
    }

    public String getRegister19_document_url() {
        return register19_document_url;
    }

    public void setRegister19_document_url(String register19_document_url) {
        this.register19_document_url = register19_document_url;
    }

    public String getRegister19_document_url_signed() {
        return register19_document_url_signed;
    }

    public void setRegister19_document_url_signed(String register19_document_url_signed) {
        this.register19_document_url_signed = register19_document_url_signed;
    }

    public String getRegister20_document_url() {
        return register20_document_url;
    }

    public void setRegister20_document_url(String register20_document_url) {
        this.register20_document_url = register20_document_url;
    }

    public String getRegister20_document_url_signed() {
        return register20_document_url_signed;
    }

    public void setRegister20_document_url_signed(String register20_document_url_signed) {
        this.register20_document_url_signed = register20_document_url_signed;
    }

    public String getRegister24_document_url() {
        return register24_document_url;
    }

    public void setRegister24_document_url(String register24_document_url) {
        this.register24_document_url = register24_document_url;
    }

    public String getRegister24_document_url_signed() {
        return register24_document_url_signed;
    }

    public void setRegister24_document_url_signed(String register24_document_url_signed) {
        this.register24_document_url_signed = register24_document_url_signed;
    }

    public String getRegister25_document_url() {
        return register25_document_url;
    }

    public void setRegister25_document_url(String register25_document_url) {
        this.register25_document_url = register25_document_url;
    }


    public String getRegister26_document_url() {
        return register26_document_url;
    }

    public void setRegister26_document_url(String register26_document_url) {
        this.register26_document_url = register26_document_url;
    }

    public String getRegister26_document_url_signed() {
        return register26_document_url_signed;
    }

    public void setRegister26_document_url_signed(String register26_document_url_signed) {
        this.register26_document_url_signed = register26_document_url_signed;
    }

    public String getRegister27_document_url() {
        return register27_document_url;
    }

    public void setRegister27_document_url(String register27_document_url) {
        this.register27_document_url = register27_document_url;
    }

    public String getRegister27_document_url_signed() {
        return register27_document_url_signed;
    }

    public void setRegister27_document_url_signed(String register27_document_url_signed) {
        this.register27_document_url_signed = register27_document_url_signed;
    }

    public String getRegister21_document_url() {
        return register21_document_url;
    }

    public void setRegister21_document_url(String register21_document_url) {
        this.register21_document_url = register21_document_url;
    }

    public String getRegister21_document_url_signed() {
        return register21_document_url_signed;
    }

    public void setRegister21_document_url_signed(String register21_document_url_signed) {
        this.register21_document_url_signed = register21_document_url_signed;
    }

    public String getRegister22_document_url() {
        return register22_document_url;
    }

    public void setRegister22_document_url(String register22_document_url) {
        this.register22_document_url = register22_document_url;
    }

    public String getRegister22_document_url_signed() {
        return register22_document_url_signed;
    }

    public void setRegister22_document_url_signed(String register22_document_url_signed) {
        this.register22_document_url_signed = register22_document_url_signed;
    }

    public String getRegister23_document_url() {
        return register23_document_url;
    }

    public void setRegister23_document_url(String register23_document_url) {
        this.register23_document_url = register23_document_url;
    }

    public String getRegister23_document_url_signed() {
        return register23_document_url_signed;
    }

    public void setRegister23_document_url_signed(String register23_document_url_signed) {
        this.register23_document_url_signed = register23_document_url_signed;
    }

    public String getRegister28_document_url() {
        return register28_document_url;
    }

    public void setRegister28_document_url(String register28_document_url) {
        this.register28_document_url = register28_document_url;
    }

    public String getRegister28_document_url_signed() {
        return register28_document_url_signed;
    }

    public void setRegister28_document_url_signed(String register28_document_url_signed) {
        this.register28_document_url_signed = register28_document_url_signed;
    }

    public String getRegister29_document_url() {
        return register29_document_url;
    }

    public void setRegister29_document_url(String register29_document_url) {
        this.register29_document_url = register29_document_url;
    }

    public String getRegister29_document_url_signed() {
        return register29_document_url_signed;
    }

    public void setRegister29_document_url_signed(String register29_document_url_signed) {
        this.register29_document_url_signed = register29_document_url_signed;
    }

    public String getRegister30_document_url() {
        return register30_document_url;
    }

    public void setRegister30_document_url(String register30_document_url) {
        this.register30_document_url = register30_document_url;
    }

    public String getRegister30_document_url_signed() {
        return register30_document_url_signed;
    }

    public void setRegister30_document_url_signed(String register30_document_url_signed) {
        this.register30_document_url_signed = register30_document_url_signed;
    }

    public String getUnregister_document_url() {
        return unregister_document_url;
    }

    public void setUnregister_document_url(String unregister_document_url) {
        this.unregister_document_url = unregister_document_url;
    }

    public String getUnregister_document_url_signed() {
        return unregister_document_url_signed;
    }

    public void setUnregister_document_url_signed(String unregister_document_url_signed) {
        this.unregister_document_url_signed = unregister_document_url_signed;
    }

    public String getUnregister_reason() {
        return unregister_reason;
    }

    public void setUnregister_reason(String unregister_reason) {
        this.unregister_reason = unregister_reason;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getRegister25_document_url_signed() {
        return register25_document_url_signed;
    }

    public void setRegister25_document_url_signed(String register25_document_url_signed) {
        this.register25_document_url_signed = register25_document_url_signed;
    }

    public String getDocumenttype() {
        return documenttype;
    }

    public void setDocumenttype(String documenttype) {
        this.documenttype = documenttype;
    }

    public LocalDate getFs_fecha_inscripcion() {
        return fs_fecha_inscripcion;
    }

    public void setFs_fecha_inscripcion(LocalDate fs_fecha_inscripcion) {
        this.fs_fecha_inscripcion = fs_fecha_inscripcion;
    }

    public String getFs_num_expediente() {
        return fs_num_expediente;
    }

    public void setFs_num_expediente(String fs_num_expediente) {
        this.fs_num_expediente = fs_num_expediente;
    }

    public String getFs_num_ss() {
        return fs_num_ss;
    }

    public void setFs_num_ss(String fs_num_ss) {
        this.fs_num_ss = fs_num_ss;
    }

    public String getFs_estado_civil() {
        return fs_estado_civil;
    }

    public void setFs_estado_civil(String fs_estado_civil) {
        this.fs_estado_civil = fs_estado_civil;
    }

    public Boolean getFs_gradior_stimmulus() {
        return fs_gradior_stimmulus;
    }

    public void setFs_gradior_stimmulus(Boolean fs_gradior_stimmulus) {
        this.fs_gradior_stimmulus = fs_gradior_stimmulus;
    }

    public Boolean getFs_sad() {
        return fs_sad;
    }

    public void setFs_sad(Boolean fs_sad) {
        this.fs_sad = fs_sad;
    }

    public Boolean getFs_other() {
        return fs_other;
    }

    public void setFs_other(Boolean fs_other) {
        this.fs_other = fs_other;
    }

    public String getFs_other_text() {
        return fs_other_text;
    }

    public void setFs_other_text(String fs_other_text) {
        this.fs_other_text = fs_other_text;
    }

    public Boolean getFs_comer_solo() {
        return fs_comer_solo;
    }

    public void setFs_comer_solo(Boolean fs_comer_solo) {
        this.fs_comer_solo = fs_comer_solo;
    }

    public Boolean getFs_lavarse_solo() {
        return fs_lavarse_solo;
    }

    public void setFs_lavarse_solo(Boolean fs_lavarse_solo) {
        this.fs_lavarse_solo = fs_lavarse_solo;
    }

    public Boolean getFs_salir_sin_perderse() {
        return fs_salir_sin_perderse;
    }

    public void setFs_salir_sin_perderse(Boolean fs_salir_sin_perderse) {
        this.fs_salir_sin_perderse = fs_salir_sin_perderse;
    }

    public Boolean getFs_reconocer_caras() {
        return fs_reconocer_caras;
    }

    public void setFs_reconocer_caras(Boolean fs_reconocer_caras) {
        this.fs_reconocer_caras = fs_reconocer_caras;
    }

    public Boolean getFs_leer_y_escribir() {
        return fs_leer_y_escribir;
    }

    public void setFs_leer_y_escribir(Boolean fs_leer_y_escribir) {
        this.fs_leer_y_escribir = fs_leer_y_escribir;
    }

    public Boolean getFs_incontenencia_urinaria() {
        return fs_incontenencia_urinaria;
    }

    public void setFs_incontenencia_urinaria(Boolean fs_incontenencia_urinaria) {
        this.fs_incontenencia_urinaria = fs_incontenencia_urinaria;
    }

    public Boolean getFs_conversar() {
        return fs_conversar;
    }

    public void setFs_conversar(Boolean fs_conversar) {
        this.fs_conversar = fs_conversar;
    }

    public Boolean getFs_reconocer_objetos_cotidianos() {
        return fs_reconocer_objetos_cotidianos;
    }

    public void setFs_reconocer_objetos_cotidianos(Boolean fs_reconocer_objetos_cotidianos) {
        this.fs_reconocer_objetos_cotidianos = fs_reconocer_objetos_cotidianos;
    }

    public Boolean getFs_sufrir_alucinaciones() {
        return fs_sufrir_alucinaciones;
    }

    public void setFs_sufrir_alucinaciones(Boolean fs_sufrir_alucinaciones) {
        this.fs_sufrir_alucinaciones = fs_sufrir_alucinaciones;
    }

    public Boolean getFs_fases_agitacion() {
        return fs_fases_agitacion;
    }

    public void setFs_fases_agitacion(Boolean fs_fases_agitacion) {
        this.fs_fases_agitacion = fs_fases_agitacion;
    }

    public Boolean getFs_dificultad_orientarse() {
        return fs_dificultad_orientarse;
    }

    public void setFs_dificultad_orientarse(Boolean fs_dificultad_orientarse) {
        this.fs_dificultad_orientarse = fs_dificultad_orientarse;
    }

    public String getFs_movilizarse() {
        return fs_movilizarse;
    }

    public void setFs_movilizarse(String fs_movilizarse) {
        this.fs_movilizarse = fs_movilizarse;
    }

    public String getFs_datos_medicos() {
        return fs_datos_medicos;
    }

    public void setFs_datos_medicos(String fs_datos_medicos) {
        this.fs_datos_medicos = fs_datos_medicos;
    }

    public Boolean getFs_grado_minusvalia() {
        return fs_grado_minusvalia;
    }

    public void setFs_grado_minusvalia(Boolean fs_grado_minusvalia) {
        this.fs_grado_minusvalia = fs_grado_minusvalia;
    }

    public String getFs_grado_minusvalia_text() {
        return fs_grado_minusvalia_text;
    }

    public void setFs_grado_minusvalia_text(String fs_grado_minusvalia_text) {
        this.fs_grado_minusvalia_text = fs_grado_minusvalia_text;
    }

    public Boolean getFs_grado_dependencia() {
        return fs_grado_dependencia;
    }

    public void setFs_grado_dependencia(Boolean fs_grado_dependencia) {
        this.fs_grado_dependencia = fs_grado_dependencia;
    }

    public String getFs_grado_dependencia_text() {
        return fs_grado_dependencia_text;
    }

    public void setFs_grado_dependencia_text(String fs_grado_dependencia_text) {
        this.fs_grado_dependencia_text = fs_grado_dependencia_text;
    }

    public Boolean getFs_incapacitacion_judicial() {
        return fs_incapacitacion_judicial;
    }

    public void setFs_incapacitacion_judicial(Boolean fs_incapacitacion_judicial) {
        this.fs_incapacitacion_judicial = fs_incapacitacion_judicial;
    }

    public Boolean getFs_ayudas_externas() {
        return fs_ayudas_externas;
    }

    public void setFs_ayudas_externas(Boolean fs_ayudas_externas) {
        this.fs_ayudas_externas = fs_ayudas_externas;
    }

    public String getFs_ayudas_externas_text() {
        return fs_ayudas_externas_text;
    }

    public void setFs_ayudas_externas_text(String fs_ayudas_externas_text) {
        this.fs_ayudas_externas_text = fs_ayudas_externas_text;
    }

    public String getFs_url() {
        return fs_url;
    }

    public void setFs_url(String fs_url) {
        this.fs_url = fs_url;
    }

    public Boolean getFs_talleres_estimulacion() {
        return fs_talleres_estimulacion;
    }

    public void setFs_talleres_estimulacion(Boolean fs_talleres_estimulacion) {
        this.fs_talleres_estimulacion = fs_talleres_estimulacion;
    }


    public Boolean getHs_beca() {
        return hs_beca;
    }

    public void setHs_beca(Boolean hs_beca) {
        this.hs_beca = hs_beca;
    }

    public String getHs_diagnostico() {
        return hs_diagnostico;
    }

    public void setHs_diagnostico(String hs_diagnostico) {
        this.hs_diagnostico = hs_diagnostico;
    }

    public Boolean getHs_autonomia() {
        return hs_autonomia;
    }

    public void setHs_autonomia(Boolean hs_autonomia) {
        this.hs_autonomia = hs_autonomia;
    }

    public Boolean getHs_ayuda_abd() {
        return hs_ayuda_abd;
    }

    public void setHs_ayuda_abd(Boolean hs_ayuda_abd) {
        this.hs_ayuda_abd = hs_ayuda_abd;
    }

    public String getHs_nivel_formativo() {
        return hs_nivel_formativo;
    }

    public void setHs_nivel_formativo(String hs_nivel_formativo) {
        this.hs_nivel_formativo = hs_nivel_formativo;
    }

    public String getHs_interaccion_demas() {
        return hs_interaccion_demas;
    }

    public void setHs_interaccion_demas(String hs_interaccion_demas) {
        this.hs_interaccion_demas = hs_interaccion_demas;
    }

    public String getHs_interaccion_profesioneales() {
        return hs_interaccion_profesioneales;
    }

    public void setHs_interaccion_profesioneales(String hs_interaccion_profesioneales) {
        this.hs_interaccion_profesioneales = hs_interaccion_profesioneales;
    }

    public String getHs_participacion_actividades() {
        return hs_participacion_actividades;
    }

    public void setHs_participacion_actividades(String hs_participacion_actividades) {
        this.hs_participacion_actividades = hs_participacion_actividades;
    }

    public String getHs_integracion_dinamica() {
        return hs_integracion_dinamica;
    }

    public void setHs_integracion_dinamica(String hs_integracion_dinamica) {
        this.hs_integracion_dinamica = hs_integracion_dinamica;
    }

    public String getHs_grado_minusvalia_tipo() {
        return hs_grado_minusvalia_tipo;
    }

    public void setHs_grado_minusvalia_tipo(String hs_grado_minusvalia_tipo) {
        this.hs_grado_minusvalia_tipo = hs_grado_minusvalia_tipo;
    }

    public String getHs_grado_minusvalia_cuando() {
        return hs_grado_minusvalia_cuando;
    }

    public void setHs_grado_minusvalia_cuando(String hs_grado_minusvalia_cuando) {
        this.hs_grado_minusvalia_cuando = hs_grado_minusvalia_cuando;
    }

    public Boolean getHs_ley_dependencia_solicitada() {
        return hs_ley_dependencia_solicitada;
    }

    public void setHs_ley_dependencia_solicitada(Boolean hs_ley_dependencia_solicitada) {
        this.hs_ley_dependencia_solicitada = hs_ley_dependencia_solicitada;
    }

    public String getHs_ley_dependencia_grado() {
        return hs_ley_dependencia_grado;
    }

    public void setHs_ley_dependencia_grado(String hs_ley_dependencia_grado) {
        this.hs_ley_dependencia_grado = hs_ley_dependencia_grado;
    }

    public Boolean getHs_recibe_servicio_administracion() {
        return hs_recibe_servicio_administracion;
    }

    public void setHs_recibe_servicio_administracion(Boolean hs_recibe_servicio_administracion) {
        this.hs_recibe_servicio_administracion = hs_recibe_servicio_administracion;
    }

    public String getHs_patologias() {
        return hs_patologias;
    }

    public void setHs_patologias(String hs_patologias) {
        this.hs_patologias = hs_patologias;
    }

    public Boolean getHs_diabetico() {
        return hs_diabetico;
    }

    public void setHs_diabetico(Boolean hs_diabetico) {
        this.hs_diabetico = hs_diabetico;
    }

    public Boolean getHs_hipertenso() {
        return hs_hipertenso;
    }

    public void setHs_hipertenso(Boolean hs_hipertenso) {
        this.hs_hipertenso = hs_hipertenso;
    }

    public Boolean getHs_alimenta_bien() {
        return hs_alimenta_bien;
    }

    public void setHs_alimenta_bien(Boolean hs_alimenta_bien) {
        this.hs_alimenta_bien = hs_alimenta_bien;
    }

    public Boolean getHs_duerme_bien() {
        return hs_duerme_bien;
    }

    public void setHs_duerme_bien(Boolean hs_duerme_bien) {
        this.hs_duerme_bien = hs_duerme_bien;
    }

    public Boolean getHs_fuma_bebe() {
        return hs_fuma_bebe;
    }

    public void setHs_fuma_bebe(Boolean hs_fuma_bebe) {
        this.hs_fuma_bebe = hs_fuma_bebe;
    }

    public Boolean getHs_drogas() {
        return hs_drogas;
    }

    public void setHs_drogas(Boolean hs_drogas) {
        this.hs_drogas = hs_drogas;
    }

    public String getHs_drogas_text() {
        return hs_drogas_text;
    }

    public void setHs_drogas_text(String hs_drogas_text) {
        this.hs_drogas_text = hs_drogas_text;
    }

    public String getHs_valoracion_salud() {
        return hs_valoracion_salud;
    }

    public void setHs_valoracion_salud(String hs_valoracion_salud) {
        this.hs_valoracion_salud = hs_valoracion_salud;
    }

    public Boolean getHs_fam_dificultades_convivencia() {
        return hs_fam_dificultades_convivencia;
    }

    public void setHs_fam_dificultades_convivencia(Boolean hs_fam_dificultades_convivencia) {
        this.hs_fam_dificultades_convivencia = hs_fam_dificultades_convivencia;
    }

    public Boolean getHs_fam_dificultades_economicas() {
        return hs_fam_dificultades_economicas;
    }

    public void setHs_fam_dificultades_economicas(Boolean hs_fam_dificultades_economicas) {
        this.hs_fam_dificultades_economicas = hs_fam_dificultades_economicas;
    }

    public Boolean getHs_fam_dificultad_cuidados() {
        return hs_fam_dificultad_cuidados;
    }

    public void setHs_fam_dificultad_cuidados(Boolean hs_fam_dificultad_cuidados) {
        this.hs_fam_dificultad_cuidados = hs_fam_dificultad_cuidados;
    }

    public Boolean getHs_fam_sin_apoyo() {
        return hs_fam_sin_apoyo;
    }

    public void setHs_fam_sin_apoyo(Boolean hs_fam_sin_apoyo) {
        this.hs_fam_sin_apoyo = hs_fam_sin_apoyo;
    }

    public Boolean getHs_fam_agotamiento_cuidador() {
        return hs_fam_agotamiento_cuidador;
    }

    public void setHs_fam_agotamiento_cuidador(Boolean hs_fam_agotamiento_cuidador) {
        this.hs_fam_agotamiento_cuidador = hs_fam_agotamiento_cuidador;
    }

    public Boolean getHs_viv_sin_domicilio() {
        return hs_viv_sin_domicilio;
    }

    public void setHs_viv_sin_domicilio(Boolean hs_viv_sin_domicilio) {
        this.hs_viv_sin_domicilio = hs_viv_sin_domicilio;
    }

    public Boolean getHs_viv_ruinas() {
        return hs_viv_ruinas;
    }

    public void setHs_viv_ruinas(Boolean hs_viv_ruinas) {
        this.hs_viv_ruinas = hs_viv_ruinas;
    }

    public Boolean getHs_viv_barreras() {
        return hs_viv_barreras;
    }

    public void setHs_viv_barreras(Boolean hs_viv_barreras) {
        this.hs_viv_barreras = hs_viv_barreras;
    }

    public Boolean getHs_viv_inhabitabilidad() {
        return hs_viv_inhabitabilidad;
    }

    public void setHs_viv_inhabitabilidad(Boolean hs_viv_inhabitabilidad) {
        this.hs_viv_inhabitabilidad = hs_viv_inhabitabilidad;
    }

    public Boolean getHs_alquiler_elevado() {
        return hs_alquiler_elevado;
    }

    public void setHs_alquiler_elevado(Boolean hs_alquiler_elevado) {
        this.hs_alquiler_elevado = hs_alquiler_elevado;
    }

    public Boolean getHs_escaleras_exteriores() {
        return hs_escaleras_exteriores;
    }

    public void setHs_escaleras_exteriores(Boolean hs_escaleras_exteriores) {
        this.hs_escaleras_exteriores = hs_escaleras_exteriores;
    }

    public Boolean getHs_escaleras_interiores() {
        return hs_escaleras_interiores;
    }

    public void setHs_escaleras_interiores(Boolean hs_escaleras_interiores) {
        this.hs_escaleras_interiores = hs_escaleras_interiores;
    }

    public Boolean getHs_banera() {
        return hs_banera;
    }

    public void setHs_banera(Boolean hs_banera) {
        this.hs_banera = hs_banera;
    }

    public Boolean getHs_alfombras() {
        return hs_alfombras;
    }

    public void setHs_alfombras(Boolean hs_alfombras) {
        this.hs_alfombras = hs_alfombras;
    }

    public Boolean getHs_otros() {
        return hs_otros;
    }

    public void setHs_otros(Boolean hs_otros) {
        this.hs_otros = hs_otros;
    }

    public String getHs_otros_text() {
        return hs_otros_text;
    }

    public String getHs_nombre1() {
        return hs_nombre1;
    }

    public void setHs_nombre1(String hs_nombre1) {
        this.hs_nombre1 = hs_nombre1;
    }

    public String getHs_parentesco1() {
        return hs_parentesco1;
    }

    public void setHs_parentesco1(String hs_parentesco1) {
        this.hs_parentesco1 = hs_parentesco1;
    }

    public Integer getHs_edad1() {
        return hs_edad1;
    }

    public void setHs_edad1(Integer hs_edad1) {
        this.hs_edad1 = hs_edad1;
    }

    public String getHs_profesion1() {
        return hs_profesion1;
    }

    public void setHs_profesion1(String hs_profesion1) {
        this.hs_profesion1 = hs_profesion1;
    }

    public String getHs_nombre2() {
        return hs_nombre2;
    }

    public void setHs_nombre2(String hs_nombre2) {
        this.hs_nombre2 = hs_nombre2;
    }

    public String getHs_parentesco2() {
        return hs_parentesco2;
    }

    public void setHs_parentesco2(String hs_parentesco2) {
        this.hs_parentesco2 = hs_parentesco2;
    }

    public Integer getHs_edad2() {
        return hs_edad2;
    }

    public void setHs_edad2(Integer hs_edad2) {
        this.hs_edad2 = hs_edad2;
    }

    public String getHs_profesion2() {
        return hs_profesion2;
    }

    public void setHs_profesion2(String hs_profesion2) {
        this.hs_profesion2 = hs_profesion2;
    }

    public String getHs_nombre3() {
        return hs_nombre3;
    }

    public void setHs_nombre3(String hs_nombre3) {
        this.hs_nombre3 = hs_nombre3;
    }

    public String getHs_parentesco3() {
        return hs_parentesco3;
    }

    public void setHs_parentesco3(String hs_parentesco3) {
        this.hs_parentesco3 = hs_parentesco3;
    }

    public Integer getHs_edad3() {
        return hs_edad3;
    }

    public void setHs_edad3(Integer hs_edad3) {
        this.hs_edad3 = hs_edad3;
    }

    public String getHs_profesion3() {
        return hs_profesion3;
    }

    public void setHs_profesion3(String hs_profesion3) {
        this.hs_profesion3 = hs_profesion3;
    }

    public String getHs_nombre4() {
        return hs_nombre4;
    }

    public void setHs_nombre4(String hs_nombre4) {
        this.hs_nombre4 = hs_nombre4;
    }

    public String getHs_parentesco4() {
        return hs_parentesco4;
    }

    public void setHs_parentesco4(String hs_parentesco4) {
        this.hs_parentesco4 = hs_parentesco4;
    }

    public Integer getHs_edad4() {
        return hs_edad4;
    }

    public void setHs_edad4(Integer hs_edad4) {
        this.hs_edad4 = hs_edad4;
    }

    public String getHs_profesion4() {
        return hs_profesion4;
    }

    public void setHs_profesion4(String hs_profesion4) {
        this.hs_profesion4 = hs_profesion4;
    }

    public Boolean getHs_tiene_pareja() {
        return hs_tiene_pareja;
    }

    public void setHs_tiene_pareja(Boolean hs_tiene_pareja) {
        this.hs_tiene_pareja = hs_tiene_pareja;
    }

    public String getHs_relacion_pareja() {
        return hs_relacion_pareja;
    }

    public void setHs_relacion_pareja(String hs_relacion_pareja) {
        this.hs_relacion_pareja = hs_relacion_pareja;
    }

    public Boolean getHs_tiene_hijos() {
        return hs_tiene_hijos;
    }

    public void setHs_tiene_hijos(Boolean hs_tiene_hijos) {
        this.hs_tiene_hijos = hs_tiene_hijos;
    }

    public String getHs_relacion_hijos() {
        return hs_relacion_hijos;
    }

    public void setHs_relacion_hijos(String hs_relacion_hijos) {
        this.hs_relacion_hijos = hs_relacion_hijos;
    }

    public Boolean getHs_tiene_hermanos() {
        return hs_tiene_hermanos;
    }

    public void setHs_tiene_hermanos(Boolean hs_tiene_hermanos) {
        this.hs_tiene_hermanos = hs_tiene_hermanos;
    }

    public String getHs_relacion_hermanos() {
        return hs_relacion_hermanos;
    }

    public void setHs_relacion_hermanos(String hs_relacion_hermanos) {
        this.hs_relacion_hermanos = hs_relacion_hermanos;
    }

    public Boolean getHs_visitas_familiares() {
        return hs_visitas_familiares;
    }

    public void setHs_visitas_familiares(Boolean hs_visitas_familiares) {
        this.hs_visitas_familiares = hs_visitas_familiares;
    }

    public String getHs_visitas_cuanto() {
        return hs_visitas_cuanto;
    }

    public void setHs_visitas_cuanto(String hs_visitas_cuanto) {
        this.hs_visitas_cuanto = hs_visitas_cuanto;
    }

    public Boolean getHs_apoyo_amigos() {
        return hs_apoyo_amigos;
    }

    public void setHs_apoyo_amigos(Boolean hs_apoyo_amigos) {
        this.hs_apoyo_amigos = hs_apoyo_amigos;
    }

    public Boolean getHs_relacion_familia() {
        return hs_relacion_familia;
    }

    public void setHs_relacion_familia(Boolean hs_relacion_familia) {
        this.hs_relacion_familia = hs_relacion_familia;
    }

    public Boolean getHs_acude_otras() {
        return hs_acude_otras;
    }

    public void setHs_acude_otras(Boolean hs_acude_otras) {
        this.hs_acude_otras = hs_acude_otras;
    }

    public Boolean getHs_recibe_pension() {
        return hs_recibe_pension;
    }

    public void setHs_recibe_pension(Boolean hs_recibe_pension) {
        this.hs_recibe_pension = hs_recibe_pension;
    }

    public Double getHs_cuantia_pension() {
        return hs_cuantia_pension;
    }

    public void setHs_cuantia_pension(Double hs_cuantia_pension) {
        this.hs_cuantia_pension = hs_cuantia_pension;
    }

    public String getHs_otros_ingresos() {
        return hs_otros_ingresos;
    }

    public void setHs_otros_ingresos(String hs_otros_ingresos) {
        this.hs_otros_ingresos = hs_otros_ingresos;
    }

    public String getHs_otros_recursos() {
        return hs_otros_recursos;
    }

    public void setHs_otros_recursos(String hs_otros_recursos) {
        this.hs_otros_recursos = hs_otros_recursos;
    }

    public String getHs_valoracion_profesional() {
        return hs_valoracion_profesional;
    }

    public void setHs_valoracion_profesional(String hs_valoracion_profesional) {
        this.hs_valoracion_profesional = hs_valoracion_profesional;
    }

    public String getHs_observaciones() {
        return hs_observaciones;
    }

    public void setHs_observaciones(String hs_observaciones) {
        this.hs_observaciones = hs_observaciones;
    }

    public Boolean getHs_uc_solo() {
        return hs_uc_solo;
    }

    public void setHs_uc_solo(Boolean hs_uc_solo) {
        this.hs_uc_solo = hs_uc_solo;
    }

    public Boolean getHs_uc_conyuge() {
        return hs_uc_conyuge;
    }

    public void setHs_uc_conyuge(Boolean hs_uc_conyuge) {
        this.hs_uc_conyuge = hs_uc_conyuge;
    }

    public Boolean getHs_uc_hijos() {
        return hs_uc_hijos;
    }

    public void setHs_uc_hijos(Boolean hs_uc_hijos) {
        this.hs_uc_hijos = hs_uc_hijos;
    }

    public Boolean getHs_uc_other() {
        return hs_uc_other;
    }

    public void setHs_uc_other(Boolean hs_uc_other) {
        this.hs_uc_other = hs_uc_other;
    }

    public String getHs_uc_other_text() {
        return hs_uc_other_text;
    }

    public void setHs_uc_other_text(String hs_uc_other_text) {
        this.hs_uc_other_text = hs_uc_other_text;
    }

    public String getHs_otra_prestacion() {
        return hs_otra_prestacion;
    }

    public void setHs_otra_prestacion(String hs_otra_prestacion) {
        this.hs_otra_prestacion = hs_otra_prestacion;
    }

    public String getHs_url() {
        return hs_url;
    }

    public void setHs_url(String hs_url) {
        this.hs_url = hs_url;
    }

    public void setHs_otros_text(String hs_otros_text) {
        this.hs_otros_text = hs_otros_text;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getIs_tiempo_conoce_usuario() {
        return is_tiempo_conoce_usuario;
    }

    public void setIs_tiempo_conoce_usuario(String is_tiempo_conoce_usuario) {
        this.is_tiempo_conoce_usuario = is_tiempo_conoce_usuario;
    }

    public String getIs_servicios_prestados() {
        return is_servicios_prestados;
    }

    public void setIs_servicios_prestados(String is_servicios_prestados) {
        this.is_servicios_prestados = is_servicios_prestados;
    }

    public String getIs_como_adaptado() {
        return is_como_adaptado;
    }

    public void setIs_como_adaptado(String is_como_adaptado) {
        this.is_como_adaptado = is_como_adaptado;
    }

    public Boolean getIs_acudio_voluntad_propia() {
        return is_acudio_voluntad_propia;
    }

    public void setIs_acudio_voluntad_propia(Boolean is_acudio_voluntad_propia) {
        this.is_acudio_voluntad_propia = is_acudio_voluntad_propia;
    }

    public String getIs_quien_influyo_decision() {
        return is_quien_influyo_decision;
    }

    public void setIs_quien_influyo_decision(String is_quien_influyo_decision) {
        this.is_quien_influyo_decision = is_quien_influyo_decision;
    }

    public String getIs_que_actividades() {
        return is_que_actividades;
    }

    public void setIs_que_actividades(String is_que_actividades) {
        this.is_que_actividades = is_que_actividades;
    }

    public String getIs_como_relaciona() {
        return is_como_relaciona;
    }

    public void setIs_como_relaciona(String is_como_relaciona) {
        this.is_como_relaciona = is_como_relaciona;
    }

    public String getIs_como_pasa_dia() {
        return is_como_pasa_dia;
    }

    public void setIs_como_pasa_dia(String is_como_pasa_dia) {
        this.is_como_pasa_dia = is_como_pasa_dia;
    }

    public Boolean getIs_problemas_psico() {
        return is_problemas_psico;
    }

    public void setIs_problemas_psico(Boolean is_problemas_psico) {
        this.is_problemas_psico = is_problemas_psico;
    }

    public String getIs_problemas_psico_text() {
        return is_problemas_psico_text;
    }

    public void setIs_problemas_psico_text(String is_problemas_psico_text) {
        this.is_problemas_psico_text = is_problemas_psico_text;
    }

    public Boolean getIs_familia_estru() {
        return is_familia_estru;
    }

    public void setIs_familia_estru(Boolean is_familia_estru) {
        this.is_familia_estru = is_familia_estru;
    }

    public String getIs_familia_estru_text() {
        return is_familia_estru_text;
    }

    public void setIs_familia_estru_text(String is_familia_estru_text) {
        this.is_familia_estru_text = is_familia_estru_text;
    }

    public Boolean getIs_recibe_ingresos_actividad_laboral() {
        return is_recibe_ingresos_actividad_laboral;
    }

    public void setIs_recibe_ingresos_actividad_laboral(Boolean is_recibe_ingresos_actividad_laboral) {
        this.is_recibe_ingresos_actividad_laboral = is_recibe_ingresos_actividad_laboral;
    }

    public Boolean getIs_esta_buscando_empleo() {
        return is_esta_buscando_empleo;
    }

    public void setIs_esta_buscando_empleo(Boolean is_esta_buscando_empleo) {
        this.is_esta_buscando_empleo = is_esta_buscando_empleo;
    }

    public String getIs_vive_en() {
        return is_vive_en;
    }

    public void setIs_vive_en(String is_vive_en) {
        this.is_vive_en = is_vive_en;
    }

    public Boolean getIs_cubiertas_necesidades_diarias() {
        return is_cubiertas_necesidades_diarias;
    }

    public void setIs_cubiertas_necesidades_diarias(Boolean is_cubiertas_necesidades_diarias) {
        this.is_cubiertas_necesidades_diarias = is_cubiertas_necesidades_diarias;
    }

    public String getIs_valoracion_profesional() {
        return is_valoracion_profesional;
    }

    public void setIs_valoracion_profesional(String is_valoracion_profesional) {
        this.is_valoracion_profesional = is_valoracion_profesional;
    }

    public String getIs_propuesta() {
        return is_propuesta;
    }

    public void setIs_propuesta(String is_propuesta) {
        this.is_propuesta = is_propuesta;
    }

    public String getIs_url() {
        return is_url;
    }

    public void setIs_url(String is_url) {
        this.is_url = is_url;
    }

    public Boolean getIs_recibe_tratamiento() {
        return is_recibe_tratamiento;
    }

    public void setIs_recibe_tratamiento(Boolean is_recibe_tratamiento) {
        this.is_recibe_tratamiento = is_recibe_tratamiento;
    }

    public String getIs_recibe_tratamiento_text() {
        return is_recibe_tratamiento_text;
    }

    public void setIs_recibe_tratamiento_text(String is_recibe_tratamiento_text) {
        this.is_recibe_tratamiento_text = is_recibe_tratamiento_text;
    }

    public LocalDate getIns_fecha_informe() {
        return ins_fecha_informe;
    }

    public void setIns_fecha_informe(LocalDate ins_fecha_informe) {
        this.ins_fecha_informe = ins_fecha_informe;
    }

    public String getIns_motivo_consulta() {
        return ins_motivo_consulta;
    }

    public void setIns_motivo_consulta(String ins_motivo_consulta) {
        this.ins_motivo_consulta = ins_motivo_consulta;
    }

    public String getIns_antecedentes() {
        return ins_antecedentes;
    }

    public void setIns_antecedentes(String ins_antecedentes) {
        this.ins_antecedentes = ins_antecedentes;
    }

    public String getIns_diagnostico() {
        return ins_diagnostico;
    }

    public void setIns_diagnostico(String ins_diagnostico) {
        this.ins_diagnostico = ins_diagnostico;
    }

    public String getIns_texto_pre_puntuaciones() {
        return ins_texto_pre_puntuaciones;
    }

    public void setIns_texto_pre_puntuaciones(String ins_texto_pre_puntuaciones) {
        this.ins_texto_pre_puntuaciones = ins_texto_pre_puntuaciones;
    }

    public LocalDate getIns_fecha1() {
        return ins_fecha1;
    }

    public void setIns_fecha1(LocalDate ins_fecha1) {
        this.ins_fecha1 = ins_fecha1;
    }

    public LocalDate getIns_fecha2() {
        return ins_fecha2;
    }

    public void setIns_fecha2(LocalDate ins_fecha2) {
        this.ins_fecha2 = ins_fecha2;
    }

    public LocalDate getIns_fecha3() {
        return ins_fecha3;
    }

    public void setIns_fecha3(LocalDate ins_fecha3) {
        this.ins_fecha3 = ins_fecha3;
    }

    public LocalDate getIns_fecha4() {
        return ins_fecha4;
    }

    public void setIns_fecha4(LocalDate ins_fecha4) {
        this.ins_fecha4 = ins_fecha4;
    }

    public Double getIns_orientacion1() {
        return ins_orientacion1;
    }

    public void setIns_orientacion1(Double ins_orientacion1) {
        this.ins_orientacion1 = ins_orientacion1;
    }

    public Double getIns_orientacion2() {
        return ins_orientacion2;
    }

    public void setIns_orientacion2(Double ins_orientacion2) {
        this.ins_orientacion2 = ins_orientacion2;
    }

    public Double getIns_orientacion3() {
        return ins_orientacion3;
    }

    public void setIns_orientacion3(Double ins_orientacion3) {
        this.ins_orientacion3 = ins_orientacion3;
    }

    public Double getIns_orientacion4() {
        return ins_orientacion4;
    }

    public void setIns_orientacion4(Double ins_orientacion4) {
        this.ins_orientacion4 = ins_orientacion4;
    }

    public Double getIns_lenguaje1() {
        return ins_lenguaje1;
    }

    public void setIns_lenguaje1(Double ins_lenguaje1) {
        this.ins_lenguaje1 = ins_lenguaje1;
    }

    public Double getIns_lenguaje2() {
        return ins_lenguaje2;
    }

    public void setIns_lenguaje2(Double ins_lenguaje2) {
        this.ins_lenguaje2 = ins_lenguaje2;
    }

    public Double getIns_lenguaje3() {
        return ins_lenguaje3;
    }

    public void setIns_lenguaje3(Double ins_lenguaje3) {
        this.ins_lenguaje3 = ins_lenguaje3;
    }

    public Double getIns_lenguaje4() {
        return ins_lenguaje4;
    }

    public void setIns_lenguaje4(Double ins_lenguaje4) {
        this.ins_lenguaje4 = ins_lenguaje4;
    }

    public Double getIns_memoria1() {
        return ins_memoria1;
    }

    public void setIns_memoria1(Double ins_memoria1) {
        this.ins_memoria1 = ins_memoria1;
    }

    public Double getIns_memoria2() {
        return ins_memoria2;
    }

    public void setIns_memoria2(Double ins_memoria2) {
        this.ins_memoria2 = ins_memoria2;
    }

    public Double getIns_memoria3() {
        return ins_memoria3;
    }

    public void setIns_memoria3(Double ins_memoria3) {
        this.ins_memoria3 = ins_memoria3;
    }

    public Double getIns_memoria4() {
        return ins_memoria4;
    }

    public void setIns_memoria4(Double ins_memoria4) {
        this.ins_memoria4 = ins_memoria4;
    }

    public Double getIns_atencalculo1() {
        return ins_atencalculo1;
    }

    public void setIns_atencalculo1(Double ins_atencalculo1) {
        this.ins_atencalculo1 = ins_atencalculo1;
    }

    public Double getIns_atencalculo2() {
        return ins_atencalculo2;
    }

    public void setIns_atencalculo2(Double ins_atencalculo2) {
        this.ins_atencalculo2 = ins_atencalculo2;
    }

    public Double getIns_atencalculo3() {
        return ins_atencalculo3;
    }

    public void setIns_atencalculo3(Double ins_atencalculo3) {
        this.ins_atencalculo3 = ins_atencalculo3;
    }

    public Double getIns_atencalculo4() {
        return ins_atencalculo4;
    }

    public void setIns_atencalculo4(Double ins_atencalculo4) {
        this.ins_atencalculo4 = ins_atencalculo4;
    }

    public Double getIns_praxis1() {
        return ins_praxis1;
    }

    public void setIns_praxis1(Double ins_praxis1) {
        this.ins_praxis1 = ins_praxis1;
    }

    public Double getIns_praxis2() {
        return ins_praxis2;
    }

    public void setIns_praxis2(Double ins_praxis2) {
        this.ins_praxis2 = ins_praxis2;
    }

    public Double getIns_praxis3() {
        return ins_praxis3;
    }

    public void setIns_praxis3(Double ins_praxis3) {
        this.ins_praxis3 = ins_praxis3;
    }

    public Double getIns_praxis4() {
        return ins_praxis4;
    }

    public void setIns_praxis4(Double ins_praxis4) {
        this.ins_praxis4 = ins_praxis4;
    }

    public Double getIns_pensabstracto1() {
        return ins_pensabstracto1;
    }

    public void setIns_pensabstracto1(Double ins_pensabstracto1) {
        this.ins_pensabstracto1 = ins_pensabstracto1;
    }

    public Double getIns_pensabstracto2() {
        return ins_pensabstracto2;
    }

    public void setIns_pensabstracto2(Double ins_pensabstracto2) {
        this.ins_pensabstracto2 = ins_pensabstracto2;
    }

    public Double getIns_pensabstracto3() {
        return ins_pensabstracto3;
    }

    public void setIns_pensabstracto3(Double ins_pensabstracto3) {
        this.ins_pensabstracto3 = ins_pensabstracto3;
    }

    public Double getIns_pensabstracto4() {
        return ins_pensabstracto4;
    }

    public void setIns_pensabstracto4(Double ins_pensabstracto4) {
        this.ins_pensabstracto4 = ins_pensabstracto4;
    }

    public Double getIns_percecpcion1() {
        return ins_percecpcion1;
    }

    public void setIns_percecpcion1(Double ins_percecpcion1) {
        this.ins_percecpcion1 = ins_percecpcion1;
    }

    public Double getIns_percecpcion2() {
        return ins_percecpcion2;
    }

    public void setIns_percecpcion2(Double ins_percecpcion2) {
        this.ins_percecpcion2 = ins_percecpcion2;
    }

    public Double getIns_percecpcion3() {
        return ins_percecpcion3;
    }

    public void setIns_percecpcion3(Double ins_percecpcion3) {
        this.ins_percecpcion3 = ins_percecpcion3;
    }

    public Double getIns_percecpcion4() {
        return ins_percecpcion4;
    }

    public void setIns_percecpcion4(Double ins_percecpcion4) {
        this.ins_percecpcion4 = ins_percecpcion4;
    }

    public Double getIns_total1() {
        return ins_total1;
    }

    public void setIns_total1(Double ins_total1) {
        this.ins_total1 = ins_total1;
    }

    public Double getIns_total2() {
        return ins_total2;
    }

    public void setIns_total2(Double ins_total2) {
        this.ins_total2 = ins_total2;
    }

    public Double getIns_total3() {
        return ins_total3;
    }

    public void setIns_total3(Double ins_total3) {
        this.ins_total3 = ins_total3;
    }

    public Double getIns_total4() {
        return ins_total4;
    }

    public void setIns_total4(Double ins_total4) {
        this.ins_total4 = ins_total4;
    }

    public LocalDate getIns_fecha_mms1() {
        return ins_fecha_mms1;
    }

    public void setIns_fecha_mms1(LocalDate ins_fecha_mms1) {
        this.ins_fecha_mms1 = ins_fecha_mms1;
    }

    public LocalDate getIns_fecha_mms2() {
        return ins_fecha_mms2;
    }

    public void setIns_fecha_mms2(LocalDate ins_fecha_mms2) {
        this.ins_fecha_mms2 = ins_fecha_mms2;
    }

    public LocalDate getIns_fecha_mms3() {
        return ins_fecha_mms3;
    }

    public void setIns_fecha_mms3(LocalDate ins_fecha_mms3) {
        this.ins_fecha_mms3 = ins_fecha_mms3;
    }

    public LocalDate getIns_fecha_mms4() {
        return ins_fecha_mms4;
    }

    public void setIns_fecha_mms4(LocalDate ins_fecha_mms4) {
        this.ins_fecha_mms4 = ins_fecha_mms4;
    }

    public Double getIns_mmse1() {
        return ins_mmse1;
    }

    public void setIns_mmse1(Double ins_mmse1) {
        this.ins_mmse1 = ins_mmse1;
    }

    public Double getIns_mmse2() {
        return ins_mmse2;
    }

    public void setIns_mmse2(Double ins_mmse2) {
        this.ins_mmse2 = ins_mmse2;
    }

    public Double getIns_mmse3() {
        return ins_mmse3;
    }

    public void setIns_mmse3(Double ins_mmse3) {
        this.ins_mmse3 = ins_mmse3;
    }

    public Double getIns_mmse4() {
        return ins_mmse4;
    }

    public void setIns_mmse4(Double ins_mmse4) {
        this.ins_mmse4 = ins_mmse4;
    }

    public String getIns_texto_post_puntuaciones() {
        return ins_texto_post_puntuaciones;
    }

    public void setIns_texto_post_puntuaciones(String ins_texto_post_puntuaciones) {
        this.ins_texto_post_puntuaciones = ins_texto_post_puntuaciones;
    }

    public LocalDate getIns_fecha_ind1() {
        return ins_fecha_ind1;
    }

    public void setIns_fecha_ind1(LocalDate ins_fecha_ind1) {
        this.ins_fecha_ind1 = ins_fecha_ind1;
    }

    public LocalDate getIns_fecha_ind2() {
        return ins_fecha_ind2;
    }

    public void setIns_fecha_ind2(LocalDate ins_fecha_ind2) {
        this.ins_fecha_ind2 = ins_fecha_ind2;
    }

    public LocalDate getIns_fecha_ind3() {
        return ins_fecha_ind3;
    }

    public void setIns_fecha_ind3(LocalDate ins_fecha_ind3) {
        this.ins_fecha_ind3 = ins_fecha_ind3;
    }

    public Double getIns_indbathel1() {
        return ins_indbathel1;
    }

    public void setIns_indbathel1(Double ins_indbathel1) {
        this.ins_indbathel1 = ins_indbathel1;
    }

    public Double getIns_indbathel2() {
        return ins_indbathel2;
    }

    public void setIns_indbathel2(Double ins_indbathel2) {
        this.ins_indbathel2 = ins_indbathel2;
    }

    public Double getIns_indbathel3() {
        return ins_indbathel3;
    }

    public void setIns_indbathel3(Double ins_indbathel3) {
        this.ins_indbathel3 = ins_indbathel3;
    }

    public Double getIns_indlawton1() {
        return ins_indlawton1;
    }

    public void setIns_indlawton1(Double ins_indlawton1) {
        this.ins_indlawton1 = ins_indlawton1;
    }

    public Double getIns_indlawton2() {
        return ins_indlawton2;
    }

    public void setIns_indlawton2(Double ins_indlawton2) {
        this.ins_indlawton2 = ins_indlawton2;
    }

    public Double getIns_indlawton3() {
        return ins_indlawton3;
    }

    public void setIns_indlawton3(Double ins_indlawton3) {
        this.ins_indlawton3 = ins_indlawton3;
    }

    public String getIns_texto_eval_conductual() {
        return ins_texto_eval_conductual;
    }

    public void setIns_texto_eval_conductual(String ins_texto_eval_conductual) {
        this.ins_texto_eval_conductual = ins_texto_eval_conductual;
    }

    public String getIns_texto_conclusion() {
        return ins_texto_conclusion;
    }

    public void setIns_texto_conclusion(String ins_texto_conclusion) {
        this.ins_texto_conclusion = ins_texto_conclusion;
    }

    public String getIns_url() {
        return ins_url;
    }

    public void setIns_url(String ins_url) {
        this.ins_url = ins_url;
    }

    public String getOcupacionAnterior() {
        return ocupacionAnterior;
    }

    public void setOcupacionAnterior(String ocupacionAnterior) {
        this.ocupacionAnterior = ocupacionAnterior;
    }


    public String getIps_url() {
        return ips_url;
    }

    public void setIps_url(String ips_url) {
        this.ips_url = ips_url;
    }

    public LocalDate getIps_fecha_informe() {
        return ips_fecha_informe;
    }

    public void setIps_fecha_informe(LocalDate ips_fecha_informe) {
        this.ips_fecha_informe = ips_fecha_informe;
    }

    public String getIps_sanitarios() {
        return ips_sanitarios;
    }

    public void setIps_sanitarios(String ips_sanitarios) {
        this.ips_sanitarios = ips_sanitarios;
    }

    public String getIps_sociofamiliar() {
        return ips_sociofamiliar;
    }

    public void setIps_sociofamiliar(String ips_sociofamiliar) {
        this.ips_sociofamiliar = ips_sociofamiliar;
    }

    public String getIps_evalcognitiva() {
        return ips_evalcognitiva;
    }

    public void setIps_evalcognitiva(String ips_evalcognitiva) {
        this.ips_evalcognitiva = ips_evalcognitiva;
    }

    public String getIps_evalconductual() {
        return ips_evalconductual;
    }

    public void setIps_evalconductual(String ips_evalconductual) {
        this.ips_evalconductual = ips_evalconductual;
    }

    public String getIps_evalfuncional() {
        return ips_evalfuncional;
    }

    public void setIps_evalfuncional(String ips_evalfuncional) {
        this.ips_evalfuncional = ips_evalfuncional;
    }

    public String getIps_situacioneconomica() {
        return ips_situacioneconomica;
    }

    public void setIps_situacioneconomica(String ips_situacioneconomica) {
        this.ips_situacioneconomica = ips_situacioneconomica;
    }

    public String getIps_observaciones() {
        return ips_observaciones;
    }

    public void setIps_observaciones(String ips_observaciones) {
        this.ips_observaciones = ips_observaciones;
    }

    public String getNum_contrato() {
        return num_contrato;
    }

    public void setNum_contrato(String num_contrato) {
        this.num_contrato = num_contrato;
    }
}
