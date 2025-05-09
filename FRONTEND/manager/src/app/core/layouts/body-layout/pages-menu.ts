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
      icon: { icon: 'fa-pills', pack: 'fas' },
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


  
  
  //MENÚ REGISTRO TEMPERATURAS
  export const MENU_LEGIONELLA: NbMenuItem[] = [
    {
      title: 'Control Legionella',
      icon: { icon: 'fa-bacteria', pack: 'fas' },
      link: '/worker-legionella',
      data: 'menu.worker.legionella',
      expanded: false,
    }
  ];  
 
  
  //MENÚ REGISTRO LIMPIEZA
  export const MENU_CLEANING: NbMenuItem[] = [
    {
      title: 'Registro de limpieza',
      icon: { icon: 'fa-broom', pack: 'fas' },
      link: '/worker-wclogs',
      data: 'menu.worker-wclogs',
      expanded: false,
    }
  ];  



    
  //MENÚ REGISTRO SANITARIO
  export const MENU_HEALTH: NbMenuItem[] = [
    {
      title: 'Registro sanitario',
      icon: { icon: 'fa-kit-medical', pack: 'fas' },
      link: '/worker-healthlogs',
      data: 'menu.worker-healthlogs',
      expanded: false,
    }
  ];  


    
  //MENÚ DOCUMENTOS
  export const MENU_DOCS: NbMenuItem[] = [
    {
      title: 'Documentos',
      icon: { icon: 'fa-folder-open', pack: 'fas' },
      link: '/worker-docs',
      data: 'menu.worker-docs',
      expanded: false,
    }
  ];  


  

    
  //MENÚ PROYECTOS
  export const MENU_PROJECTS: NbMenuItem[] = [
    {
      title: 'Proyectos',
      icon: { icon: 'fa-project-diagram', pack: 'fas' },
      link: '/projects',
      data: 'menu.projects',
      expanded: false,
    }
  ];  

    
  //MENÚ SOCIOS
  export const MENU_MEMBERS: NbMenuItem[] = [
    {
      title: 'Socios',
      icon: { icon: 'fa-user-check', pack: 'fas' },
      link: '/members',
      data: 'menu.members',
      expanded: false,
    }
  ];  

  //MENÚ USUARIOS (PACIENTES)
  export const MENU_PATIENTS: NbMenuItem[] = [
    {
      title: 'Usuarios',
      icon: { icon: 'fa-user', pack: 'fas' },
      link: '/users',
      data: 'menu.users',
      expanded: false,
    }
  ];  

  //MENÚ STAFF
  export const MENU_STAFF: NbMenuItem[] = [
    {
      title: 'Trabajadores',
      icon: { icon: 'fa-user-check', pack: 'fas' },
      link: '/staff',
      data: 'menu.staff',
      expanded: false,
    }
  ];  

  
  
    
  //MENÚ RECIBOS
  export const MENU_RECEIPTS: NbMenuItem[] = [
    {
      title: 'Recibos',
      icon: { icon: 'fa-receipt', pack: 'fas' },
      link: '/members/receipts',
      data: 'menu.receipts',
      expanded: false,
    }
  ];  
  
    
  //MENÚ FACTURAS
  export const MENU_INVOICES: NbMenuItem[] = [
    {
      title: 'Facturas',
      icon: { icon: 'fa-file-invoice', pack: 'fas' },
      link: '/invoices',
      data: 'menu.invoices',
      expanded: false,
    }
  ];  




  //MENÜ PSICOLOGÍA
  export const MENU_PSICO: NbMenuItem[] = [
    {
      title: 'Docs Psico',
      icon: { icon: 'fa-file-contract', pack: 'fas' },
      link: '/worker-docs/psico',
      data: 'menu.worker-psico',
      expanded: false,
    },
    {
      title: 'PAI Psicología',
      icon: { icon: 'fa-file-circle-check', pack: 'fas' },
      link: '/pai-psico',
      data: 'menu.pai-psico',
      expanded: false,
    }
  ];  


  

  //MENÜ TRABSOCIAL
  export const MENU_SOCIAL_WORKER: NbMenuItem[] = [
    {
      title: 'Trab. Social',
      icon: { icon: 'fa-people-arrows', pack: 'fas' },
      link: '/social-worker-docs',
      data: 'menu.social-worker',
      expanded: false,
    },
    {
      title: 'PAI Trab. Social',
      icon: { icon: 'fa-file-circle-check', pack: 'fas' },
      link: '/pai-social',
      data: 'menu.pai-social',
      expanded: false,
    }
  ];  


   

  //MENÜ FISIO
  export const MENU_FISIO: NbMenuItem[] = [
    {
      title: 'PAI Fisioterapia',
      icon: { icon: 'fa-file-circle-check', pack: 'fas' },
      link: '/pai-fisio',
      data: 'menu.pai-fisio',
      expanded: false,
    }
  ];  

  
     

  //MENÜ TOCUPA
  export const MENU_TOCUPA: NbMenuItem[] = [
    {
      title: 'PAI Terapia Ocupacional',
      icon: { icon: 'fa-file-circle-check', pack: 'fas' },
      link: '/pai-tocupa',
      data: 'menu.pai-tocupa',
      expanded: false,
    }
  ];  

  
  //MENÜ ENFER
  export const MENU_ENFER: NbMenuItem[] = [
    {
      title: 'PAI Enfermería',
      icon: { icon: 'fa-file-circle-check', pack: 'fas' },
      link: '/pai-enfer',
      data: 'menu.pai-enfer',
      expanded: false,
    }
  ];  

  
    //MENÜ COMPLETO
    export const MENU_PAI: NbMenuItem[] = [
      {
        title: 'PAI',
        icon: { icon: 'fa-file-circle-check', pack: 'fas' },
        link: '/pai',
        data: 'menu.pai',
        expanded: false,
      }
    ];  

  //MENÜ ATENCIONES
  export const MENU_ATENCIONES: NbMenuItem[] = [
    {
      title: 'Atenciones',
      icon: { icon: 'fa-phone', pack: 'fas' },
      link: '/atenciones',
      data: 'menu.atenciones',
      expanded: false,
    }
  ];  


      
  //MENÚ AGENDA
  export const MENU_AGENDA: NbMenuItem[] = [
    {
      title: 'Agenda',
      icon: { icon: 'fa-address-book', pack: 'fas' },
      link: '/agenda',
      data: 'menu.agenda',
      expanded: false,
    }
  ];  
  
    

      
  //MENÚ TRANSPORT
  export const MENU_TRANSPORT: NbMenuItem[] = [
    
  {
    title: 'Faltas',
    icon: { icon: 'fa-xmark', pack: 'fas' },
    link: '/absences-worker',
    data: 'menu.absences-worker',
    expanded: false,
  }
  ];  
  
    