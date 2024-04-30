import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { NbGlobalPhysicalPosition, NbPopoverDirective, NbToastrConfig } from '@nebular/theme';
import * as moment from 'moment';
import { Subscription } from 'rxjs';
import { NotificationsService } from '../../services/notifications.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit, OnDestroy {
  @ViewChild(NbPopoverDirective) popover: NbPopoverDirective;
  notifications: any[] = [];
  hiddenPopover = true;
  navigationEvents: Subscription;

  constructor(
    private router: Router,
    private notificationsService: NotificationsService,
  ) { }

  ngOnInit(): void {

    this.findMyNotifications(true);

    if(!this.navigationEvents)
      this.navigationEvents = this.router.events.subscribe((val) => {
        // see also
        if (val instanceof NavigationEnd) {
          this.findMyNotifications(false);
        }
      });
  }

  ngOnDestroy() {
    this.navigationEvents.unsubscribe();
  }

  findMyNotifications(schedule : boolean) {
    
    this.notificationsService.findMyNotifications().subscribe(
      res => {
        this.notifications = res.map(item => this.convertDates(item));
        
        if(schedule) window.setTimeout(() => this.findMyNotifications(true), 300000);
      },
      reserror => {

          console.error(JSON.stringify(reserror)); 
      }
    );
  }


  markAsRead(notification, update) {
    console.log("Marksared")
    this.notificationsService.delete(notification._id).subscribe(
      res => {
        if(update) this.findMyNotifications(false);
      },
      reserror => {

          console.error(JSON.stringify(reserror)); 
      }
    );
  }



  setpopover() 
  {
    if (this.hiddenPopover) 
    {
      this.popover.show();
      this.hiddenPopover = false;
      this.findMyNotifications(false);
    } 
    else 
    {
      this.popover.hide();
      this.hiddenPopover = true;
    }
  }

  markAllAsRead() {
    this.hiddenPopover = true;
    this.notificationsService.deleteAll().subscribe(
      res => {
        this.findMyNotifications(false);
      },
      reserror => {

          console.error(JSON.stringify(reserror)); 
      }
    );
  }

  hasNotification() {
    return this.notifications.length;
  }

  resizeNum(num) {
    if (num.toString().length === 3) {
      document.getElementById('num').style.fontSize = '11px';
    } else if (num.toString().length <= 2) {
      document.getElementById('num').style.fontSize = '13px';
    } else {
      document.getElementById('num').style.fontSize = '8px';
    }
  }


  redirect(notification, event) {
    event.stopPropagation();
    this.hiddenPopover = true;
    if(notification.url)
    {
      if(notification.url.startsWith("http://") || notification.url.startsWith("https://"))
      {
        this.markAsRead(notification, false);
        window.open(notification.url, "_blank");
      }
      else
      {
        this.markAsRead(notification, false);
        this.router.navigateByUrl(notification.url);
      }
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
    return 'El ' + daysOfWeek[thedate.getDay()] + " " + this.completeZeros(thedate.getDate()) + "/" + this.completeZeros(thedate.getMonth()+1) + "/" + (thedate.getFullYear()+"") + " a las " +  this.completeZeros(thedate.getHours()) + ":" + this.completeZeros(thedate.getMinutes()) + " h.";
  }


  completeZeros(x : number) : string
  {
    if(x<=9) return "0" + x;
    else return ""+x;
  }



  convertDates(notification: any)
  {
    notification.created = this.date2Text(notification.created);
    return notification;
  }


  value2Text(thevalue : any[]) : string
  {
    let res = '';
    if(thevalue[0]) res += "desde " + thevalue[0].toLowerCase();
    if(thevalue[1]) res += " hasta " + thevalue[1].toLowerCase();
    if(thevalue[2]) res += " - TRANSPORTE: " + thevalue[2];
    return res;
  }

}
