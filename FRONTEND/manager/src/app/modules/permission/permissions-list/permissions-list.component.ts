
import { Component, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { NbDialogService, NbGlobalPhysicalPosition, NbToastrService } from '@nebular/theme';
import { PatientDTO, PermissionDTO } from 'src/app/shared/models/models';
import { PermissionsService } from 'src/app/core/services/permissions.service';
import { UsersService } from 'src/app/core/services/users.service';
import { MyPdfViewerComponent } from 'src/app/shared/components/my-pdf-viewer/my-pdf-viewer.component';


@Component({
  selector: 'app-permissions-list',
  templateUrl: './permissions-list.component.html',
  styleUrls: ['./permissions-list.component.scss']
})
export class PermissionsListComponent implements OnInit {

  page : number = 0;
  size : number = 5;
  totalPages : number = 0;
  idpatient : string;
  status : string = 'P';
  
  allPatients : PatientDTO[] = [];
  
  permissions: any[]  = []; // any => formato del listado
  permissionsObjects: PermissionDTO[]  = [];

  thePermission : PermissionDTO = {
    idpermission: '',
    idpatient: '',
    patient_fullname: '',
    patient_dni: '',
    idrelative: '',
    relative_fullname: '',
    relative_dni: '',
    type: '',
    comment: '',
    permission_url: '',
    permission_signed_url: ''
  }

  
  loadingE : boolean = false;
  isProcessing : boolean = true;




  constructor(
    public toastService: NbToastrService,
    private permissionsService : PermissionsService,
    private usersService : UsersService,
    private dialogService : NbDialogService
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
            this.getPermissions(this.page);
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
  


  getPermissions(page: number) 
  {
    this.page = page;
    this.permissionsService.getPermissions(this.page, this.size, this.idpatient, this.status).subscribe(
      res => {
        this.isProcessing = false;
        this.permissions = res.content.map(item => { return {id: item.idpermission, values: [item.type, item.comment]  }; });
        this.permissionsObjects = res.content;

        this.totalPages = res.totalPages;
      },
      error => {
        this.isProcessing = false;
        console.error("getPermissions():"+JSON.stringify(error));
        this.toastService.show("No se han podido obtener las autorizaciones por familiar.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }

  


  
  
/*
  saveRelativeAbsence()
  {
    if(this.theAbsence.from==null || this.theAbsence.to==null)
    { 
      this.toastService.show("Debes completar las fechas/horas de inicio y fin de la falta.",
      "¡Ups!", 
      { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false }
      );
      return; 
    } 
    
    //const dfrom = new Date(this.theAbsence.from);
    //const dto = new Date(this.theAbsence.to);


    if(this.theAbsence.to < this.theAbsence.from) 
    { 
      this.toastService.show("El momento de inicio de la falta no puede ser posterior a la de su fin.",
      "¡Ups!", 
      { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
      );
      return; 
    } 

    this.isProcessing = true;
    const isNew = this.theAbsence.id=='';


    this.relativeAbsencesService.saveAbsenceByRelative(this.theAbsence.id, this.theAbsence.idpatient, this.theAbsence.from, this.theAbsence.to, this.theAbsence.allday, this.theAbsence.transport, this.theAbsence.comment).subscribe(
      res => {
        this.isProcessing = false;
        this.getRelativeAbsences(0);
        this.toastService.show(this.theAbsence.id=='Falta añadida correctamente'?"":"Falta actualizada correctamente.",
                               "¡Ok!", 
                               { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );
        if(isNew) this.modal.dismissAll();
      },
      error => {
        console.error("saveAbsenceByRelative():"+JSON.stringify(error));
        this.isProcessing = false;
        this.toastService.show("No se ha podido añadir/modificar la falta.",
                               "¡Ups!", 
                               { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                              );
      }
    );


  }*/



  setPage(event) 
  {
    this.page = event;
    this.getPermissions(this.page);
  }



  filter(page: number) {
    this.getPermissions(page);
  }


  action(event) 
  {

    if (event && event[0] === 'show') 
    {
      const selected = this.permissionsObjects.find(item => item.idpermission === event[1]);
      console.log("selected==>"+JSON.stringify(selected))
      this.dialogService.open(MyPdfViewerComponent, {
        hasScroll: true,
        closeOnBackdropClick: false,
        closeOnEsc: false,
        context: {
          pdfSrc: (selected.permission_signed_url && selected.permission_signed_url.startsWith('https:'))?selected.permission_signed_url:selected.permission_url,
          forSigning : !selected.permission_signed_url,
          openExternal : true
        }
      }).onClose.subscribe(
        res => {
            if(res != 'close') 
            {
              if(res.url && res.url.startsWith('https'))
                this.signPermission(this.idpatient, event[1], res.url);
              else
              {
                console.error("action():"+JSON.stringify(res));
                this.toastService.show("No se ha podido firmar la autorización correctamente.",
                "¡Ups!", 
                { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
                );
              }
            }
              
        }
      );

      
      
      
    } 
  }


  signPermission(idpatient: string, idpermission: string, url_signed_file : string)
  {
    this.permissionsService.signPermission(idpatient, idpermission, url_signed_file).subscribe(
      res => {
        this.isProcessing = false;
        this.toastService.show("Autorización firmada correctamente.",
            "¡Ok!", 
            { status: 'success', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
          );
        this.getPermissions(0);
      },
      error => {
        this.isProcessing = false;
        console.error("signPermission():"+JSON.stringify(error));
        this.toastService.show("No se ha podido firmar la autorización correctamente.",
          "¡Ups!", 
          { status: 'danger', destroyByClick: true, duration: 3000,  hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false  }
         );
      }
    );
  }


}
