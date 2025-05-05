
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment as ENV} from '../../environments/environment'; 
import { PatientDTO } from 'src/app/shared/models/models';


@Injectable({
  providedIn: 'root'
})
export class PatientsService {



  constructor(private http: HttpClient,) { }

  
  getPatients(
    page: number,
    size: number,
    idpatient?: string,
    name_surnames?: string,
    documentid?: string,
    status?: string,
    order?: string,
    orderasc?: string,
    gender?: string,
    servicetype?: string,
    transportservice?: boolean,
    tallerpsico?: boolean,
    comedorservice?: boolean,
    ayudadomicilioservice?: boolean,
    hs_ley_dependencia_solicitada?: boolean
  ) {
    let url = ENV.url.patients + `/getPatients?page=${page}&size=${size}`;
  
    if(idpatient) url += `&idpatient=${encodeURIComponent(idpatient || '')}`;
    if (name_surnames) url += `&name_surnames=${encodeURIComponent(name_surnames)}`;
    if (documentid) url += `&documentid=${encodeURIComponent(documentid)}`;
    if (status) url += `&status=${encodeURIComponent(status)}`;
    if (order) url += `&order=${encodeURIComponent(order)}`;
    if (orderasc) url += `&orderasc=${encodeURIComponent(orderasc)}`;
    if (gender) url += `&gender=${encodeURIComponent(gender)}`;
    if (servicetype) url += `&servicetype=${encodeURIComponent(servicetype)}`;
    if (transportservice !== undefined) url += `&transportservice=${transportservice}`;
    if (tallerpsico !== undefined) url += `&tallerpsico=${tallerpsico}`;
    if (comedorservice !== undefined) url += `&comedorservice=${comedorservice}`;
    if (ayudadomicilioservice !== undefined) url += `&ayudadomicilioservice=${ayudadomicilioservice}`;
    if (hs_ley_dependencia_solicitada !== undefined) url += `&hs_ley_dependencia_solicitada=${hs_ley_dependencia_solicitada}`;
  
    return this.http.get<any>(url, {});
  }
  

  getAllRelatives() {
    let url = ENV.url.patients + `/getAllRelatives`;
    return this.http.get<any>(url, {});
  }

