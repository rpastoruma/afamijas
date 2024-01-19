// Respuesta del login:
export interface LoginResponse {
    token: string;
    role: string;
    userId: string;
    username: string;
    dni: string;
    fullname : string;
    photo_url : string;
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

export const RoleTranslate: RolesT = {
    ROOT: 'Super Administrador',
    RELATIVE: 'Familiar',
    WORKER : 'Trabajador/a',
    ADMIN :'Administrador/a',
    SOCIALWORKER : 'Trabajador/a social',
    PSYCHO : 'Psicólogo/a'
}

export enum RoleCode 
{
    ROOT = 'ROOT',
    RELATIVE = 'RELATIVE',
    WORKER = 'WORKER',
    ADMIN = 'ADMIN',
    SOCIALWORKER = 'SOCIALWORKER',
    PSYCHO = 'PSYCHO'
}

export function hasRole(role, roleCompare: RoleCode) 
{
    return role === roleCompare;
}

// Tiempo notificaciones flotantse
export enum ToastTime {
    TIME = 2500
}
