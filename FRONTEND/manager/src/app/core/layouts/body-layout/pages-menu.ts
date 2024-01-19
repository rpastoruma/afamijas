import { NbMenuItem } from '@nebular/theme';

//MENÚ PARA TODOS LOS PERFILES
export const MENU_ITEMS: NbMenuItem[] = [
  {
    title: 'Calendar',
    icon: { icon: 'fa-calendar', pack: 'fas' },
    link: '/calendar',
    data: 'menu.calendar',
    expanded: false,
  }

 
];

//MENÚ PARA LOS FAMILIARES
export const MENU_RELATIVE_ITEMS: NbMenuItem[] = [
  {
    title: 'Ruta',
    icon: { icon: 'fa-route', pack: 'fas' },
    link: '/route',
    data: 'menu.route',
    expanded: false,
  },
  {
    title: 'Faltas',
    icon: { icon: 'fa-xmark', pack: 'fas' },
    link: '/absences',
    data: 'menu.absences',
    expanded: false,
  },
  {
    title: 'Menús',
    icon: { icon: 'fa-file-lines', pack: 'fas' },
    data: 'menu.menus',
    expanded: false,
  },      
  {
    title: 'Autorizaciones',
    icon: { icon: 'fa-pen', pack: 'fas' },
    data: 'menu.permissions',
    expanded: false,
  }
       
          
];
