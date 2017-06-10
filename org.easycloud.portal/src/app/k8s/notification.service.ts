import { Injectable } from '@angular/core';
import { Subject } from 'rxjs/Subject';

const MSG_TYPE = {
  error: 'error',
  info: 'info'
};

@Injectable()
export class NotificationService {
  messageStream: Subject<any> = new Subject();

  constructor() { }

  sendError(message: string): void {
    this.messageStream.next({ type: MSG_TYPE.error, message: message });
  }

  getMessageStream(): Subject<any> {
    return this.messageStream;
  }
}
