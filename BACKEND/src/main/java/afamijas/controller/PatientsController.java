package afamijas.controller;


import afamijas.model.dto.PatientDTO;
import afamijas.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@Validated
@RequestMapping("/private/patients")
public class PatientsController extends AbstractBaseController
{
	final ErrorsService errorsService;

	final PatientsService patientsService;

	final MediaService mediaService;

	final RelativesService relativesService;

	@Autowired
	public PatientsController(UsersService usersService, ErrorsService errorsService, PatientsService patientsService, MediaService mediaService, RelativesService relativesService)
	{
		super(usersService);
		this.errorsService = errorsService;
		this.patientsService = patientsService;
		this.mediaService = mediaService;
		this.relativesService = relativesService;
	}


	@RequestMapping(method=RequestMethod.GET, value="getPatients", produces="application/json")
	public ResponseEntity<?> getPatients(
			@RequestParam(value = "idpatient", required = false) String idpatient,
			@RequestParam(value = "name_surnames", required = false) String name_surnames,
			@RequestParam(value = "documentid", required = false) String documentid,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam(value = "page", required = true) Integer page,
			@RequestParam(value = "size", required = true) Integer size,
			@RequestParam(value = "order", required = false) String order,
			@RequestParam(value = "orderasc", required = false) String orderasc,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(order==null) order = "name";
			if(orderasc==null) orderasc = "ASC";
			return new ResponseEntity<>(this.patientsService.getPatients(idpatient, name_surnames, documentid, status, page, size, order, orderasc), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.GET, value="getAllRelatives", produces="application/json")
	public ResponseEntity<?> getAllRelatives(
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.relativesService.getAllRelatives(), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}







	@RequestMapping(method=RequestMethod.POST, value="uploadRegisterDocument", produces="application/json")
	public ResponseEntity<?> uploadRegisterDocument(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.patientsService.uploadRegisterDocument(idpatient, file), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="uploadUnRegisterDocument", produces="application/json")
	public ResponseEntity<?> uploadUnRegisterDocument(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			return new ResponseEntity<>(this.patientsService.uploadUnRegisterDocument(idpatient, file), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="savePatient", produces="application/json")
	public ResponseEntity<?> savePatient(
			@RequestParam(value = "id", required = false) String id,

			@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "surname1", required = true) String surname1,
			@RequestParam(value = "surname2", required = false) String surname2,
			@RequestParam(value = "birthdate", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthdate,
			@RequestParam(value = "gender", required = true) String gender,
			@RequestParam(value = "documentid", required = true) String documentid,
			@RequestParam(value = "documenttype", required = true) String documenttype,

			@RequestParam(value = "idrelative", required = true) String idrelative,
			@RequestParam(value = "relativerelation", required = true) String relativerelation,

			@RequestParam(value = "postaladdress", required = true) String postaladdress,
			@RequestParam(value = "idcity", required = true) Integer idcity,
			@RequestParam(value = "idstate", required = true) Integer idstate,
			@RequestParam(value = "idcountry", required = true) Integer idcountry,
			@RequestParam(value = "postalcode", required = true) String postalcode,

			@RequestParam(value = "num_contrato", required = false) String num_contrato,
			@RequestParam(value = "fs_num_expediente", required = false) String fs_num_expediente,
			@RequestParam(value = "fs_fecha_inscripcion", required = true) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fs_fecha_inscripcion,
			@RequestParam(value = "fs_num_ss", required = false) String fs_num_ss,
			@RequestParam(value = "fs_estado_civil", required = true) String fs_estado_civil,
			@RequestParam(value = "phone", required = true) String phone,

			@RequestParam(value = "servicetype", required = true) String servicetype,
			@RequestParam(value = "tallerpsico", required = false) Boolean tallerpsico,

			@RequestParam(value = "transportservice", required = false) Boolean transportservice,
			@RequestParam(value = "transportservice_text", required = false) String transportservice_text,

			@RequestParam(value = "comedorservice", required = false) Boolean comedorservice,
			@RequestParam(value = "comedorservice_text", required = false) String comedorservice_text,

			@RequestParam(value = "ayudadomicilioservice", required = false) Boolean ayudadomicilioservice,
			@RequestParam(value = "ayudadomicilioservice_text", required = false) String ayudadomicilioservice_text,

			@RequestParam(value = "duchaservice", required = false) Boolean duchaservice,
			@RequestParam(value = "duchaservice_text", required = false) String duchaservice_text,

			@RequestParam(value = "register_document_url", required = false) String register_document_url,
			@RequestParam(value = "register_document_url_signed", required = false) String register_document_url_signed,

			@RequestParam(value = "register19_document_url", required = false) String register19_document_url,
			@RequestParam(value = "register19_document_url_signed", required = false) String register19_document_url_signed,
			@RequestParam(value = "register20_document_url", required = false) String register20_document_url,
			@RequestParam(value = "register20_document_url_signed", required = false) String register20_document_url_signed,
			@RequestParam(value = "register21_document_url", required = false) String register21_document_url,
			@RequestParam(value = "register21_document_url_signed", required = false) String register21_document_url_signed,
			@RequestParam(value = "register22_document_url", required = false) String register22_document_url,
			@RequestParam(value = "register22_document_url_signed", required = false) String register22_document_url_signed,
			@RequestParam(value = "register23_document_url", required = false) String register23_document_url,
			@RequestParam(value = "register23_document_url_signed", required = false) String register23_document_url_signed,
			@RequestParam(value = "register24_document_url", required = false) String register24_document_url,
			@RequestParam(value = "register24_document_url_signed", required = false) String register24_document_url_signed,
			@RequestParam(value = "register25_document_url", required = false) String register25_document_url,
			@RequestParam(value = "register25_document_url_signed", required = false) String register25_document_url_signed,
			@RequestParam(value = "register26_document_url", required = false) String register26_document_url,
			@RequestParam(value = "register26_document_url_signed", required = false) String register26_document_url_signed,
			@RequestParam(value = "register27_document_url", required = false) String register27_document_url,
			@RequestParam(value = "register27_document_url_signed", required = false) String register27_document_url_signed,
			@RequestParam(value = "register28_document_url", required = false) String register28_document_url,
			@RequestParam(value = "register28_document_url_signed", required = false) String register28_document_url_signed,
			@RequestParam(value = "register29_document_url", required = false) String register29_document_url,
			@RequestParam(value = "register29_document_url_signed", required = false) String register29_document_url_signed,
			@RequestParam(value = "register30_document_url", required = false) String register30_document_url,
			@RequestParam(value = "register30_document_url_signed", required = false) String register30_document_url_signed,

			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			if(tallerpsico==null) tallerpsico = false;
			if(transportservice==null) transportservice = false;
			if(comedorservice==null) comedorservice = false;
			if(ayudadomicilioservice==null) ayudadomicilioservice = false;
			if(duchaservice==null) duchaservice = false;

			return new ResponseEntity<>(this.patientsService.savePatient(id, name, surname1, surname2, birthdate, gender, documentid, documenttype,
					idrelative, relativerelation,
					postaladdress, idcity, idstate, idcountry, postalcode,
					num_contrato, fs_num_expediente, fs_fecha_inscripcion, fs_num_ss, fs_estado_civil, phone,
					servicetype, tallerpsico, transportservice, transportservice_text, comedorservice, comedorservice_text, ayudadomicilioservice, ayudadomicilioservice_text, duchaservice, duchaservice_text,
					register_document_url,  register_document_url_signed,
					register19_document_url,  register19_document_url_signed,
					register20_document_url,  register20_document_url_signed,
					register21_document_url,  register21_document_url_signed,
					register22_document_url,  register22_document_url_signed,
					register23_document_url,  register23_document_url_signed,
					register24_document_url,  register24_document_url_signed,
					register25_document_url,  register25_document_url_signed,
					register26_document_url,  register26_document_url_signed,
					register27_document_url,  register27_document_url_signed,
					register28_document_url,  register28_document_url_signed,
					register29_document_url,  register29_document_url_signed,
					register30_document_url,  register30_document_url_signed), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@RequestMapping(method=RequestMethod.POST, value="savePatientFichaSocial", produces="application/json")
	public ResponseEntity<?> savePatientFichaSocial(
			@RequestParam(value = "id", required = true) String id,

			@RequestParam(value = "fs_talleres_estimulacion", required = false) Boolean fs_talleres_estimulacion,
			@RequestParam(value = "transportservice", required = false) Boolean transportservice,
			@RequestParam(value = "fs_gradior_stimmulus", required = false) Boolean fs_gradior_stimmulus,
			@RequestParam(value = "fs_sad", required = false) Boolean fs_sad,
			@RequestParam(value = "fs_other", required = false) Boolean fs_other,
			@RequestParam(value = "fs_other_text", required = false) String fs_other_text,
			@RequestParam(value = "fs_comer_solo", required = false) Boolean fs_comer_solo,
			@RequestParam(value = "fs_lavarse_solo", required = false) Boolean fs_lavarse_solo,
			@RequestParam(value = "fs_salir_sin_perderse", required = false) Boolean fs_salir_sin_perderse,
			@RequestParam(value = "fs_reconocer_caras", required = false) Boolean fs_reconocer_caras,
			@RequestParam(value = "fs_leer_y_escribir", required = false) Boolean fs_leer_y_escribir,
			@RequestParam(value = "fs_incontenencia_urinaria", required = false) Boolean fs_incontenencia_urinaria,
			@RequestParam(value = "fs_conversar", required = false) Boolean fs_conversar,
			@RequestParam(value = "fs_reconocer_objetos_cotidianos", required = false) Boolean fs_reconocer_objetos_cotidianos,
			@RequestParam(value = "fs_sufrir_alucinaciones", required = false) Boolean fs_sufrir_alucinaciones,
			@RequestParam(value = "fs_fases_agitacion", required = false) Boolean fs_fases_agitacion,
			@RequestParam(value = "fs_dificultad_orientarse", required = false) Boolean fs_dificultad_orientarse,
			@RequestParam(value = "fs_movilizarse", required = false) String fs_movilizarse,
			@RequestParam(value = "fs_datos_medicos", required = false) String fs_datos_medicos,
			@RequestParam(value = "fs_grado_minusvalia", required = false) Boolean fs_grado_minusvalia,
			@RequestParam(value = "fs_grado_minusvalia_text", required = false) String fs_grado_minusvalia_text,
			@RequestParam(value = "fs_grado_dependencia", required = false) Boolean fs_grado_dependencia,
			@RequestParam(value = "fs_grado_dependencia_text", required = false) String fs_grado_dependencia_text,
			@RequestParam(value = "fs_incapacitacion_judicial", required = false) Boolean fs_incapacitacion_judicial,
			@RequestParam(value = "fs_ayudas_externas", required = false) Boolean fs_ayudas_externas,
			@RequestParam(value = "fs_ayudas_externas_text", required = false) String fs_ayudas_externas_text,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			return new ResponseEntity<>(this.patientsService.savePatientFichaSocial(id, transportservice,

					fs_talleres_estimulacion,

					fs_gradior_stimmulus,

					fs_sad,

					fs_other,

					fs_other_text,

					fs_comer_solo,

					fs_lavarse_solo,

					fs_salir_sin_perderse,

					fs_reconocer_caras,

					fs_leer_y_escribir,

					fs_incontenencia_urinaria,

					fs_conversar,

					fs_reconocer_objetos_cotidianos,

					fs_sufrir_alucinaciones,

					fs_fases_agitacion,

					fs_dificultad_orientarse,

					fs_movilizarse,

					fs_datos_medicos,

					fs_grado_minusvalia,

					fs_grado_minusvalia_text,

					fs_grado_dependencia,

					fs_grado_dependencia_text,

					fs_incapacitacion_judicial,

					fs_ayudas_externas,

					fs_ayudas_externas_text), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(method=RequestMethod.POST, value="savePatientHistoriaSocial", produces="application/json")
	public ResponseEntity<?> savePatientHistoriaSocial(
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "hs_beca", required = false) Boolean hs_beca,
			@RequestParam(value = "hs_diagnostico", required = false) String hs_diagnostico,
			@RequestParam(value = "hs_autonomia", required = false) Boolean hs_autonomia,
			@RequestParam(value = "hs_ayuda_abd", required = false) Boolean hs_ayuda_abd,
			@RequestParam(value = "hs_uc_solo", required = false) Boolean hs_uc_solo,
			@RequestParam(value = "hs_uc_conyuge", required = false) Boolean hs_uc_conyuge,
			@RequestParam(value = "hs_uc_hijos", required = false) Boolean hs_uc_hijos,
			@RequestParam(value = "hs_uc_other", required = false) Boolean hs_uc_other,
			@RequestParam(value = "hs_uc_other_text", required = false) String hs_uc_other_text,
			@RequestParam(value = "hs_nivel_formativo", required = false) String hs_nivel_formativo,
			@RequestParam(value = "hs_interaccion_demas", required = false) String hs_interaccion_demas,
			@RequestParam(value = "hs_interaccion_profesioneales", required = false) String hs_interaccion_profesioneales,
			@RequestParam(value = "hs_participacion_actividades", required = false) String hs_participacion_actividades,
			@RequestParam(value = "hs_integracion_dinamica", required = false) String hs_integracion_dinamica,
			@RequestParam(value = "hs_grado_minusvalia_tipo", required = false) String hs_grado_minusvalia_tipo,
			@RequestParam(value = "hs_grado_minusvalia_cuando", required = false) String hs_grado_minusvalia_cuando,
			@RequestParam(value = "hs_ley_dependencia_solicitada", required = false) Boolean hs_ley_dependencia_solicitada,
			@RequestParam(value = "hs_ley_dependencia_grado", required = false) String hs_ley_dependencia_grado,
			@RequestParam(value = "hs_recibe_servicio_administracion", required = false) Boolean hs_recibe_servicio_administracion,
			@RequestParam(value = "hs_patologias", required = false) String hs_patologias,
			@RequestParam(value = "hs_diabetico", required = false) Boolean hs_diabetico,
			@RequestParam(value = "hs_hipertenso", required = false) Boolean hs_hipertenso,
			@RequestParam(value = "hs_alimenta_bien", required = false) Boolean hs_alimenta_bien,
			@RequestParam(value = "hs_duerme_bien", required = false) Boolean hs_duerme_bien,
			@RequestParam(value = "hs_fuma_bebe", required = false) Boolean hs_fuma_bebe,
			@RequestParam(value = "hs_drogas", required = false) Boolean hs_drogas,
			@RequestParam(value = "hs_drogas_text", required = false) String hs_drogas_text,
			@RequestParam(value = "hs_valoracion_salud", required = false) String hs_valoracion_salud,
			@RequestParam(value = "hs_fam_dificultades_convivencia", required = false) Boolean hs_fam_dificultades_convivencia,
			@RequestParam(value = "hs_fam_dificultades_economicas", required = false) Boolean hs_fam_dificultades_economicas,
			@RequestParam(value = "hs_fam_dificultad_cuidados", required = false) Boolean hs_fam_dificultad_cuidados,
			@RequestParam(value = "hs_fam_sin_apoyo", required = false) Boolean hs_fam_sin_apoyo,
			@RequestParam(value = "hs_fam_agotamiento_cuidador", required = false) Boolean hs_fam_agotamiento_cuidador,
			@RequestParam(value = "hs_viv_sin_domicilio", required = false) Boolean hs_viv_sin_domicilio,
			@RequestParam(value = "hs_viv_ruinas", required = false) Boolean hs_viv_ruinas,
			@RequestParam(value = "hs_viv_barreras", required = false) Boolean hs_viv_barreras,
			@RequestParam(value = "hs_viv_inhabitabilidad", required = false) Boolean hs_viv_inhabitabilidad,
			@RequestParam(value = "hs_alquiler_elevado", required = false) Boolean hs_alquiler_elevado,
			@RequestParam(value = "hs_escaleras_exteriores", required = false) Boolean hs_escaleras_exteriores,
			@RequestParam(value = "hs_escaleras_interiores", required = false) Boolean hs_escaleras_interiores,
			@RequestParam(value = "hs_banera", required = false) Boolean hs_banera,
			@RequestParam(value = "hs_alfombras", required = false) Boolean hs_alfombras,
			@RequestParam(value = "hs_otros", required = false) Boolean hs_otros,
			@RequestParam(value = "hs_otros_text", required = false) String hs_otros_text,
			@RequestParam(value = "hs_nombre1", required = false) String hs_nombre1,
			@RequestParam(value = "hs_parentesco1", required = false) String hs_parentesco1,
			@RequestParam(value = "hs_edad1", required = false) Integer hs_edad1,
			@RequestParam(value = "hs_profesion1", required = false) String hs_profesion1,
			@RequestParam(value = "hs_nombre2", required = false) String hs_nombre2,
			@RequestParam(value = "hs_parentesco2", required = false) String hs_parentesco2,
			@RequestParam(value = "hs_edad2", required = false) Integer hs_edad2,
			@RequestParam(value = "hs_profesion2", required = false) String hs_profesion2,
			@RequestParam(value = "hs_nombre3", required = false) String hs_nombre3,
			@RequestParam(value = "hs_parentesco3", required = false) String hs_parentesco3,
			@RequestParam(value = "hs_edad3", required = false) Integer hs_edad3,
			@RequestParam(value = "hs_profesion3", required = false) String hs_profesion3,
			@RequestParam(value = "hs_nombre4", required = false) String hs_nombre4,
			@RequestParam(value = "hs_parentesco4", required = false) String hs_parentesco4,
			@RequestParam(value = "hs_edad4", required = false) Integer hs_edad4,
			@RequestParam(value = "hs_profesion4", required = false) String hs_profesion4,
			@RequestParam(value = "hs_tiene_pareja", required = false) Boolean hs_tiene_pareja,
			@RequestParam(value = "hs_relacion_pareja", required = false) String hs_relacion_pareja,
			@RequestParam(value = "hs_tiene_hijos", required = false) Boolean hs_tiene_hijos,
			@RequestParam(value = "hs_relacion_hijos", required = false) String hs_relacion_hijos,
			@RequestParam(value = "hs_tiene_hermanos", required = false) Boolean hs_tiene_hermanos,
			@RequestParam(value = "hs_relacion_hermanos", required = false) String hs_relacion_hermanos,
			@RequestParam(value = "hs_visitas_familiares", required = false) Boolean hs_visitas_familiares,
			@RequestParam(value = "hs_visitas_cuanto", required = false) String hs_visitas_cuanto,
			@RequestParam(value = "hs_apoyo_amigos", required = false) Boolean hs_apoyo_amigos,
			@RequestParam(value = "hs_relacion_familia", required = false) Boolean hs_relacion_familia,
			@RequestParam(value = "hs_acude_otras", required = false) Boolean hs_acude_otras,
			@RequestParam(value = "hs_recibe_pension", required = false) Boolean hs_recibe_pension,
			@RequestParam(value = "hs_cuantia_pension", required = false) Double hs_cuantia_pension,
			@RequestParam(value = "hs_otra_prestacion", required = false) String hs_otra_prestacion,
			@RequestParam(value = "hs_otros_ingresos", required = false) String hs_otros_ingresos,
			@RequestParam(value = "hs_otros_recursos", required = false) String hs_otros_recursos,
			@RequestParam(value = "hs_valoracion_profesional", required = false) String hs_valoracion_profesional,
			@RequestParam(value = "hs_observaciones", required = false) String hs_observaciones,
			HttpServletRequest request)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			return new ResponseEntity<>(this.patientsService.savePatientHistoriaSocial(
					id,
					hs_beca,
					hs_diagnostico,
					hs_autonomia,
					hs_ayuda_abd,
					hs_uc_solo,
					hs_uc_conyuge,
					hs_uc_hijos,
					hs_uc_other,
					hs_uc_other_text,
					hs_nivel_formativo,
					hs_interaccion_demas,
					hs_interaccion_profesioneales,
					hs_participacion_actividades,
					hs_integracion_dinamica,
					hs_grado_minusvalia_tipo,
					hs_grado_minusvalia_cuando,
					hs_ley_dependencia_solicitada,
					hs_ley_dependencia_grado,
					hs_recibe_servicio_administracion,
					hs_patologias,
					hs_diabetico,
					hs_hipertenso,
					hs_alimenta_bien,
					hs_duerme_bien,
					hs_fuma_bebe,
					hs_drogas,
					hs_drogas_text,
					hs_valoracion_salud,
					hs_fam_dificultades_convivencia,
					hs_fam_dificultades_economicas,
					hs_fam_dificultad_cuidados,
					hs_fam_sin_apoyo,
					hs_fam_agotamiento_cuidador,
					hs_viv_sin_domicilio,
					hs_viv_ruinas,
					hs_viv_barreras,
					hs_viv_inhabitabilidad,
					hs_alquiler_elevado,
					hs_escaleras_exteriores,
					hs_escaleras_interiores,
					hs_banera,
					hs_alfombras,
					hs_otros,
					hs_otros_text,
					hs_nombre1,
					hs_parentesco1,
					hs_edad1,
					hs_profesion1,
					hs_nombre2,
					hs_parentesco2,
					hs_edad2,
					hs_profesion2,
					hs_nombre3,
					hs_parentesco3,
					hs_edad3,
					hs_profesion3,
					hs_nombre4,
					hs_parentesco4,
					hs_edad4,
					hs_profesion4,
					hs_tiene_pareja,
					hs_relacion_pareja,
					hs_tiene_hijos,
					hs_relacion_hijos,
					hs_tiene_hermanos,
					hs_relacion_hermanos,
					hs_visitas_familiares,
					hs_visitas_cuanto,
					hs_apoyo_amigos,
					hs_relacion_familia,
					hs_acude_otras,
					hs_recibe_pension,
					hs_cuantia_pension,
					hs_otra_prestacion,
					hs_otros_ingresos,
					hs_otros_recursos,
					hs_valoracion_profesional,
					hs_observaciones
			), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method=RequestMethod.POST, value="savePatientInformeSocial", produces="application/json")
	public ResponseEntity<?> savePatientInformeSocial(
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "is_tiempo_conoce_usuario", required = false) String is_tiempo_conoce_usuario,
			@RequestParam(value = "is_servicios_prestados", required = false) String is_servicios_prestados,
			@RequestParam(value = "is_como_adaptado", required = false) String is_como_adaptado,
			@RequestParam(value = "is_acudio_voluntad_propia", required = false) Boolean is_acudio_voluntad_propia,
			@RequestParam(value = "is_quien_influyo_decision", required = false) String is_quien_influyo_decision,
			@RequestParam(value = "is_que_actividades", required = false) String is_que_actividades,
			@RequestParam(value = "is_como_relaciona", required = false) String is_como_relaciona,
			@RequestParam(value = "is_como_pasa_dia", required = false) String is_como_pasa_dia,
			@RequestParam(value = "is_problemas_psico", required = false) Boolean is_problemas_psico,
			@RequestParam(value = "is_problemas_psico_text", required = false) String is_problemas_psico_text,
			@RequestParam(value = "is_recibe_tratamiento", required = false) Boolean is_recibe_tratamiento,
			@RequestParam(value = "is_recibe_tratamiento_text", required = false) String is_recibe_tratamiento_text,
			@RequestParam(value = "is_familia_estru", required = false) Boolean is_familia_estru,
			@RequestParam(value = "is_familia_estru_text", required = false) String is_familia_estru_text,
			@RequestParam(value = "is_recibe_ingresos_actividad_laboral", required = false) Boolean is_recibe_ingresos_actividad_laboral,
			@RequestParam(value = "is_esta_buscando_empleo", required = false) Boolean is_esta_buscando_empleo,
			@RequestParam(value = "is_vive_en", required = false) String is_vive_en,
			@RequestParam(value = "is_cubiertas_necesidades_diarias", required = false) Boolean is_cubiertas_necesidades_diarias,
			@RequestParam(value = "is_valoracion_profesional", required = false) String is_valoracion_profesional,
			@RequestParam(value = "is_propuesta", required = false) String is_propuesta,
			HttpServletRequest request)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			return new ResponseEntity<>(this.patientsService.savePatientInformeSocial(
					id,
					is_tiempo_conoce_usuario,
					is_servicios_prestados,
					is_como_adaptado,
					is_acudio_voluntad_propia,
					is_quien_influyo_decision,
					is_que_actividades,
					is_como_relaciona,
					is_como_pasa_dia,
					is_problemas_psico,
					is_problemas_psico_text,
					is_recibe_tratamiento,
					is_recibe_tratamiento_text,
					is_familia_estru,
					is_familia_estru_text,
					is_recibe_ingresos_actividad_laboral,
					is_esta_buscando_empleo,
					is_vive_en,
					is_cubiertas_necesidades_diarias,
					is_valoracion_profesional,
					is_propuesta
			), HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}






	@RequestMapping(method=RequestMethod.POST, value="unregisterPatient", produces="application/json")
	public ResponseEntity<?> unregisterPatient(
			@RequestParam(value = "id", required = true) String id,
			@RequestParam(value = "unregister_reason", required = true) String unregister_reason,
			@RequestParam(value = "unregister_document_url", required = false) String unregister_document_url,
			@RequestParam(value = "is_document_signed", required = false) Boolean is_document_signed,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			if(is_document_signed==null) is_document_signed = false;
			this.patientsService.unregisterPatient(id, unregister_reason, unregister_document_url, is_document_signed);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}




	@RequestMapping(method=RequestMethod.POST, value="signDocumentRegister", produces="application/json")
	public ResponseEntity<?> signDocumentRegister(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "register_document_url_signed", required = true) String register_document_url_signed,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			this.patientsService.signDocumentRegister(idpatient, register_document_url_signed);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(method=RequestMethod.POST, value="signDocumentUnRegister", produces="application/json")
	public ResponseEntity<?> signDocumentUnRegister(
			@RequestParam(value = "idpatient", required = true) String idpatient,
			@RequestParam(value = "unregister_document_url_signed", required = true) String unregister_document_url_signed,
			HttpServletRequest request
	)
	{
		try
		{
			if(!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			this.patientsService.signDocumentUnRegister(idpatient, unregister_document_url_signed);
			return new ResponseEntity<>("", HttpStatus.OK);
		}
		catch(Exception e)
		{
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



	@RequestMapping(method = RequestMethod.POST, value = "saveInformeNeuroPsicologico", produces = "application/json")
	public ResponseEntity<?> saveInformeNeuroPsicologico(
			@RequestParam(value = "id", required = true) String id,

			@RequestParam(value = "ins_fecha_informe", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ins_fecha_informe,
			@RequestParam(value = "ins_motivo_consulta", required = false) String ins_motivo_consulta,
			@RequestParam(value = "ins_antecedentes", required = false) String ins_antecedentes,
			@RequestParam(value = "ins_diagnostico", required = false) String ins_diagnostico,
			@RequestParam(value = "ins_texto_pre_puntuaciones", required = false) String ins_texto_pre_puntuaciones,

			@RequestParam(value = "ins_fecha1", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  ins_fecha1,
			@RequestParam(value = "ins_fecha2", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  ins_fecha2,
			@RequestParam(value = "ins_fecha3", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  ins_fecha3,
			@RequestParam(value = "ins_fecha4", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  ins_fecha4,

			@RequestParam(value = "ins_orientacion1", required = false) Double ins_orientacion1,
			@RequestParam(value = "ins_orientacion2", required = false) Double ins_orientacion2,
			@RequestParam(value = "ins_orientacion3", required = false) Double ins_orientacion3,
			@RequestParam(value = "ins_orientacion4", required = false) Double ins_orientacion4,

			@RequestParam(value = "ins_lenguaje1", required = false) Double ins_lenguaje1,
			@RequestParam(value = "ins_lenguaje2", required = false) Double ins_lenguaje2,
			@RequestParam(value = "ins_lenguaje3", required = false) Double ins_lenguaje3,
			@RequestParam(value = "ins_lenguaje4", required = false) Double ins_lenguaje4,

			@RequestParam(value = "ins_memoria1", required = false) Double ins_memoria1,
			@RequestParam(value = "ins_memoria2", required = false) Double ins_memoria2,
			@RequestParam(value = "ins_memoria3", required = false) Double ins_memoria3,
			@RequestParam(value = "ins_memoria4", required = false) Double ins_memoria4,

			@RequestParam(value = "ins_atencalculo1", required = false) Double ins_atencalculo1,
			@RequestParam(value = "ins_atencalculo2", required = false) Double ins_atencalculo2,
			@RequestParam(value = "ins_atencalculo3", required = false) Double ins_atencalculo3,
			@RequestParam(value = "ins_atencalculo4", required = false) Double ins_atencalculo4,

			@RequestParam(value = "ins_praxis1", required = false) Double ins_praxis1,
			@RequestParam(value = "ins_praxis2", required = false) Double ins_praxis2,
			@RequestParam(value = "ins_praxis3", required = false) Double ins_praxis3,
			@RequestParam(value = "ins_praxis4", required = false) Double ins_praxis4,

			@RequestParam(value = "ins_pensabstracto1", required = false) Double ins_pensabstracto1,
			@RequestParam(value = "ins_pensabstracto2", required = false) Double ins_pensabstracto2,
			@RequestParam(value = "ins_pensabstracto3", required = false) Double ins_pensabstracto3,
			@RequestParam(value = "ins_pensabstracto4", required = false) Double ins_pensabstracto4,

			@RequestParam(value = "ins_percecpcion1", required = false) Double ins_percecpcion1,
			@RequestParam(value = "ins_percecpcion2", required = false) Double ins_percecpcion2,
			@RequestParam(value = "ins_percecpcion3", required = false) Double ins_percecpcion3,
			@RequestParam(value = "ins_percecpcion4", required = false) Double ins_percecpcion4,

			@RequestParam(value = "ins_total1", required = false) Double ins_total1,
			@RequestParam(value = "ins_total2", required = false) Double ins_total2,
			@RequestParam(value = "ins_total3", required = false) Double ins_total3,
			@RequestParam(value = "ins_total4", required = false) Double ins_total4,

			@RequestParam(value = "ins_fecha_mms1", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  ins_fecha_mms1,
			@RequestParam(value = "ins_fecha_mms2", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  ins_fecha_mms2,
			@RequestParam(value = "ins_fecha_mms3", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  ins_fecha_mms3,
			@RequestParam(value = "ins_fecha_mms4", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  ins_fecha_mms4,

			@RequestParam(value = "ins_mmse1", required = false) Double ins_mmse1,
			@RequestParam(value = "ins_mmse2", required = false) Double ins_mmse2,
			@RequestParam(value = "ins_mmse3", required = false) Double ins_mmse3,
			@RequestParam(value = "ins_mmse4", required = false) Double ins_mmse4,

			@RequestParam(value = "ins_texto_post_puntuaciones", required = false) String ins_texto_post_puntuaciones,

			@RequestParam(value = "ins_fecha_ind1", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  ins_fecha_ind1,
			@RequestParam(value = "ins_fecha_ind2", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  ins_fecha_ind2,
			@RequestParam(value = "ins_fecha_ind3", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate  ins_fecha_ind3,

			@RequestParam(value = "ins_indbathel1", required = false) Double ins_indbathel1,
			@RequestParam(value = "ins_indbathel2", required = false) Double ins_indbathel2,
			@RequestParam(value = "ins_indbathel3", required = false) Double ins_indbathel3,

			@RequestParam(value = "ins_indlawton1", required = false) Double ins_indlawton1,
			@RequestParam(value = "ins_indlawton2", required = false) Double ins_indlawton2,
			@RequestParam(value = "ins_indlawton3", required = false) Double ins_indlawton3,

			@RequestParam(value = "ins_texto_eval_conductual", required = false) String ins_texto_eval_conductual,
			@RequestParam(value = "ins_texto_conclusion", required = false) String ins_texto_conclusion,

			HttpServletRequest request) {
		try {
			if (!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			return new ResponseEntity<>(this.patientsService.saveInformeNeuroPsicologico(
					id, ins_fecha_informe, ins_motivo_consulta, ins_antecedentes, ins_diagnostico, ins_texto_pre_puntuaciones,
					ins_fecha1, ins_fecha2, ins_fecha3, ins_fecha4, ins_orientacion1, ins_orientacion2, ins_orientacion3, ins_orientacion4,
					ins_lenguaje1, ins_lenguaje2, ins_lenguaje3, ins_lenguaje4, ins_memoria1, ins_memoria2, ins_memoria3, ins_memoria4,
					ins_atencalculo1, ins_atencalculo2, ins_atencalculo3, ins_atencalculo4, ins_praxis1, ins_praxis2, ins_praxis3, ins_praxis4,
					ins_pensabstracto1, ins_pensabstracto2, ins_pensabstracto3, ins_pensabstracto4, ins_percecpcion1, ins_percecpcion2,
					ins_percecpcion3, ins_percecpcion4, ins_total1, ins_total2, ins_total3, ins_total4, ins_fecha_mms1, ins_fecha_mms2,
					ins_fecha_mms3, ins_fecha_mms4, ins_mmse1, ins_mmse2, ins_mmse3, ins_mmse4, ins_texto_post_puntuaciones,
					ins_fecha_ind1, ins_fecha_ind2, ins_fecha_ind3, ins_indbathel1, ins_indbathel2, ins_indbathel3, ins_indlawton1,
					ins_indlawton2, ins_indlawton3, ins_texto_eval_conductual, ins_texto_conclusion), HttpStatus.OK);
		} catch (Exception e) {
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}





	@RequestMapping(method = RequestMethod.POST, value = "saveInformePsicoSocial", produces = "application/json")
	public ResponseEntity<?> saveInformePsicoSocial(
			@RequestParam(value = "id", required = true) String id,

			@RequestParam(value = "ips_fecha_informe", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate ips_fecha_informe,
			@RequestParam(value = "ips_sanitarios", required = false) String ips_sanitarios,
			@RequestParam(value = "ips_sociofamiliar", required = false) String ips_sociofamiliar,
			@RequestParam(value = "ips_evalcognitiva", required = false) String ips_evalcognitiva,
			@RequestParam(value = "ips_evalconductual", required = false) String ips_evalconductual,
			@RequestParam(value = "ips_evalfuncional", required = false) String ips_evalfuncional,
			@RequestParam(value = "ips_situacioneconomica", required = false) String ips_situacioneconomica,
			@RequestParam(value = "ips_observaciones", required = false) String ips_observaciones,


			HttpServletRequest request) {
		try {
			if (!this.isADMIN() && !this.isMANAGER()) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

			return new ResponseEntity<>(this.patientsService.saveInformePsicoSocial(
					id, ips_fecha_informe, ips_sanitarios, ips_sociofamiliar, ips_evalcognitiva, ips_evalconductual,
					ips_evalfuncional, ips_situacioneconomica, ips_observaciones), HttpStatus.OK);
		} catch (Exception e) {
			this.errorsService.sendError(e, this.getParameters(request));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}