  savePatient(thePatient: PatientDTO) 
  {
    const form = new FormData();
    if (thePatient.id && thePatient.id != '') form.append('id', thePatient.id);

    if (thePatient.name && thePatient.name != '') form.append('name', thePatient.name);
    if (thePatient.surname1 && thePatient.surname1 != '') form.append('surname1', thePatient.surname1);
    if (thePatient.surname2 && thePatient.surname2 != '') form.append('surname2', thePatient.surname2);
    if (thePatient.birthdate) form.append('birthdate', this.formatDate2(thePatient.birthdate));
    if (thePatient.gender && thePatient.gender != '') form.append('gender', thePatient.gender);
    if (thePatient.documentid && thePatient.documentid != '') form.append('documentid', thePatient.documentid);
    if (thePatient.documenttype && thePatient.documenttype != '') form.append('documenttype', thePatient.documenttype);
    
    if (thePatient.idrelative && thePatient.idrelative != '') form.append('idrelative', thePatient.idrelative);
    if (thePatient.relativerelation && thePatient.relativerelation != '') form.append('relativerelation', thePatient.relativerelation);
    
    if (thePatient.postaladdress && thePatient.postaladdress != '') form.append('postaladdress', thePatient.postaladdress);
    if (thePatient.idcity) form.append('idcity', thePatient.idcity.toString());
    if (thePatient.idstate) form.append('idstate', thePatient.idstate.toString());
    if (thePatient.idcountry) form.append('idcountry', thePatient.idcountry.toString());
    if (thePatient.postalcode && thePatient.postalcode != '') form.append('postalcode', thePatient.postalcode);
    
    if (thePatient.num_contrato && thePatient.num_contrato != '') form.append('num_contrato', thePatient.num_contrato);
    if (thePatient.fs_num_expediente && thePatient.fs_num_expediente != '') form.append('fs_num_expediente', thePatient.fs_num_expediente);
    if (thePatient.fs_fecha_inscripcion) form.append('fs_fecha_inscripcion', this.formatDate2(thePatient.fs_fecha_inscripcion));
    if (thePatient.fs_num_ss != null) form.append('fs_num_ss', thePatient.fs_num_ss == null ? "" : thePatient.fs_num_ss);
    if (thePatient.fs_estado_civil && thePatient.fs_estado_civil != '') form.append('fs_estado_civil', thePatient.fs_estado_civil);
    if (thePatient.phone && thePatient.phone != '') form.append('phone', thePatient.phone);
    
    if (thePatient.language && thePatient.language != '') form.append('language', thePatient.language);
    
    if (thePatient.servicetype && thePatient.servicetype != '') form.append('servicetype', thePatient.servicetype);
    if (thePatient.tallerpsico) form.append('tallerpsico', thePatient.tallerpsico.toString());
    
    if (thePatient.transportservice) form.append('transportservice', thePatient.transportservice.toString());
    if (thePatient.comedorservice) form.append('comedorservice', thePatient.comedorservice.toString());
    if (thePatient.ayudadomicilioservice) form.append('ayudadomicilioservice', thePatient.ayudadomicilioservice.toString());
    if (thePatient.duchaservice) form.append('duchaservice', thePatient.duchaservice.toString());
    
    if (thePatient.transportservice_text && thePatient.transportservice_text != '') form.append('transportservice_text', thePatient.transportservice_text);
    if (thePatient.comedorservice_text && thePatient.comedorservice_text != '') form.append('comedorservice_text', thePatient.comedorservice_text);
    if (thePatient.ayudadomicilioservice_text && thePatient.ayudadomicilioservice_text != '') form.append('ayudadomicilioservice_text', thePatient.ayudadomicilioservice_text);
    if (thePatient.duchaservice_text && thePatient.duchaservice_text != '') form.append('duchaservice_text', thePatient.duchaservice_text);
    
    if (thePatient.register_document_url && thePatient.register_document_url != '') form.append('register_document_url', thePatient.register_document_url);
    if (thePatient.register_document_url_signed && thePatient.register_document_url_signed.startsWith('https://')) form.append('register_document_url_signed', thePatient.register_document_url_signed);
    
    if (thePatient.register19_document_url && thePatient.register19_document_url != '') form.append('register19_document_url', thePatient.register19_document_url);
    if (thePatient.register19_document_url_signed && thePatient.register19_document_url_signed.startsWith('https://')) form.append('register19_document_url_signed', thePatient.register19_document_url_signed);
    
    if (thePatient.register20_document_url && thePatient.register20_document_url != '') form.append('register20_document_url', thePatient.register20_document_url);
    if (thePatient.register20_document_url_signed && thePatient.register20_document_url_signed.startsWith('https://')) form.append('register20_document_url_signed', thePatient.register20_document_url_signed);
    
    if (thePatient.register21_document_url && thePatient.register21_document_url != '') form.append('register21_document_url', thePatient.register21_document_url);
    if (thePatient.register21_document_url_signed && thePatient.register21_document_url_signed.startsWith('https://')) form.append('register21_document_url_signed', thePatient.register21_document_url_signed);
    
    if (thePatient.register22_document_url && thePatient.register22_document_url != '') form.append('register22_document_url', thePatient.register22_document_url);
    if (thePatient.register22_document_url_signed && thePatient.register22_document_url_signed.startsWith('https://')) form.append('register22_document_url_signed', thePatient.register22_document_url_signed);
    
    if (thePatient.register23_document_url && thePatient.register23_document_url != '') form.append('register23_document_url', thePatient.register23_document_url);
    if (thePatient.register23_document_url_signed && thePatient.register23_document_url_signed.startsWith('https://')) form.append('register23_document_url_signed', thePatient.register23_document_url_signed);
    
    if (thePatient.register24_document_url && thePatient.register24_document_url != '') form.append('register24_document_url', thePatient.register24_document_url);
    if (thePatient.register24_document_url_signed && thePatient.register24_document_url_signed.startsWith('https://')) form.append('register24_document_url_signed', thePatient.register24_document_url_signed);
    
    if (thePatient.register25_document_url && thePatient.register25_document_url != '') form.append('register25_document_url', thePatient.register25_document_url);
    if (thePatient.register25_document_url_signed && thePatient.register25_document_url_signed.startsWith('https://')) form.append('register25_document_url_signed', thePatient.register25_document_url_signed);
    
    if (thePatient.register26_document_url && thePatient.register26_document_url != '') form.append('register26_document_url', thePatient.register26_document_url);
    if (thePatient.register26_document_url_signed && thePatient.register26_document_url_signed.startsWith('https://')) form.append('register26_document_url_signed', thePatient.register26_document_url_signed);
    
    if (thePatient.register27_document_url && thePatient.register27_document_url != '') form.append('register27_document_url', thePatient.register27_document_url);
    if (thePatient.register27_document_url_signed && thePatient.register27_document_url_signed.startsWith('https://')) form.append('register27_document_url_signed', thePatient.register27_document_url_signed);
    
    if (thePatient.register28_document_url && thePatient.register28_document_url != '') form.append('register28_document_url', thePatient.register28_document_url);
    if (thePatient.register28_document_url_signed && thePatient.register28_document_url_signed.startsWith('https://')) form.append('register28_document_url_signed', thePatient.register28_document_url_signed);
    
    if (thePatient.register29_document_url && thePatient.register29_document_url != '') form.append('register29_document_url', thePatient.register29_document_url);
    if (thePatient.register29_document_url_signed && thePatient.register29_document_url_signed.startsWith('https://')) form.append('register29_document_url_signed', thePatient.register29_document_url_signed);
    
    if (thePatient.register30_document_url && thePatient.register30_document_url != '') form.append('register30_document_url', thePatient.register30_document_url);
    if (thePatient.register30_document_url_signed && thePatient.register30_document_url_signed.startsWith('https://')) form.append('register30_document_url_signed', thePatient.register30_document_url_signed);
    

    return this.http.post<any>(ENV.url.patients + '/savePatient', form);
  }


  
  savePatientFichaSocial(thePatient: PatientDTO) 
  {
    const form = new FormData();
    if(thePatient.id && thePatient.id!='') form.append('id', thePatient.id);

    if(thePatient.transportservice) form.append('transportservice', thePatient.transportservice.toString());


    if(thePatient.fs_talleres_estimulacion) form.append('fs_talleres_estimulacion', thePatient.fs_talleres_estimulacion.toString());
    if(thePatient.fs_gradior_stimmulus) form.append('fs_gradior_stimmulus', thePatient.fs_gradior_stimmulus.toString());
    if(thePatient.fs_sad) form.append('fs_sad', thePatient.fs_sad.toString());
    if(thePatient.fs_other) form.append('fs_other', thePatient.fs_other.toString());
    if(thePatient.fs_other_text) form.append('fs_other_text', thePatient.fs_other_text);
    if(thePatient.fs_comer_solo) form.append('fs_comer_solo', thePatient.fs_comer_solo.toString());
    if(thePatient.fs_lavarse_solo) form.append('fs_lavarse_solo', thePatient.fs_lavarse_solo.toString());
    if(thePatient.fs_salir_sin_perderse) form.append('fs_salir_sin_perderse', thePatient.fs_salir_sin_perderse.toString());
    if(thePatient.fs_reconocer_caras) form.append('fs_reconocer_caras', thePatient.fs_reconocer_caras.toString());
    if(thePatient.fs_leer_y_escribir) form.append('fs_leer_y_escribir', thePatient.fs_leer_y_escribir.toString());
    if(thePatient.fs_incontenencia_urinaria) form.append('fs_incontenencia_urinaria', thePatient.fs_incontenencia_urinaria.toString());
    if(thePatient.fs_conversar) form.append('fs_conversar', thePatient.fs_conversar.toString());
    if(thePatient.fs_reconocer_objetos_cotidianos) form.append('fs_reconocer_objetos_cotidianos', thePatient.fs_reconocer_objetos_cotidianos.toString());
    if(thePatient.fs_sufrir_alucinaciones) form.append('fs_sufrir_alucinaciones', thePatient.fs_sufrir_alucinaciones.toString());
    if(thePatient.fs_fases_agitacion) form.append('fs_fases_agitacion', thePatient.fs_fases_agitacion.toString());
    if(thePatient.fs_dificultad_orientarse) form.append('fs_dificultad_orientarse', thePatient.fs_dificultad_orientarse.toString());


    if(thePatient.fs_movilizarse) form.append('fs_movilizarse', thePatient.fs_movilizarse);
    if(thePatient.fs_datos_medicos) form.append('fs_datos_medicos', thePatient.fs_datos_medicos);

    if(thePatient.fs_grado_minusvalia) form.append('fs_grado_minusvalia', thePatient.fs_grado_minusvalia.toString());
    if(thePatient.fs_grado_minusvalia_text) form.append('fs_grado_minusvalia_text', thePatient.fs_grado_minusvalia_text);
    if(thePatient.fs_grado_dependencia) form.append('fs_grado_dependencia', thePatient.fs_grado_dependencia.toString());
    if(thePatient.fs_grado_dependencia_text) form.append('fs_grado_dependencia_text', thePatient.fs_grado_dependencia_text);
    if(thePatient.fs_incapacitacion_judicial) form.append('fs_incapacitacion_judicial', thePatient.fs_incapacitacion_judicial.toString());
    if(thePatient.fs_ayudas_externas) form.append('fs_ayudas_externas', thePatient.fs_ayudas_externas.toString());
    if(thePatient.fs_ayudas_externas_text) form.append('fs_ayudas_externas_text', thePatient.fs_ayudas_externas_text);


    return this.http.post<any>(ENV.url.patients + '/savePatientFichaSocial', form);
  }



  
  savePatientHistoriaSocial(thePatient: PatientDTO) 
  {
    const form = new FormData();
    if(thePatient.id && thePatient.id!='') form.append('id', thePatient.id);

    if (thePatient.hs_beca) form.append('hs_beca', thePatient.hs_beca.toString());
    if (thePatient.hs_diagnostico) form.append('hs_diagnostico', thePatient.hs_diagnostico.toString());
    if (thePatient.hs_autonomia) form.append('hs_autonomia', thePatient.hs_autonomia.toString());
    if (thePatient.hs_ayuda_abd) form.append('hs_ayuda_abd', thePatient.hs_ayuda_abd.toString());
    if (thePatient.hs_uc_solo) form.append('hs_uc_solo', thePatient.hs_uc_solo.toString());
    if (thePatient.hs_uc_conyuge) form.append('hs_uc_conyuge', thePatient.hs_uc_conyuge.toString());
    if (thePatient.hs_uc_hijos) form.append('hs_uc_hijos', thePatient.hs_uc_hijos.toString());
    if (thePatient.hs_uc_other) form.append('hs_uc_other', thePatient.hs_uc_other.toString());
    if (thePatient.hs_uc_other_text) form.append('hs_uc_other_text', thePatient.hs_uc_other_text.toString());
    if (thePatient.hs_nivel_formativo) form.append('hs_nivel_formativo', thePatient.hs_nivel_formativo.toString());
    if (thePatient.hs_interaccion_demas) form.append('hs_interaccion_demas', thePatient.hs_interaccion_demas.toString());
    if (thePatient.hs_interaccion_profesioneales) form.append('hs_interaccion_profesioneales', thePatient.hs_interaccion_profesioneales.toString());
    if (thePatient.hs_participacion_actividades) form.append('hs_participacion_actividades', thePatient.hs_participacion_actividades.toString());
    if (thePatient.hs_integracion_dinamica) form.append('hs_integracion_dinamica', thePatient.hs_integracion_dinamica.toString());
    if (thePatient.hs_grado_minusvalia_tipo) form.append('hs_grado_minusvalia_tipo', thePatient.hs_grado_minusvalia_tipo.toString());
    if (thePatient.hs_grado_minusvalia_cuando) form.append('hs_grado_minusvalia_cuando', thePatient.hs_grado_minusvalia_cuando.toString());
    if (thePatient.hs_ley_dependencia_solicitada) form.append('hs_ley_dependencia_solicitada', thePatient.hs_ley_dependencia_solicitada.toString());
    if (thePatient.hs_ley_dependencia_grado) form.append('hs_ley_dependencia_grado', thePatient.hs_ley_dependencia_grado.toString());
    if (thePatient.hs_recibe_servicio_administracion) form.append('hs_recibe_servicio_administracion', thePatient.hs_recibe_servicio_administracion.toString());
    if (thePatient.hs_patologias) form.append('hs_patologias', thePatient.hs_patologias.toString());
    if (thePatient.hs_diabetico) form.append('hs_diabetico', thePatient.hs_diabetico.toString());
    if (thePatient.hs_hipertenso) form.append('hs_hipertenso', thePatient.hs_hipertenso.toString());
    if (thePatient.hs_alimenta_bien) form.append('hs_alimenta_bien', thePatient.hs_alimenta_bien.toString());
    if (thePatient.hs_duerme_bien) form.append('hs_duerme_bien', thePatient.hs_duerme_bien.toString());
    if (thePatient.hs_fuma_bebe) form.append('hs_fuma_bebe', thePatient.hs_fuma_bebe.toString());
    if (thePatient.hs_drogas) form.append('hs_drogas', thePatient.hs_drogas.toString());
    if (thePatient.hs_drogas_text) form.append('hs_drogas_text', thePatient.hs_drogas_text.toString());
    if (thePatient.hs_valoracion_salud) form.append('hs_valoracion_salud', thePatient.hs_valoracion_salud.toString());
    if (thePatient.hs_fam_dificultades_convivencia) form.append('hs_fam_dificultades_convivencia', thePatient.hs_fam_dificultades_convivencia.toString());
    if (thePatient.hs_fam_dificultades_economicas) form.append('hs_fam_dificultades_economicas', thePatient.hs_fam_dificultades_economicas.toString());
    if (thePatient.hs_fam_dificultad_cuidados) form.append('hs_fam_dificultad_cuidados', thePatient.hs_fam_dificultad_cuidados.toString());
    if (thePatient.hs_fam_sin_apoyo) form.append('hs_fam_sin_apoyo', thePatient.hs_fam_sin_apoyo.toString());
    if (thePatient.hs_fam_agotamiento_cuidador) form.append('hs_fam_agotamiento_cuidador', thePatient.hs_fam_agotamiento_cuidador.toString());
    if (thePatient.hs_viv_sin_domicilio) form.append('hs_viv_sin_domicilio', thePatient.hs_viv_sin_domicilio.toString());
    if (thePatient.hs_viv_ruinas) form.append('hs_viv_ruinas', thePatient.hs_viv_ruinas.toString());
    if (thePatient.hs_viv_barreras) form.append('hs_viv_barreras', thePatient.hs_viv_barreras.toString());
    if (thePatient.hs_viv_inhabitabilidad) form.append('hs_viv_inhabitabilidad', thePatient.hs_viv_inhabitabilidad.toString());
    if (thePatient.hs_alquiler_elevado) form.append('hs_alquiler_elevado', thePatient.hs_alquiler_elevado.toString());
    if (thePatient.hs_escaleras_exteriores) form.append('hs_escaleras_exteriores', thePatient.hs_escaleras_exteriores.toString());
    if (thePatient.hs_escaleras_interiores) form.append('hs_escaleras_interiores', thePatient.hs_escaleras_interiores.toString());
    if (thePatient.hs_banera) form.append('hs_banera', thePatient.hs_banera.toString());
    if (thePatient.hs_alfombras) form.append('hs_alfombras', thePatient.hs_alfombras.toString());
    if (thePatient.hs_otros) form.append('hs_otros', thePatient.hs_otros.toString());
    if (thePatient.hs_otros_text) form.append('hs_otros_text', thePatient.hs_otros_text.toString());

    if (thePatient.hs_nombre1) form.append('hs_nombre1', thePatient.hs_nombre1.toString());
    if (thePatient.hs_parentesco1) form.append('hs_parentesco1', thePatient.hs_parentesco1.toString());
    if (thePatient.hs_edad1) form.append('hs_edad1', thePatient.hs_edad1.toString());
    if (thePatient.hs_profesion1) form.append('hs_profesion1', thePatient.hs_profesion1.toString());

    if (thePatient.hs_nombre2) form.append('hs_nombre2', thePatient.hs_nombre2.toString());
    if (thePatient.hs_parentesco2) form.append('hs_parentesco2', thePatient.hs_parentesco2.toString());
    if (thePatient.hs_edad2) form.append('hs_edad2', thePatient.hs_edad2.toString());
    if (thePatient.hs_profesion2) form.append('hs_profesion2', thePatient.hs_profesion2.toString());

    if (thePatient.hs_nombre3) form.append('hs_nombre3', thePatient.hs_nombre3.toString());
    if (thePatient.hs_parentesco3) form.append('hs_parentesco3', thePatient.hs_parentesco3.toString());
    if (thePatient.hs_edad3) form.append('hs_edad3', thePatient.hs_edad3.toString());
    if (thePatient.hs_profesion3) form.append('hs_profesion3', thePatient.hs_profesion3.toString());

    if (thePatient.hs_nombre4) form.append('hs_nombre4', thePatient.hs_nombre4.toString());
    if (thePatient.hs_parentesco4) form.append('hs_parentesco4', thePatient.hs_parentesco4.toString());
    if (thePatient.hs_edad4) form.append('hs_edad4', thePatient.hs_edad4.toString());
    if (thePatient.hs_profesion4) form.append('hs_profesion4', thePatient.hs_profesion4.toString());

    if (thePatient.hs_tiene_pareja) form.append('hs_tiene_pareja', thePatient.hs_tiene_pareja.toString());
    if (thePatient.hs_relacion_pareja) form.append('hs_relacion_pareja', thePatient.hs_relacion_pareja.toString());
    if (thePatient.hs_tiene_hijos) form.append('hs_tiene_hijos', thePatient.hs_tiene_hijos.toString());
    if (thePatient.hs_relacion_hijos) form.append('hs_relacion_hijos', thePatient.hs_relacion_hijos.toString());
    if (thePatient.hs_tiene_hermanos) form.append('hs_tiene_hermanos', thePatient.hs_tiene_hermanos.toString());
    if (thePatient.hs_relacion_hermanos) form.append('hs_relacion_hermanos', thePatient.hs_relacion_hermanos.toString());

    if (thePatient.hs_visitas_familiares) form.append('hs_visitas_familiares', thePatient.hs_visitas_familiares.toString());
    if (thePatient.hs_visitas_cuanto) form.append('hs_visitas_cuanto', thePatient.hs_visitas_cuanto.toString());
    if (thePatient.hs_apoyo_amigos) form.append('hs_apoyo_amigos', thePatient.hs_apoyo_amigos.toString());
    if (thePatient.hs_relacion_familia) form.append('hs_relacion_familia', thePatient.hs_relacion_familia.toString());
    if (thePatient.hs_acude_otras) form.append('hs_acude_otras', thePatient.hs_acude_otras.toString());
    if (thePatient.hs_recibe_pension) form.append('hs_recibe_pension', thePatient.hs_recibe_pension.toString());
    if (thePatient.hs_cuantia_pension) form.append('hs_cuantia_pension', thePatient.hs_cuantia_pension.toString());
    if (thePatient.hs_otra_prestacion) form.append('hs_otra_prestacion', thePatient.hs_otra_prestacion.toString());
    if (thePatient.hs_otros_ingresos) form.append('hs_otros_ingresos', thePatient.hs_otros_ingresos.toString());
    if (thePatient.hs_otros_recursos) form.append('hs_otros_recursos', thePatient.hs_otros_recursos.toString());
    if (thePatient.hs_valoracion_profesional) form.append('hs_valoracion_profesional', thePatient.hs_valoracion_profesional.toString());
    if (thePatient.hs_observaciones) form.append('hs_observaciones', thePatient.hs_observaciones.toString());


    return this.http.post<any>(ENV.url.patients + '/savePatientHistoriaSocial', form);
  }


  

  
  savePatientInformeSocial(thePatient: PatientDTO) 
  {
    const form = new FormData();
    if(thePatient.id && thePatient.id!='') form.append('id', thePatient.id);

    if (thePatient.is_tiempo_conoce_usuario) form.append('is_tiempo_conoce_usuario', thePatient.is_tiempo_conoce_usuario.toString());
    if (thePatient.is_servicios_prestados) form.append('is_servicios_prestados', thePatient.is_servicios_prestados.toString());
    if (thePatient.is_como_adaptado) form.append('is_como_adaptado', thePatient.is_como_adaptado.toString());
    if (thePatient.is_acudio_voluntad_propia) form.append('is_acudio_voluntad_propia', thePatient.is_acudio_voluntad_propia.toString());
    if (thePatient.is_quien_influyo_decision) form.append('is_quien_influyo_decision', thePatient.is_quien_influyo_decision.toString());
    if (thePatient.is_que_actividades) form.append('is_que_actividades', thePatient.is_que_actividades.toString());
    if (thePatient.is_como_relaciona) form.append('is_como_relaciona', thePatient.is_como_relaciona.toString());
    if (thePatient.is_como_pasa_dia) form.append('is_como_pasa_dia', thePatient.is_como_pasa_dia.toString());
    if (thePatient.is_problemas_psico) form.append('is_problemas_psico', thePatient.is_problemas_psico.toString());
    if (thePatient.is_problemas_psico_text) form.append('is_problemas_psico_text', thePatient.is_problemas_psico_text.toString());
    if (thePatient.is_recibe_tratamiento) form.append('is_recibe_tratamiento', thePatient.is_recibe_tratamiento.toString());
    if (thePatient.is_recibe_tratamiento_text) form.append('is_recibe_tratamiento_text', thePatient.is_recibe_tratamiento_text.toString());
    if (thePatient.is_familia_estru) form.append('is_familia_estru', thePatient.is_familia_estru.toString());
    if (thePatient.is_familia_estru_text) form.append('is_familia_estru_text', thePatient.is_familia_estru_text.toString());
    if (thePatient.is_recibe_ingresos_actividad_laboral) form.append('is_recibe_ingresos_actividad_laboral', thePatient.is_recibe_ingresos_actividad_laboral.toString());
    if (thePatient.is_esta_buscando_empleo) form.append('is_esta_buscando_empleo', thePatient.is_esta_buscando_empleo.toString());
    if (thePatient.is_vive_en) form.append('is_vive_en', thePatient.is_vive_en.toString());
    if (thePatient.is_cubiertas_necesidades_diarias) form.append('is_cubiertas_necesidades_diarias', thePatient.is_cubiertas_necesidades_diarias.toString());
    if (thePatient.is_valoracion_profesional) form.append('is_valoracion_profesional', thePatient.is_valoracion_profesional.toString());
    if (thePatient.is_propuesta) form.append('is_propuesta', thePatient.is_propuesta.toString());

    return this.http.post<any>(ENV.url.patients + '/savePatientInformeSocial', form);
  }


