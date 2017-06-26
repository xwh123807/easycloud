import { Pipe, PipeTransform } from '@angular/core';
/** Base for binary prefixes */
const base = 1024;

/** Names of the suffixes where I-th name is for base^I suffix. */
const powerSuffixes = ['', 'Ki', 'Mi', 'Gi', 'Ti', 'Pi'];

@Pipe({
  name: 'memory'
})
export class MemoryPipe implements PipeTransform {

  /**
   * 将bytes转换为最近的单位,Ki、Mi、Gi
   */
  transform(value: number): string {
    let divider = 1;
    let power = 0;

    while (value / divider > base && power < powerSuffixes.length - 1) {
      divider *= base;
      power += 1;
    }
    const formatted = (value / divider).toFixed(2);
    const suffix = powerSuffixes[power];
    return suffix ? `${formatted} ${suffix}` : formatted;
  }

}
