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

export interface UserDTO {
    id : string;
    fullname : string;
    roles : string[]
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



// Tiempo notificaciones flotantse
export enum ToastTime {
    TIME = 2500
}