  saveInformeNeuroPsicologico(thePatient: PatientDTO) {
    const form = new FormData();
    if(thePatient.id && thePatient.id!='') form.append('id', thePatient.id);

    if (thePatient.ins_fecha_informe) form.append('ins_fecha_informe', this.formatDate2(thePatient.ins_fecha_informe));
    if (thePatient.ins_motivo_consulta) form.append('ins_motivo_consulta', thePatient.ins_motivo_consulta);
    if (thePatient.ins_antecedentes) form.append('ins_antecedentes', thePatient.ins_antecedentes);
    if (thePatient.ins_diagnostico) form.append('ins_diagnostico', thePatient.ins_diagnostico);
    if (thePatient.ins_texto_pre_puntuaciones) form.append('ins_texto_pre_puntuaciones', thePatient.ins_texto_pre_puntuaciones);
    if (thePatient.ins_fecha1) form.append('ins_fecha1', this.formatDate2(thePatient.ins_fecha1));
    if (thePatient.ins_fecha2) form.append('ins_fecha2', this.formatDate2(thePatient.ins_fecha2));
    if (thePatient.ins_fecha3) form.append('ins_fecha3', this.formatDate2(thePatient.ins_fecha3));
    if (thePatient.ins_fecha4) form.append('ins_fecha4', this.formatDate2(thePatient.ins_fecha4));
    if (thePatient.ins_orientacion1) form.append('ins_orientacion1', thePatient.ins_orientacion1.toString());
    if (thePatient.ins_orientacion2) form.append('ins_orientacion2', thePatient.ins_orientacion2.toString());
    if (thePatient.ins_orientacion3) form.append('ins_orientacion3', thePatient.ins_orientacion3.toString());
    if (thePatient.ins_orientacion4) form.append('ins_orientacion4', thePatient.ins_orientacion4.toString());
    if (thePatient.ins_lenguaje1) form.append('ins_lenguaje1', thePatient.ins_lenguaje1.toString());
    if (thePatient.ins_lenguaje2) form.append('ins_lenguaje2', thePatient.ins_lenguaje2.toString());
    if (thePatient.ins_lenguaje3) form.append('ins_lenguaje3', thePatient.ins_lenguaje3.toString());
    if (thePatient.ins_lenguaje4) form.append('ins_lenguaje4', thePatient.ins_lenguaje4.toString());
    if (thePatient.ins_memoria1) form.append('ins_memoria1', thePatient.ins_memoria1.toString());
    if (thePatient.ins_memoria2) form.append('ins_memoria2', thePatient.ins_memoria2.toString());
    if (thePatient.ins_memoria3) form.append('ins_memoria3', thePatient.ins_memoria3.toString());
    if (thePatient.ins_memoria4) form.append('ins_memoria4', thePatient.ins_memoria4.toString());
    if (thePatient.ins_atencalculo1) form.append('ins_atencalculo1', thePatient.ins_atencalculo1.toString());
    if (thePatient.ins_atencalculo2) form.append('ins_atencalculo2', thePatient.ins_atencalculo2.toString());
    if (thePatient.ins_atencalculo3) form.append('ins_atencalculo3', thePatient.ins_atencalculo3.toString());
    if (thePatient.ins_atencalculo4) form.append('ins_atencalculo4', thePatient.ins_atencalculo4.toString());
    if (thePatient.ins_praxis1) form.append('ins_praxis1', thePatient.ins_praxis1.toString());
    if (thePatient.ins_praxis2) form.append('ins_praxis2', thePatient.ins_praxis2.toString());
    if (thePatient.ins_praxis3) form.append('ins_praxis3', thePatient.ins_praxis3.toString());
    if (thePatient.ins_praxis4) form.append('ins_praxis4', thePatient.ins_praxis4.toString());
    if (thePatient.ins_pensabstracto1) form.append('ins_pensabstracto1', thePatient.ins_pensabstracto1.toString());
    if (thePatient.ins_pensabstracto2) form.append('ins_pensabstracto2', thePatient.ins_pensabstracto2.toString());
    if (thePatient.ins_pensabstracto3) form.append('ins_pensabstracto3', thePatient.ins_pensabstracto3.toString());
    if (thePatient.ins_pensabstracto4) form.append('ins_pensabstracto4', thePatient.ins_pensabstracto4.toString());
    if (thePatient.ins_percecpcion1) form.append('ins_percecpcion1', thePatient.ins_percecpcion1.toString());
    if (thePatient.ins_percecpcion2) form.append('ins_percecpcion2', thePatient.ins_percecpcion2.toString());
    if (thePatient.ins_percecpcion3) form.append('ins_percecpcion3', thePatient.ins_percecpcion3.toString());
    if (thePatient.ins_percecpcion4) form.append('ins_percecpcion4', thePatient.ins_percecpcion4.toString());
    if (thePatient.ins_total1) form.append('ins_total1', thePatient.ins_total1.toString());
    if (thePatient.ins_total2) form.append('ins_total2', thePatient.ins_total2.toString());
    if (thePatient.ins_total3) form.append('ins_total3', thePatient.ins_total3.toString());
    if (thePatient.ins_total4) form.append('ins_total4', thePatient.ins_total4.toString());
    if (thePatient.ins_fecha_mms1) form.append('ins_fecha_mms1', this.formatDate2(thePatient.ins_fecha_mms1));
    if (thePatient.ins_fecha_mms2) form.append('ins_fecha_mms2', this.formatDate2(thePatient.ins_fecha_mms2));
    if (thePatient.ins_fecha_mms3) form.append('ins_fecha_mms3', this.formatDate2(thePatient.ins_fecha_mms3));
    if (thePatient.ins_fecha_mms4) form.append('ins_fecha_mms4', this.formatDate2(thePatient.ins_fecha_mms4));
    if (thePatient.ins_mmse1) form.append('ins_mmse1', thePatient.ins_mmse1.toString());
    if (thePatient.ins_mmse2) form.append('ins_mmse2', thePatient.ins_mmse2.toString());
    if (thePatient.ins_mmse3) form.append('ins_mmse3', thePatient.ins_mmse3.toString());
    if (thePatient.ins_mmse4) form.append('ins_mmse4', thePatient.ins_mmse4.toString());
    if (thePatient.ins_texto_post_puntuaciones) form.append('ins_texto_post_puntuaciones', thePatient.ins_texto_post_puntuaciones);
    if (thePatient.ins_fecha_ind1) form.append('ins_fecha_ind1', this.formatDate2(thePatient.ins_fecha_ind1));
    if (thePatient.ins_fecha_ind2) form.append('ins_fecha_ind2', this.formatDate2(thePatient.ins_fecha_ind2));
    if (thePatient.ins_fecha_ind3) form.append('ins_fecha_ind3', this.formatDate2(thePatient.ins_fecha_ind3));
    if (thePatient.ins_indbathel1) form.append('ins_indbathel1', thePatient.ins_indbathel1.toString());
    if (thePatient.ins_indbathel2) form.append('ins_indbathel2', thePatient.ins_indbathel2.toString());
    if (thePatient.ins_indbathel3) form.append('ins_indbathel3', thePatient.ins_indbathel3.toString());
    if (thePatient.ins_indlawton1) form.append('ins_indlawton1', thePatient.ins_indlawton1.toString());
    if (thePatient.ins_indlawton2) form.append('ins_indlawton2', thePatient.ins_indlawton2.toString());
    if (thePatient.ins_indlawton3) form.append('ins_indlawton3', thePatient.ins_indlawton3.toString());
    if (thePatient.ins_texto_eval_conductual) form.append('ins_texto_eval_conductual', thePatient.ins_texto_eval_conductual);
    if (thePatient.ins_texto_conclusion) form.append('ins_texto_conclusion', thePatient.ins_texto_conclusion);
    

    return this.http.post<any>(ENV.url.patients + '/saveInformeNeuroPsicologico', form);
  }



