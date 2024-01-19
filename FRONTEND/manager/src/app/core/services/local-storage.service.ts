import { Injectable } from '@angular/core';
import { reviver } from 'src/app/shared/models/models';

@Injectable()
export class LocalStorageService {

  setObject<T extends Object>(key: string, value: T) {
    localStorage.setItem(key, JSON.stringify(value));
  }

  setString(key: string, value: string) {
    localStorage.setItem(key, value);
  }

  getObject(key: string): any {
    const item = localStorage.getItem(key);
    if (item != null) {
      return JSON.parse(item, reviver);
    }
    return null;
  }

  getString(key: string): string {
    return localStorage.getItem(key)!;
  }

  removeItem(key: string) {
    localStorage.removeItem(key);
    // ('delete key', key);
  }


  clear() {
    localStorage.clear();
  }

}
