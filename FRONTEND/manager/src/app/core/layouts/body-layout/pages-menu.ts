import { NbMenuItem } from '@nebular/theme';

//MENÚ PARA TODOS LOS PERFILES
export const MENU_ITEMS: NbMenuItem[] = [
  {
    title: 'Calendario',
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
    link: '/routes/relative-route',
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
    icon: { icon: 'fa-utensils', pack: 'fas' },
    link: '/menus',
    data: 'menu.menus',
    expanded: false,
  },      
  {
    title: 'Autorizaciones',
    icon: { icon: 'fa-pen', pack: 'fas' },
    link: '/permissions',
    data: 'menu.permissions',
    expanded: false,
  }
       


          


];


  //MENÚ MEDICACIÓN
  export const MENU_MEDICATION: NbMenuItem[] = [
    {
      title: 'Medicación',
      icon: { icon: 'fa-kit-medical', pack: 'fas' },
      link: '/worker-medication',
      data: 'menu.worker.medication',
      expanded: false,
    }
  ];  


  
  //MENÚ COMIDAS
  export const MENU_FOODS: NbMenuItem[] = [
    {
      title: 'Comidas',
      icon: { icon: 'fa-utensils', pack: 'fas' },
      link: '/worker-food',
      data: 'menu.worker.food',
      expanded: false,
    }
  ];  



  
  
  //MENÚ REGISTRO ALIMETNACIÓN
  export const MENU_FEEDINGS: NbMenuItem[] = [
    {
      title: 'Registro alimentación',
      icon: { icon: 'fa-bowl-food', pack: 'fas' },
      link: '/worker-feeding',
      data: 'menu.worker.feeding',
      expanded: false,
    }
  ];  


  
  
  //MENÚ REGISTRO TEMPERATURAS
  export const MENU_TEMP: NbMenuItem[] = [
    {
      title: 'Registro temperaturas',
      icon: { icon: 'fa-temperature-low', pack: 'fas' },
      link: '/worker-temp',
      data: 'menu.worker.temp',
      expanded: false,
    }
  ];  
