import { K8sService } from '../../k8s.service';
import { NotificationService } from '../../notification.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-service-list',
  templateUrl: './service-list.component.html',
  styleUrls: ['./service-list.component.scss']
})
export class ServiceListComponent implements OnInit {
  services: any;

  constructor(
    private service: K8sService,
    private notification: NotificationService
  ) { }

  ngOnInit() {
    this.service.getServices().subscribe(r => this.services = r.services, error => {
      this.notification.sendError(error);
    });
  }

}
