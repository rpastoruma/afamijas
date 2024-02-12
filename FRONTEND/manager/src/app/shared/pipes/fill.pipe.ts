import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'fill'
})
export class FillPipe implements PipeTransform {

  transform(value: any): any {
    return Array.from(new Array(value), (val, index) => index + 1);
  }

}