  saveInformePsicoSocial(thePatient: PatientDTO) {
    const form = new FormData();
    if(thePatient.id && thePatient.id!='') form.append('id', thePatient.id);

    if (thePatient.ips_fecha_informe) form.append('ips_fecha_informe', this.formatDate2(thePatient.ips_fecha_informe));
    if (thePatient.ips_sanitarios) form.append('ips_sanitarios', thePatient.ips_sanitarios);
    if (thePatient.ips_sociofamiliar) form.append('ips_sociofamiliar', thePatient.ips_sociofamiliar);
    if (thePatient.ips_evalcognitiva) form.append('ips_evalcognitiva', thePatient.ips_evalcognitiva);
    if (thePatient.ips_evalconductual) form.append('ips_evalconductual', thePatient.ips_evalconductual);
    if (thePatient.ips_evalfuncional) form.append('ips_evalfuncional', thePatient.ips_evalfuncional);
    if (thePatient.ips_situacioneconomica) form.append('ips_situacioneconomica', thePatient.ips_situacioneconomica);
    if (thePatient.ips_observaciones) form.append('ips_observaciones', thePatient.ips_observaciones);
    return this.http.post<any>(ENV.url.patients + '/saveInformePsicoSocial', form);
  }

  savePAIFisio(thePatient: PatientDTO) {
    const form = new FormData();
    if (thePatient.id && thePatient.id != '') form.append('id', thePatient.id);
  
    if (thePatient.pai_fisio_fecha_valoracion) form.append('pai_fisio_fecha_valoracion', this.formatDate2(thePatient.pai_fisio_fecha_valoracion));
    if (thePatient.pai_fisio_prob_salud) form.append('pai_fisio_prob_salud', thePatient.pai_fisio_prob_salud);
    if (thePatient.pai_fisio_dolres) form.append('pai_fisio_dolres', thePatient.pai_fisio_dolres);
    if (thePatient.pai_fisio_duerme) form.append('pai_fisio_duerme', thePatient.pai_fisio_duerme);
    if (thePatient.pai_fisio_nec_aliment) form.append('pai_fisio_nec_aliment', thePatient.pai_fisio_nec_aliment);
    if (thePatient.pai_fisio_hab_saludables) form.append('pai_fisio_hab_saludables', thePatient.pai_fisio_hab_saludables);
    if (thePatient.pai_fisio_atencion_preven) form.append('pai_fisio_atencion_preven', thePatient.pai_fisio_atencion_preven);
    if (thePatient.pai_fisio_acceso_atencion) form.append('pai_fisio_acceso_atencion', thePatient.pai_fisio_acceso_atencion);
    if (thePatient.pai_fisio_medicacion_requerida) form.append('pai_fisio_medicacion_requerida', thePatient.pai_fisio_medicacion_requerida);
    if (thePatient.pai_fisio_alergias) form.append('pai_fisio_alergias', thePatient.pai_fisio_alergias);
    if (thePatient.pai_fisio_upp) form.append('pai_fisio_upp', thePatient.pai_fisio_upp);
    if (thePatient.pai_fisio_autonomo) form.append('pai_fisio_autonomo', thePatient.pai_fisio_autonomo);
    if (thePatient.pai_fisio_ayudas_tecnicas) form.append('pai_fisio_ayudas_tecnicas', thePatient.pai_fisio_ayudas_tecnicas);
    if (thePatient.pai_fisio_movilidad_mmss) form.append('pai_fisio_movilidad_mmss', thePatient.pai_fisio_movilidad_mmss);
    if (thePatient.pai_fisio_movilidad_mmii) form.append('pai_fisio_movilidad_mmii', thePatient.pai_fisio_movilidad_mmii);
    if (thePatient.pai_fisio_movilidad_cuello) form.append('pai_fisio_movilidad_cuello', thePatient.pai_fisio_movilidad_cuello);
    if (thePatient.pai_fisio_movilida_tronco) form.append('pai_fisio_movilida_tronco', thePatient.pai_fisio_movilida_tronco);
    if (thePatient.pai_fisio_equilibrio) form.append('pai_fisio_equilibrio', thePatient.pai_fisio_equilibrio);
    if (thePatient.pai_fisio_bipedestacion) form.append('pai_fisio_bipedestacion', thePatient.pai_fisio_bipedestacion);
    if (thePatient.pai_fisio_marcha) form.append('pai_fisio_marcha', thePatient.pai_fisio_marcha);
    if (thePatient.pai_fisio_riesgo_caidas) form.append('pai_fisio_riesgo_caidas', thePatient.pai_fisio_riesgo_caidas);
    if (thePatient.pai_fisio_deformidades) form.append('pai_fisio_deformidades', thePatient.pai_fisio_deformidades);
    if (thePatient.pai_fisio_disfruta_ocio) form.append('pai_fisio_disfruta_ocio', thePatient.pai_fisio_disfruta_ocio);
    if (thePatient.pai_fisio_espacios_ocio) form.append('pai_fisio_espacios_ocio', thePatient.pai_fisio_espacios_ocio);
    if (thePatient.pai_fisio_relaciones_entorno) form.append('pai_fisio_relaciones_entorno', thePatient.pai_fisio_relaciones_entorno);
    if (thePatient.pai_fisio_objetivos) form.append('pai_fisio_objetivos', thePatient.pai_fisio_objetivos);
    if (thePatient.pai_fisio_tratamiento) form.append('pai_fisio_tratamiento', thePatient.pai_fisio_tratamiento);
    if (thePatient.pai_fisio_valoraciones) form.append('pai_fisio_valoraciones', thePatient.pai_fisio_valoraciones);
    if (thePatient.pai_fisio_actuaciones) form.append('pai_fisio_actuaciones', thePatient.pai_fisio_actuaciones);
    if (thePatient.pai_fisio_incidencias) form.append('pai_fisio_incidencias', thePatient.pai_fisio_incidencias);
  
    return this.http.post<any>(ENV.url.patients + '/savePAIFisio', form);
  }
  
