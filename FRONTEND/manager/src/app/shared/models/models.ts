// Respuesta del login:
export interface LoginResponse {
    token: string;
    roles: string[];
    userId: string;
    username: string;
    dni: string;
    fullname : string;
    photo_url : string;
}

export interface ActionDTO {
    action : string;
    text : string;
}

export interface UserDTO {
    id : string;
    fullname : string;
    roles : string[]
}

export interface PatientDTO {
    id : string;
    username : string;
    email : string;
    name : string;
    surname1 : string;
    surname2 : string;
    dni : string;
    phone : string;
    postaladdres : string;
    idcity : string;
    cityname : string;
    idstate : string;
    statename : string;
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
    postaladdress : RouteStopDTO[];
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
    roleNames.set("PSYCHOLOGIST", "Piscología");
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
