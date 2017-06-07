import { JsonPipe } from '@angular/common';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'kv'
})
export class KvPipe implements PipeTransform {

  transform(value: any): string {
    if (!value) return '';
    const props: Array<string> = Object.getOwnPropertyNames(value);
    let tmp = '';
    props.map(name => {
      tmp += name + ': ' + value[name] + '\r\n';
    });
    return tmp;
  }

}