  savePAIPsico(thePatient: PatientDTO) {
    const form = new FormData();
    if (thePatient.id && thePatient.id !== '') form.append('id', thePatient.id);
  
    if (thePatient.pai_psico_acude) form.append('pai_psico_acude', thePatient.pai_psico_acude);
    if (thePatient.pai_psico_sintomas) form.append('pai_psico_sintomas', thePatient.pai_psico_sintomas);
    if (thePatient.pai_psico_diagnostico) form.append('pai_psico_diagnostico', thePatient.pai_psico_diagnostico);
    if (thePatient.pai_psico_quien_diagnostica) form.append('pai_psico_quien_diagnostica', thePatient.pai_psico_quien_diagnostica);
    if (thePatient.pai_psico_fecha_diagnostico) form.append('pai_psico_fecha_diagnostico', this.formatDate2(thePatient.pai_psico_fecha_diagnostico));
    if (thePatient.pai_psico_forma_evalucion) form.append('pai_psico_forma_evalucion', thePatient.pai_psico_forma_evalucion);
    if (thePatient.pai_psico_sintomatologia_actual) form.append('pai_psico_sintomatologia_actual', thePatient.pai_psico_sintomatologia_actual);
    if (thePatient.pai_psico_antecedentes) form.append('pai_psico_antecedentes', thePatient.pai_psico_antecedentes);
    if (thePatient.pai_psico_breve_historial) form.append('pai_psico_breve_historial', thePatient.pai_psico_breve_historial);
    if (thePatient.pai_psico_orientacion) form.append('pai_psico_orientacion', thePatient.pai_psico_orientacion);
    if (thePatient.pai_psico_lenguaje) form.append('pai_psico_lenguaje', thePatient.pai_psico_lenguaje);
    if (thePatient.pai_psico_memoria) form.append('pai_psico_memoria', thePatient.pai_psico_memoria);
    if (thePatient.pai_psico_atencion) form.append('pai_psico_atencion', thePatient.pai_psico_atencion);
    if (thePatient.pai_psico_praxi) form.append('pai_psico_praxi', thePatient.pai_psico_praxi);
    if (thePatient.pai_psico_pensamiento_abstracto) form.append('pai_psico_pensamiento_abstracto', thePatient.pai_psico_pensamiento_abstracto);
    if (thePatient.pai_psico_percepcion) form.append('pai_psico_percepcion', thePatient.pai_psico_percepcion);
    if (thePatient.pai_psico_funcion_ejecutiva) form.append('pai_psico_funcion_ejecutiva', thePatient.pai_psico_funcion_ejecutiva);
    if (thePatient.pai_psico_escala_folstein) form.append('pai_psico_escala_folstein', thePatient.pai_psico_escala_folstein);
    if (thePatient.pai_psico_evaluacion_conductual) form.append('pai_psico_evaluacion_conductual', thePatient.pai_psico_evaluacion_conductual);
    if (thePatient.pai_psico_plan_act_valoracion_s1) form.append('pai_psico_plan_act_valoracion_s1', thePatient.pai_psico_plan_act_valoracion_s1);
    if (thePatient.pai_psico_plan_act_valoracion_s2) form.append('pai_psico_plan_act_valoracion_s2', thePatient.pai_psico_plan_act_valoracion_s2);
    if (thePatient.pai_psico_plan_act_instrumentos_s1) form.append('pai_psico_plan_act_instrumentos_s1', thePatient.pai_psico_plan_act_instrumentos_s1);
    if (thePatient.pai_psico_plan_act_instrumentos_s2) form.append('pai_psico_plan_act_instrumentos_s2', thePatient.pai_psico_plan_act_instrumentos_s2);
    if (thePatient.pai_psico_plan_act_objetivos_s1) form.append('pai_psico_plan_act_objetivos_s1', thePatient.pai_psico_plan_act_objetivos_s1);
    if (thePatient.pai_psico_plan_act_objetivos_s2) form.append('pai_psico_plan_act_objetivos_s2', thePatient.pai_psico_plan_act_objetivos_s2);
    if (thePatient.pai_psico_plan_act_actividades_s1) form.append('pai_psico_plan_act_actividades_s1', thePatient.pai_psico_plan_act_actividades_s1);
    if (thePatient.pai_psico_plan_act_actividades_s2) form.append('pai_psico_plan_act_actividades_s2', thePatient.pai_psico_plan_act_actividades_s2);
    if (thePatient.pai_psico_plan_act_incidencias_s1) form.append('pai_psico_plan_act_incidencias_s1', thePatient.pai_psico_plan_act_incidencias_s1);
    if (thePatient.pai_psico_plan_act_incidencias_s2) form.append('pai_psico_plan_act_incidencias_s2', thePatient.pai_psico_plan_act_incidencias_s2);
    if (thePatient.pai_psico_valoraciones) form.append('pai_psico_valoraciones', thePatient.pai_psico_valoraciones);
    if (thePatient.pai_psico_actuaciones) form.append('pai_psico_actuaciones', thePatient.pai_psico_actuaciones);
    if (thePatient.pai_psico_incidencias) form.append('pai_psico_incidencias', thePatient.pai_psico_incidencias);
  
    return this.http.post<any>(ENV.url.patients + '/savePAIPsico', form);
  }
  

