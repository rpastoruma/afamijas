import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { NbGlobalPhysicalPosition, NbPopoverDirective, NbToastrConfig } from '@nebular/theme';
import * as moment from 'moment';
import { Subscription } from 'rxjs';
import { ToastTime } from 'src/app/shared/models/models';
import { NotificationsService } from '../../services/notifications.service';

@Component({
  selector: 'app-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit, OnDestroy {
  @ViewChild(NbPopoverDirective) popover: NbPopoverDirective;
  notifications;
  hiddenPopover = true;
  navigationEvents: Subscription;

  constructor(
    private router: Router,
    private notificationService: NotificationsService,
  ) { }

  ngOnInit(): void {

    this.getNotifications();

    this.navigationEvents = this.router.events.subscribe((val) => {
      // see also
      if (val instanceof NavigationEnd) {
        this.getNotifications();
      }
    });
  }

  ngOnDestroy() {
    this.navigationEvents.unsubscribe();
  }

  getNotifications() {
    this.notificationService.findMyNotifications().subscribe(
      res => {
        this.notifications = res;
      },
      _ => { 
        /*
        const config: Partial<NbToastrConfig> = {
          status: 'danger', destroyByClick: true, duration: ToastTime.TIME,
          hasIcon: true, position: NbGlobalPhysicalPosition.TOP_RIGHT, preventDuplicates: false
        };
        this.toastService.show("MESSAGE",  "TITLE", config);
        */
      }
    );
  }

  markAsReadAll() {
    const notificationsfilter = this.notifications ? this.notifications.filter(item => item.closetype === 'VOLATILE'
    || item.closetype === 'PERMANENT') : null;
    const ids = notificationsfilter ? notificationsfilter.map(item => item.id) : null;
    if (ids) {
      this.markAsRead(ids);
    }
  }

  setpopover() {
    if (this.hiddenPopover) {
      this.popover.show();
      this.hiddenPopover = false;
      this.getNotifications();
    } else {
      this.popover.hide();
      const ids = this.getIdsVolatiles();
      if (ids && ids.length > 0) { this.markAsRead(ids); }
      this.hiddenPopover = true;
    }
  }

  getIdsVolatiles() {
    const ids = [];
    if (this.notifications) {
      for (const item of this.notifications) {
        if (item.closetype === 'VOLATILE' && item.status === 'A') {
          ids.push(item.id);
        }
      }
    }

    return ids;
  }



  hasNotification() {
    let num = 0;
    if (this.notifications) {
      for (const item of this.notifications) {
        if (item.status === 'A') {
          num++;
        }
      }
    }

    if (num && document.getElementById('num')) { this.resizeNum(num); }
    return num;
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

  markAsRead(ids) {
    this.notificationService.markAsRead(ids).subscribe(
      result => {
        this.getNotifications();
      }
    );
  }

  getClass(type: string) {
    let icon = '';
    switch (type) {
      case 'VOLATILE':
        icon = 'fas fa-info-circle';
        break;
      case 'PERMANENT':
        icon = 'fas fa-info-circle';
        break;
      case 'DURATION':
        icon = 'fas fa-info-circle';
        break;
      case 'ACTION':
        icon = 'fas fa-bolt';
        break;
      default:
        icon = '';
        break;
    }
    return icon;
  }

  redirect(notification) {
    /*
    switch (notification.title) {
      case 'PENDING_PUBLICATIONS':
        if (notification.objecttype === 'RESEARCHER') {
          this.router.navigate(['researchers/update/' + notification.idobject + '/PRODUCTIONS']);
        } else {
          this.router.navigate(['moderation/preresearchers/update/' + notification.idobject + '/PRODUCTIONS']);
        }
        break;
      case 'PENDING_PUBLICATIONS_CVN':
        this.router.navigate(['moderation/scientific-production/NEW/CVN/' + notification.idobject + '/' + notification.userfromfullname]);
        break;
      case 'PENDING_PUBLICATIONS_ORCID':
        this.router.navigate(['moderation/scientific-production/NEW/ORCID/' + notification.idobject + '/' + notification.userfromfullname]);
        break;
      case 'NEW_SIGNUP':
        this.router.navigate(['moderation/preresearchers/update/' + notification.idobject]);
        break;
      default:
        break;
    } */
  }

}
