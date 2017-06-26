import { Pipe, PipeTransform } from '@angular/core';

const UnitConversions = {
  MILLISECONDS_PER_SECOND: 1000,
  SECONDS_PER_MINUTE: 60,
  MINUTES_PER_HOUR: 60,
  HOURS_PER_DAY: 24,
  DAYS_PER_MONTH: 30,
  DAYS_PER_YEAR: 365,
  MONTHS_PER_YEAR: 12,
};

const i18n = {
  /** @export {string} @desc Time units label, a single second.*/
  MSG_TIME_UNIT_SECOND_LABEL: 'a second',
  /** @export {string} @desc Time units label, many seconds (plural).*/
  MSG_TIME_UNIT_SECONDS_LABEL: 'seconds',
  /** @export {string} @desc Time units label, a single minute.*/
  MSG_TIME_UNIT_MINUTE_LABEL: 'a minute',
  /** @export {string} @desc Time units label, many minutes (plural).*/
  MSG_TIME_UNIT_MINUTES_LABEL: 'minutes',
  /** @export {string} @desc Time units label, a single hour.*/
  MSG_TIME_UNIT_HOUR_LABEL: 'an hour',
  /** @export {string} @desc Time units label, many hours (plural).*/
  MSG_TIME_UNIT_HOURS_LABEL: 'hours',
  /** @export {string} @desc Time units label, a single day.*/
  MSG_TIME_UNIT_DAY_LABEL: 'a day',
  /** @export {string} @desc Time units label, many days (plural).*/
  MSG_TIME_UNIT_DAYS_LABEL: 'days',
  /** @export {string} @desc Time units label, a single month.*/
  MSG_TIME_UNIT_MONTH_LABEL: 'a month',
  /** @export {string} @desc Time units label, many months (plural).*/
  MSG_TIME_UNIT_MONTHS_LABEL: 'months',
  /** @export {string} @desc Time units label, a single year.*/
  MSG_TIME_UNIT_YEAR_LABEL: 'a year',
  /** @export {string} @desc Time units label, many years (plural).*/
  MSG_TIME_UNIT_YEARS_LABEL: 'years',
  /** @export {string} @desc Label for relative time that did not happened yet.*/
  MSG_TIME_NOT_YET_LABEL: `-`,
};

const Units = {
  SECOND: [i18n.MSG_TIME_UNIT_SECOND_LABEL, i18n.MSG_TIME_UNIT_SECONDS_LABEL],
  MINUTE: [i18n.MSG_TIME_UNIT_MINUTE_LABEL, i18n.MSG_TIME_UNIT_MINUTES_LABEL],
  HOUR: [i18n.MSG_TIME_UNIT_HOUR_LABEL, i18n.MSG_TIME_UNIT_HOURS_LABEL],
  DAY: [i18n.MSG_TIME_UNIT_DAY_LABEL, i18n.MSG_TIME_UNIT_DAYS_LABEL],
  MONTH: [i18n.MSG_TIME_UNIT_MONTH_LABEL, i18n.MSG_TIME_UNIT_MONTHS_LABEL],
  YEAR: [i18n.MSG_TIME_UNIT_YEAR_LABEL, i18n.MSG_TIME_UNIT_YEARS_LABEL],
};

@Pipe({
  name: 'age'
})
export class AgePipe implements PipeTransform {

  transform(value: any, args?: any): any {

    // Current and given times in miliseconds.
    let currentTime = (new Date()).getTime();
    let givenTime = (new Date(value)).getTime();

    // Time differences between current time and given time in specific units.
    let diffInMilliseconds = currentTime - givenTime;
    let diffInSeconds = Math.floor(diffInMilliseconds / UnitConversions.MILLISECONDS_PER_SECOND);
    let diffInMinutes = Math.floor(diffInSeconds / UnitConversions.SECONDS_PER_MINUTE);
    let diffInHours = Math.floor(diffInMinutes / UnitConversions.MINUTES_PER_HOUR);
    let diffInDays = Math.floor(diffInHours / UnitConversions.HOURS_PER_DAY);
    let diffInMonths = Math.floor(diffInDays / UnitConversions.DAYS_PER_MONTH);
    let diffInYears = Math.floor(diffInDays / UnitConversions.DAYS_PER_YEAR);

    // Returns relative time value. Only biggest unit will be taken into consideration, so if time
    // difference is 2 days and 15 hours, only '2 days' string will be returned.
    if (diffInMilliseconds < -1000) {
      // Display NOT_YET only when diff is lower than -1000ms. To show NOW message for
      // times now() +- 1 second. This is because there may be a small desync in server time
      // computation.
      return 'time not yet';
    } else if (diffInSeconds < 1) {
      return this.formatOutputTimeString_(0, Units.SECOND);
    } else if (diffInMinutes < 1) {
      return this.formatOutputTimeString_(diffInSeconds, Units.SECOND);
    } else if (diffInHours < 1) {
      return this.formatOutputTimeString_(diffInMinutes, Units.MINUTE);
    } else if (diffInDays < 1) {
      return this.formatOutputTimeString_(diffInHours, Units.HOUR);
    } else if (diffInMonths < 1) {
      return this.formatOutputTimeString_(diffInDays, Units.DAY);
    } else if (diffInYears < 1) {
      return this.formatOutputTimeString_(diffInMonths, Units.MONTH);
    } else {
      return this.formatOutputTimeString_(diffInYears, Units.YEAR);
    }
  }

  formatOutputTimeString_(timeValue, timeUnit): string {
    if (timeValue > 1 || timeValue === 0) {
      return `${timeValue} ${timeUnit[1]}`;
    } else {
      return timeUnit[0];
    }
  }
}
