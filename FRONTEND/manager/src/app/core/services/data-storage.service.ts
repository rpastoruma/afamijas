import { Injectable } from '@angular/core';
import { Subscription } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataStorageService {
  public data: any;
  public userInfo: any;
  public headerSubscription: Subscription | null = null;

  constructor() { }

  getData() {
    return this.data;
  }

  setData(value: any) {
    this.data = value;
  }

  getUserInfo() {
    return this.userInfo;
  }

  setUserInfo(value: any) {
    this.userInfo = value;
  }

  getHeaderSubscription() {
    return this.headerSubscription;
  }

  setHeaderSubscription(value: any) {
    this.headerSubscription = value;
  }
}
