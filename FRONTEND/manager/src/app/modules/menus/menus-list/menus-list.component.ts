import { Component, OnInit} from '@angular/core';
import { NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { PatientDTO, MenuDTO } from 'src/app/shared/models/models';
import { MenusService } from 'src/app/core/services/menus.service';
import { UsersService } from 'src/app/core/services/users.service';

@Component({
  selector: 'app-menus-list',
  templateUrl: './menus-list.component.html',
  styleUrls: ['./menus-list.component.scss']
})
export class MenusListComponent implements OnInit {

  totalPages : number = 0;
  idpatient : string;

  allPatients : PatientDTO[] = [];
  
  menus: any[]  = []; // any => formato del listado
  menusObjects: MenuDTO[]  = [];

  themenu : MenuDTO = {
    id: '',
    type: '',
    description: '',
    menu_url: '',
    from: undefined,
    to: undefined,
  }

  
  loadingE : boolean = false;
  isProcessing : boolean = true;


  constructor(
    public toastService: NbToastrService,
    private menusService : MenusService,
    private usersService : UsersService,
  ) { }

  ngOnInit(): void {
    this.getPatients();
    
  }

  getPatients()
  {

      this.usersService.getPatientsByRelative().subscribe(
        res => {
          this.allPatients = res;
          if(this.allPatients && this.allPatients.length>0) 
          {
            this.idpatient = this.allPatients[0].id;
            this.getMenu();
          }
        },
        error => 
        {
          console.error("getPatients():"+JSON.stringify(error));
          this.toastService.show("No se pueden obtener los usuarios asociados a este familiar.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
        }
      );
  }
  


  getMenu() 
  {
    this.menusService.getMenu(this.idpatient).subscribe(
      res => {
        this.isProcessing = false;
        this.menus = res.map(item => { return {id: item.id, values: [this.date2Text(item.from), this.date2Text(item.to), item.type, item.description]  }; });
        this.menusObjects = res.map( item => this.convertDates(item) );
      },
      error => {
        this.isProcessing = false;
        console.error("getMenu():"+JSON.stringify(error));
        this.toastService.show("No se ha podido obtener la información de los menús.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }

  




  
  


  

  action(event) 
  {
    if (event && event[0] === 'show') 
    {
      const selected = this.menusObjects.find(item => item.id === event[1]);
      window.open(selected.menu_url);
    } 
  }


  //[2024,3,1,13,0,...] --> Date
  localDateTime2Date(thedate : number[]) : Date
  {
    if(!thedate || thedate.length==0) return null;
    let sdate = '';
    if(thedate[0]) sdate += thedate[0];
    if(thedate[1]) sdate += ('-' + this.completeZeros(thedate[1]));
    if(thedate[2]) sdate += ('-' + this.completeZeros(thedate[2]));
    if(thedate[3] || thedate[3]==0) sdate += ('T' + this.completeZeros(thedate[3]));
    if(thedate[4] || thedate[4]==0) sdate += (':' + this.completeZeros(thedate[4]));
    if(thedate[5] || thedate[5]==0) sdate += (':' + this.completeZeros(thedate[5]));
    if(thedate[6] || thedate[6]==0) sdate += ('.' + thedate[6]);
    return new Date(Date.parse(sdate));
  }

  
  date2Text(thelocaldatetime : number[])
  {
    let thedate : Date = this.localDateTime2Date(thelocaldatetime);
    if(!thedate) return '';
    let daysOfWeek = [ 'domingo', 'lunes', 'martes', 'miércoles', 'jueves', 'viernes', 'sábado' ];
    const dayOfWeek = daysOfWeek[thedate.getDay()];
    return 'El ' + daysOfWeek[thedate.getDay()] + " " + this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"");
  }


  completeZeros(x : number) : string
  {
    if(x<=9) return "0" + x;
    else return ""+x;
  }



  convertDates(menu: any)
  {
    menu.from = this.localDateTime2Date(menu.from);
    menu.to = this.localDateTime2Date(menu.to);
    return menu
  }



}
