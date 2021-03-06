import { NotificationService } from '../../notification.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss']
})
export class NotificationComponent implements OnInit {
  message: any;

  isHide = true;

  constructor(
    private notification: NotificationService
  ) { }

  ngOnInit() {
    this.notification.getMessageStream().subscribe(msg => {
      this.message = msg;
      this.isHide = false;
    });
  }

  close() {
    this.isHide = true;
  }
}