  savePAITocupa(thePatient: PatientDTO) {
    const form = new FormData();
    if (thePatient.id && thePatient.id !== '') form.append('id', thePatient.id);
  
    if (thePatient.pai_tocupa_nivel_independencia) form.append('pai_tocupa_nivel_independencia', thePatient.pai_tocupa_nivel_independencia);
    if (thePatient.pai_tocupa_plan_motriz) form.append('pai_tocupa_plan_motriz', thePatient.pai_tocupa_plan_motriz);
    if (thePatient.pai_tocupa_alimentacion) form.append('pai_tocupa_alimentacion', thePatient.pai_tocupa_alimentacion);
    if (thePatient.pai_tocupa_wc) form.append('pai_tocupa_wc', thePatient.pai_tocupa_wc);
    if (thePatient.pai_tocupa_aseo) form.append('pai_tocupa_aseo', thePatient.pai_tocupa_aseo);
    if (thePatient.pai_tocupa_deambular) form.append('pai_tocupa_deambular', thePatient.pai_tocupa_deambular);
    if (thePatient.pai_tocupa_transferencias) form.append('pai_tocupa_transferencias', thePatient.pai_tocupa_transferencias);
    if (thePatient.pai_tocupa_vestido) form.append('pai_tocupa_vestido', thePatient.pai_tocupa_vestido);
    if (thePatient.pai_tocupa_bano) form.append('pai_tocupa_bano', thePatient.pai_tocupa_bano);
    if (thePatient.pai_tocupa_escaolones) form.append('pai_tocupa_escaolones', thePatient.pai_tocupa_escaolones);
    if (thePatient.pai_tocupa_esfinteres) form.append('pai_tocupa_esfinteres', thePatient.pai_tocupa_esfinteres);
    if (thePatient.pai_tocupa_dinero) form.append('pai_tocupa_dinero', thePatient.pai_tocupa_dinero);
    if (thePatient.pai_tocupa_compras) form.append('pai_tocupa_compras', thePatient.pai_tocupa_compras);
    if (thePatient.pai_tocupa_telefono) form.append('pai_tocupa_telefono', thePatient.pai_tocupa_telefono);
    if (thePatient.pai_tocupa_casa) form.append('pai_tocupa_casa', thePatient.pai_tocupa_casa);
    if (thePatient.pai_tocupa_calle) form.append('pai_tocupa_calle', thePatient.pai_tocupa_calle);
    if (thePatient.pai_tocupa_medicacion) form.append('pai_tocupa_medicacion', thePatient.pai_tocupa_medicacion);
    if (thePatient.pai_tocupa_indice_barthel) form.append('pai_tocupa_indice_barthel', thePatient.pai_tocupa_indice_barthel);
    if (thePatient.pai_tocupa_escala_actividad) form.append('pai_tocupa_escala_actividad', thePatient.pai_tocupa_escala_actividad);
    if (thePatient.pai_tocupa_disfruta_tiempo) form.append('pai_tocupa_disfruta_tiempo', thePatient.pai_tocupa_disfruta_tiempo);
    if (thePatient.pai_tocupa_espacios_ocio) form.append('pai_tocupa_espacios_ocio', thePatient.pai_tocupa_espacios_ocio);
    if (thePatient.pai_tocupa_disfruta_ocio) form.append('pai_tocupa_disfruta_ocio', thePatient.pai_tocupa_disfruta_ocio);
    if (thePatient.pai_tocupa_relacion_otros) form.append('pai_tocupa_relacion_otros', thePatient.pai_tocupa_relacion_otros);
    if (thePatient.pai_tocupa_propios_objetivos) form.append('pai_tocupa_propios_objetivos', thePatient.pai_tocupa_propios_objetivos);
    if (thePatient.pai_tocupa_participa_actividades) form.append('pai_tocupa_participa_actividades', thePatient.pai_tocupa_participa_actividades);
    if (thePatient.pai_tocupa_actividades_iniciativa_propia) form.append('pai_tocupa_actividades_iniciativa_propia', thePatient.pai_tocupa_actividades_iniciativa_propia);
    if (thePatient.pai_tocupa_valoraciones) form.append('pai_tocupa_valoraciones', thePatient.pai_tocupa_valoraciones);
    if (thePatient.pai_tocupa_actuaciones) form.append('pai_tocupa_actuaciones', thePatient.pai_tocupa_actuaciones);
    if (thePatient.pai_tocupa_incidencias) form.append('pai_tocupa_incidencias', thePatient.pai_tocupa_incidencias);

  
    return this.http.post<any>(ENV.url.patients + '/savePAITocupa', form);
  }
  

  savePAIEnfer(thePatient: PatientDTO) {
    const form = new FormData();
    if (thePatient.id && thePatient.id !== '') form.append('id', thePatient.id);   

  
    if (thePatient.pai_enfer_diagnostico) form.append('pai_enfer_diagnostico', thePatient.pai_enfer_diagnostico);
    if (thePatient.pai_enfer_problemas_audio) form.append('pai_enfer_problemas_audio', thePatient.pai_enfer_problemas_audio);
    if (thePatient.pai_enfer_problemas_audio_text) form.append('pai_enfer_problemas_audio_text', thePatient.pai_enfer_problemas_audio_text);
    if (thePatient.pai_enfer_uso_audifono) form.append('pai_enfer_uso_audifono', thePatient.pai_enfer_uso_audifono);
    if (thePatient.pai_enfer_problemas_vision) form.append('pai_enfer_problemas_vision', thePatient.pai_enfer_problemas_vision);
    if (thePatient.pai_enfer_problemas_vision_text) form.append('pai_enfer_problemas_vision_text', thePatient.pai_enfer_problemas_vision_text);
    if (thePatient.pai_enfer_uso_gafas) form.append('pai_enfer_uso_gafas', thePatient.pai_enfer_uso_gafas);
    if (thePatient.pai_enfer_tension) form.append('pai_enfer_tension', thePatient.pai_enfer_tension);
    if (thePatient.pai_enfer_uso_medicacion) form.append('pai_enfer_uso_medicacion', thePatient.pai_enfer_uso_medicacion);
    if (thePatient.pai_enfer_diabetes) form.append('pai_enfer_diabetes', thePatient.pai_enfer_diabetes);
    if (thePatient.pai_enfer_diabetes_text) form.append('pai_enfer_diabetes_text', thePatient.pai_enfer_diabetes_text);
    if (thePatient.pai_enfer_alergias) form.append('pai_enfer_alergias', thePatient.pai_enfer_alergias);
    if (thePatient.pai_enfer_otras_enfermedades) form.append('pai_enfer_otras_enfermedades', thePatient.pai_enfer_otras_enfermedades);
  
    for (let i = 1; i <= 4; i++) {
      if (thePatient[`pai_enfer_tratamiento_medicamento_${i}`]) form.append(`pai_enfer_tratamiento_medicamento_${i}`, thePatient[`pai_enfer_tratamiento_medicamento_${i}`]);
      if (thePatient[`pai_enfer_tratamiento_dosis_${i}`]) form.append(`pai_enfer_tratamiento_dosis_${i}`, thePatient[`pai_enfer_tratamiento_dosis_${i}`]);
      if (thePatient[`pai_enfer_tratamiento_fecha_${i}`]) form.append(`pai_enfer_tratamiento_fecha_${i}`, thePatient[`pai_enfer_tratamiento_fecha_${i}`]);
      if (thePatient[`pai_enfer_tratamiento_para_${i}`]) form.append(`pai_enfer_tratamiento_para_${i}`, thePatient[`pai_enfer_tratamiento_para_${i}`]);
    }
  
    if (thePatient.pai_enfer_medicacion_centro) form.append('pai_enfer_medicacion_centro', thePatient.pai_enfer_medicacion_centro);
    if (thePatient.pai_enfer_medicacion_centro_text) form.append('pai_enfer_medicacion_centro_text', thePatient.pai_enfer_medicacion_centro_text);
    if (thePatient.pai_enfer_medicacion_puntual) form.append('pai_enfer_medicacion_puntual', thePatient.pai_enfer_medicacion_puntual);
  
    if (thePatient.pai_enfer_wc_esfinteres) form.append('pai_enfer_wc_esfinteres', thePatient.pai_enfer_wc_esfinteres);
    if (thePatient.pai_enfer_wc_retencion) form.append('pai_enfer_wc_retencion', thePatient.pai_enfer_wc_retencion);
    if (thePatient.pai_enfer_wc_estrenimiento) form.append('pai_enfer_wc_estrenimiento', thePatient.pai_enfer_wc_estrenimiento);
    if (thePatient.pai_enfer_wc_acompanam) form.append('pai_enfer_wc_acompanam', thePatient.pai_enfer_wc_acompanam);
  
    if (thePatient.pai_enfer_alim_alergias) form.append('pai_enfer_alim_alergias', thePatient.pai_enfer_alim_alergias);
    if (thePatient.pai_enfer_alim_alergias_text) form.append('pai_enfer_alim_alergias_text', thePatient.pai_enfer_alim_alergias_text);
    if (thePatient.pai_enfer_alim_dieta) form.append('pai_enfer_alim_dieta', thePatient.pai_enfer_alim_dieta);
    if (thePatient.pai_enfer_alim_dieta_text) form.append('pai_enfer_alim_dieta_text', thePatient.pai_enfer_alim_dieta_text);
    if (thePatient.pai_enfer_alim_problemas_deglucion) form.append('pai_enfer_alim_problemas_deglucion', thePatient.pai_enfer_alim_problemas_deglucion);
    if (thePatient.pai_enfer_alim_espesantes) form.append('pai_enfer_alim_espesantes', thePatient.pai_enfer_alim_espesantes);
    if (thePatient.pai_enfer_alim_ayuda) form.append('pai_enfer_alim_ayuda', thePatient.pai_enfer_alim_ayuda);
    if (thePatient.pai_enfer_alim_observaciones) form.append('pai_enfer_alim_observaciones', thePatient.pai_enfer_alim_observaciones);
  
    if (thePatient.pai_enfer_valoraciones) form.append('pai_enfer_valoraciones', thePatient.pai_enfer_valoraciones);
    if (thePatient.pai_enfer_actuaciones) form.append('pai_enfer_actuaciones', thePatient.pai_enfer_actuaciones);
    if (thePatient.pai_enfer_incidencias) form.append('pai_enfer_incidencias', thePatient.pai_enfer_incidencias);
  
    return this.http.post<any>(ENV.url.patients + '/savePAIEnfer', form);
  }
  

  savePAISocial(thePatient: PatientDTO) {
    const form = new FormData();
    if (thePatient.id && thePatient.id !== '') form.append('id', thePatient.id);   

    if (thePatient.pai_social_historia) form.append('pai_social_historia', thePatient.pai_social_historia);
    if (thePatient.pai_social_informes) form.append('pai_social_informes', thePatient.pai_social_informes);
    if (thePatient.pai_social_informes_text) form.append('pai_social_informes_text', thePatient.pai_social_informes_text);
    if (thePatient.pai_social_valoracion_disca) form.append('pai_social_valoracion_disca', thePatient.pai_social_valoracion_disca);
    if (thePatient.pai_social_valoracion_disca_fecha) form.append('pai_social_valoracion_disca_fecha', thePatient.pai_social_valoracion_disca_fecha);
    if (thePatient.pai_social_valoracion_disca_ca) form.append('pai_social_valoracion_disca_ca', thePatient.pai_social_valoracion_disca_ca);
    if (thePatient.pai_social_valoracion_disca_grado) form.append('pai_social_valoracion_disca_grado', thePatient.pai_social_valoracion_disca_grado);
    if (thePatient.pai_social_3_persona) form.append('pai_social_3_persona', thePatient.pai_social_3_persona);
    if (thePatient.pai_social_ayudas_tecnicas) form.append('pai_social_ayudas_tecnicas', thePatient.pai_social_ayudas_tecnicas);
    if (thePatient.pai_social_ayudas_tecnicas_text) form.append('pai_social_ayudas_tecnicas_text', thePatient.pai_social_ayudas_tecnicas_text);
    if (thePatient.pai_social_movilidad) form.append('pai_social_movilidad', thePatient.pai_social_movilidad);
    if (thePatient.pai_social_ley_dependencai) form.append('pai_social_ley_dependencai', thePatient.pai_social_ley_dependencai);
    if (thePatient.pai_social_grado_y_nivel) form.append('pai_social_grado_y_nivel', thePatient.pai_social_grado_y_nivel);
    if (thePatient.pai_social_cuidador) form.append('pai_social_cuidador', thePatient.pai_social_cuidador);
    if (thePatient.pai_social_relacion_cuidador) form.append('pai_social_relacion_cuidador', thePatient.pai_social_relacion_cuidador);
    if (thePatient.pai_social_indicadores) form.append('pai_social_indicadores', thePatient.pai_social_indicadores);
    if (thePatient.pai_social_apoyos) form.append('pai_social_apoyos', thePatient.pai_social_apoyos);
    if (thePatient.pai_social_vive) form.append('pai_social_vive', thePatient.pai_social_vive);
    if (thePatient.pai_social_domicilio_obstaculos) form.append('pai_social_domicilio_obstaculos', thePatient.pai_social_domicilio_obstaculos);
    if (thePatient.pai_social_domicilio_ayudas_tecnicas) form.append('pai_social_domicilio_ayudas_tecnicas', thePatient.pai_social_domicilio_ayudas_tecnicas);
    if (thePatient.pai_social_domicilio_confort) form.append('pai_social_domicilio_confort', thePatient.pai_social_domicilio_confort);
    if (thePatient.pai_social_domicilio_actual_quiere) form.append('pai_social_domicilio_actual_quiere', thePatient.pai_social_domicilio_actual_quiere);
    if (thePatient.pai_social_domicilio_actual_otro) form.append('pai_social_domicilio_actual_otro', thePatient.pai_social_domicilio_actual_otro);
    if (thePatient.pai_social_domicilio_actula_residencia) form.append('pai_social_domicilio_actula_residencia', thePatient.pai_social_domicilio_actula_residencia);
    if (thePatient.pai_social_apoyo_tipo1) form.append('pai_social_apoyo_tipo1', thePatient.pai_social_apoyo_tipo1);
    if (thePatient.pai_social_apoyo_tipo2) form.append('pai_social_apoyo_tipo2', thePatient.pai_social_apoyo_tipo2);
    if (thePatient.pai_social_apoyo_tipo3) form.append('pai_social_apoyo_tipo3', thePatient.pai_social_apoyo_tipo3);
    if (thePatient.pai_social_apoyo_tipo4) form.append('pai_social_apoyo_tipo4', thePatient.pai_social_apoyo_tipo4);
    if (thePatient.pai_social_apoyo_titularidad1) form.append('pai_social_apoyo_titularidad1', thePatient.pai_social_apoyo_titularidad1);
    if (thePatient.pai_social_apoyo_titularidad2) form.append('pai_social_apoyo_titularidad2', thePatient.pai_social_apoyo_titularidad2);
    if (thePatient.pai_social_apoyo_titularidad3) form.append('pai_social_apoyo_titularidad3', thePatient.pai_social_apoyo_titularidad3);
    if (thePatient.pai_social_apoyo_titularidad4) form.append('pai_social_apoyo_titularidad4', thePatient.pai_social_apoyo_titularidad4);
    if (thePatient.pai_social_apoyo_coste1) form.append('pai_social_apoyo_coste1', thePatient.pai_social_apoyo_coste1);
    if (thePatient.pai_social_apoyo_coste2) form.append('pai_social_apoyo_coste2', thePatient.pai_social_apoyo_coste2);
    if (thePatient.pai_social_apoyo_coste3) form.append('pai_social_apoyo_coste3', thePatient.pai_social_apoyo_coste3);
    if (thePatient.pai_social_apoyo_coste4) form.append('pai_social_apoyo_coste4', thePatient.pai_social_apoyo_coste4);
    if (thePatient.pai_social_apoyo_aportacion1) form.append('pai_social_apoyo_aportacion1', thePatient.pai_social_apoyo_aportacion1);
    if (thePatient.pai_social_apoyo_aportacion2) form.append('pai_social_apoyo_aportacion2', thePatient.pai_social_apoyo_aportacion2);
    if (thePatient.pai_social_apoyo_aportacion3) form.append('pai_social_apoyo_aportacion3', thePatient.pai_social_apoyo_aportacion3);
    if (thePatient.pai_social_apoyo_aportacion4) form.append('pai_social_apoyo_aportacion4', thePatient.pai_social_apoyo_aportacion4);
    if (thePatient.pai_social_apoyo_dom_prestacion1) form.append('pai_social_apoyo_dom_prestacion1', thePatient.pai_social_apoyo_dom_prestacion1);
    if (thePatient.pai_social_apoyo_dom_prestacion2) form.append('pai_social_apoyo_dom_prestacion2', thePatient.pai_social_apoyo_dom_prestacion2);
    if (thePatient.pai_social_apoyo_dom_prestacion3) form.append('pai_social_apoyo_dom_prestacion3', thePatient.pai_social_apoyo_dom_prestacion3);
    if (thePatient.pai_social_apoyo_dom_prestacion4) form.append('pai_social_apoyo_dom_prestacion4', thePatient.pai_social_apoyo_dom_prestacion4);
    if (thePatient.pai_social_apoyo_dom_intensidad1) form.append('pai_social_apoyo_dom_intensidad1', thePatient.pai_social_apoyo_dom_intensidad1);
    if (thePatient.pai_social_apoyo_dom_intensidad2) form.append('pai_social_apoyo_dom_intensidad2', thePatient.pai_social_apoyo_dom_intensidad2);
    if (thePatient.pai_social_apoyo_dom_intensidad3) form.append('pai_social_apoyo_dom_intensidad3', thePatient.pai_social_apoyo_dom_intensidad3);
    if (thePatient.pai_social_apoyo_dom_intensidad4) form.append('pai_social_apoyo_dom_intensidad4', thePatient.pai_social_apoyo_dom_intensidad4);
    if (thePatient.pai_social_apoyo_dom_coste1) form.append('pai_social_apoyo_dom_coste1', thePatient.pai_social_apoyo_dom_coste1);
    if (thePatient.pai_social_apoyo_dom_coste2) form.append('pai_social_apoyo_dom_coste2', thePatient.pai_social_apoyo_dom_coste2);
    if (thePatient.pai_social_apoyo_dom_coste3) form.append('pai_social_apoyo_dom_coste3', thePatient.pai_social_apoyo_dom_coste3);
    if (thePatient.pai_social_apoyo_dom_coste4) form.append('pai_social_apoyo_dom_coste4', thePatient.pai_social_apoyo_dom_coste4);
    if (thePatient.pai_social_apoyo_dom_aportacion1) form.append('pai_social_apoyo_dom_aportacion1', thePatient.pai_social_apoyo_dom_aportacion1);
    if (thePatient.pai_social_apoyo_dom_aportacion2) form.append('pai_social_apoyo_dom_aportacion2', thePatient.pai_social_apoyo_dom_aportacion2);
    if (thePatient.pai_social_apoyo_dom_aportacion3) form.append('pai_social_apoyo_dom_aportacion3', thePatient.pai_social_apoyo_dom_aportacion3);
    if (thePatient.pai_social_apoyo_dom_aportacion4) form.append('pai_social_apoyo_dom_aportacion4', thePatient.pai_social_apoyo_dom_aportacion4);
    if (thePatient.pai_social_apoyo_otras_prestacion1) form.append('pai_social_apoyo_otras_prestacion1', thePatient.pai_social_apoyo_otras_prestacion1);
    if (thePatient.pai_social_apoyo_otras_prestacion2) form.append('pai_social_apoyo_otras_prestacion2', thePatient.pai_social_apoyo_otras_prestacion2);
    if (thePatient.pai_social_apoyo_otras_prestacion3) form.append('pai_social_apoyo_otras_prestacion3', thePatient.pai_social_apoyo_otras_prestacion3);
    if (thePatient.pai_social_apoyo_otras_prestacion4) form.append('pai_social_apoyo_otras_prestacion4', thePatient.pai_social_apoyo_otras_prestacion4);
    if (thePatient.pai_social_apoyo_otras_titularidad1) form.append('pai_social_apoyo_otras_titularidad1', thePatient.pai_social_apoyo_otras_titularidad1);
    if (thePatient.pai_social_apoyo_otras_titularidad2) form.append('pai_social_apoyo_otras_titularidad2', thePatient.pai_social_apoyo_otras_titularidad2);
    if (thePatient.pai_social_apoyo_otras_titularidad3) form.append('pai_social_apoyo_otras_titularidad3', thePatient.pai_social_apoyo_otras_titularidad3);
    if (thePatient.pai_social_apoyo_otras_titularidad4) form.append('pai_social_apoyo_otras_titularidad4', thePatient.pai_social_apoyo_otras_titularidad4);
    if (thePatient.pai_social_apoyo_otras_intensidad1) form.append('pai_social_apoyo_otras_intensidad1', thePatient.pai_social_apoyo_otras_intensidad1);
    if (thePatient.pai_social_apoyo_otras_intensidad2) form.append('pai_social_apoyo_otras_intensidad2', thePatient.pai_social_apoyo_otras_intensidad2);
    if (thePatient.pai_social_apoyo_otras_intensidad3) form.append('pai_social_apoyo_otras_intensidad3', thePatient.pai_social_apoyo_otras_intensidad3);
    if (thePatient.pai_social_apoyo_otras_intensidad4) form.append('pai_social_apoyo_otras_intensidad4', thePatient.pai_social_apoyo_otras_intensidad4);


    if (thePatient.pai_social_ingresos) form.append('pai_social_ingresos', thePatient.pai_social_ingresos);
    if (thePatient.pai_social_ingresos_familia) form.append('pai_social_ingresos_familia', thePatient.pai_social_ingresos_familia);
    if (thePatient.pai_social_ingresos_cubre) form.append('pai_social_ingresos_cubre', thePatient.pai_social_ingresos_cubre);
    if (thePatient.pai_social_nivel_estudios) form.append('pai_social_nivel_estudios', thePatient.pai_social_nivel_estudios);
    if (thePatient.pai_social_relaciones) form.append('pai_social_relaciones', thePatient.pai_social_relaciones);
    if (thePatient.pai_social_necesidades) form.append('pai_social_necesidades', thePatient.pai_social_necesidades);
    if (thePatient.pai_social_objetivos) form.append('pai_social_objetivos', thePatient.pai_social_objetivos);
    if (thePatient.pai_social_valoraciones) form.append('pai_social_valoraciones', thePatient.pai_social_valoraciones);
    if (thePatient.pai_social_actuaciones) form.append('pai_social_actuaciones', thePatient.pai_social_actuaciones);
    if (thePatient.pai_social_incidencias) form.append('pai_social_incidencias', thePatient.pai_social_incidencias);

    return this.http.post<any>(ENV.url.patients + '/savePAISocial', form);
  }


  savePAIPortada(thePatient: PatientDTO) {
    const form = new FormData();
  
    if (thePatient.id) form.append('id', thePatient.id);
  
    if (thePatient.pai_portada_fecha) form.append('pai_portada_fecha', thePatient.pai_portada_fecha);
    if (thePatient.pai_portada_representante_guardador) form.append('pai_portada_representante_guardador', thePatient.pai_portada_representante_guardador);
    if (thePatient.pai_portada_cuidador_nombre) form.append('pai_portada_cuidador_nombre', thePatient.pai_portada_cuidador_nombre);
    if (thePatient.pai_portada_cuidador_edad) form.append('pai_portada_cuidador_edad', thePatient.pai_portada_cuidador_edad);
    if (thePatient.pai_portada_cuidador_dni) form.append('pai_portada_cuidador_dni', thePatient.pai_portada_cuidador_dni);
    if (thePatient.pai_portada_cuidador_domicilio) form.append('pai_portada_cuidador_domicilio', thePatient.pai_portada_cuidador_domicilio);
    if (thePatient.pai_portada_cuidador_estado_civil) form.append('pai_portada_cuidador_estado_civil', thePatient.pai_portada_cuidador_estado_civil);
    if (thePatient.pai_portada_cuidador_profesion) form.append('pai_portada_cuidador_profesion', thePatient.pai_portada_cuidador_profesion);
    if (thePatient.pai_portada_cuidador_relacion) form.append('pai_portada_cuidador_relacion', thePatient.pai_portada_cuidador_relacion);
    if (thePatient.pai_portada_cuidador_convive_otros) form.append('pai_portada_cuidador_convive_otros', thePatient.pai_portada_cuidador_convive_otros);
    if (thePatient.pai_portada_nss) form.append('pai_portada_nss', thePatient.pai_portada_nss);
    if (thePatient.pai_portada_seguro_medico) form.append('pai_portada_seguro_medico', thePatient.pai_portada_seguro_medico);
    if (thePatient.pai_portada_datos_medicos_enfermedades) form.append('pai_portada_datos_medicos_enfermedades', thePatient.pai_portada_datos_medicos_enfermedades);
    if (thePatient.pai_portada_datos_medicos_grado_minusvalida) form.append('pai_portada_datos_medicos_grado_minusvalida', thePatient.pai_portada_datos_medicos_grado_minusvalida);
    if (thePatient.pai_portada_datos_medicos_grado_dependencia) form.append('pai_portada_datos_medicos_grado_dependencia', thePatient.pai_portada_datos_medicos_grado_dependencia);
    if (thePatient.pai_portada_profesional_1) form.append('pai_portada_profesional_1', thePatient.pai_portada_profesional_1);
    if (thePatient.pai_portada_categoria_1) form.append('pai_portada_categoria_1', thePatient.pai_portada_categoria_1);
    if (thePatient.pai_portada_profesional_2) form.append('pai_portada_profesional_2', thePatient.pai_portada_profesional_2);
    if (thePatient.pai_portada_categoria_2) form.append('pai_portada_categoria_2', thePatient.pai_portada_categoria_2);
    if (thePatient.pai_portada_profesional_3) form.append('pai_portada_profesional_3', thePatient.pai_portada_profesional_3);
    if (thePatient.pai_portada_categoria_3) form.append('pai_portada_categoria_3', thePatient.pai_portada_categoria_3);
    if (thePatient.pai_portada_profesional_4) form.append('pai_portada_profesional_4', thePatient.pai_portada_profesional_4);
    if (thePatient.pai_portada_categoria_4) form.append('pai_portada_categoria_4', thePatient.pai_portada_categoria_4);
    if (thePatient.pai_portada_profesional_5) form.append('pai_portada_profesional_5', thePatient.pai_portada_profesional_5);
    if (thePatient.pai_portada_categoria_5) form.append('pai_portada_categoria_5', thePatient.pai_portada_categoria_5);
  
    return this.http.post<any>(ENV.url.patients + '/savePaiPortada', form);
  }
  

  unregisterPatient(id: string, unregister_reason: string, unregister_document_url: string, is_document_signed: boolean) 
  {
    const form = new FormData();
    form.append('id', id);
    if(unregister_reason) form.append('unregister_reason', unregister_reason);
    if(unregister_document_url && unregister_document_url!='') form.append('unregister_document_url', unregister_document_url);
    if(is_document_signed) form.append('is_document_signed', is_document_signed.toString());

 
    return this.http.post<any>(ENV.url.patients + '/unregisterPatient', form);
  }
  

  signDocumentRegister(idpatient: string, register_document_url_signed: string) {
    const form = new FormData();
    form.append('idpatient', idpatient);
    form.append('register_document_url_signed', register_document_url_signed);

    return this.http.post<any>(ENV.url.patients + '/signDocumentRegister', form);
  }
  

  signDocumentUnRegister(idpatient: string, unregister_document_url_signed: string) {
    const form = new FormData();
    form.append('idpatient', idpatient);
    form.append('unregister_document_url_signed', unregister_document_url_signed);

    return this.http.post<any>(ENV.url.patients + '/signDocumentUnRegister', form);
  }


  generatePAIResumen(thePatient: PatientDTO) {
    const form = new FormData();
    if (thePatient.id && thePatient.id !== '') {
      form.append('id', thePatient.id);
    }
  
    return this.http.post<any>(ENV.url.patients + '/generatePAIResumen', form);
  }
  
  
  formatDate1(thedate : Date)
  {
    return (thedate.getFullYear()+"")  + "-" + this.completeZeros(thedate.getMonth()+1) + "-" + this.completeZeros(thedate.getDate()) + " " + this.completeZeros(thedate.getHours()) + ":" + this.completeZeros(thedate.getMinutes());
  }

  
  formatDate2(thedate : Date)
  {
    return (thedate.getFullYear()+"")  + "-" + this.completeZeros(thedate.getMonth()+1) + "-" + this.completeZeros(thedate.getDate());
  }


  completeZeros(x : number) : string
  {
    if(x<=9) return "0" + x;
    else return ""+x;
  }

}
