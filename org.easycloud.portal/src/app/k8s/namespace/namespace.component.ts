import { K8sService } from '../k8s.service';
import { NotificationService } from '../notification.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-namespace',
  templateUrl: './namespace.component.html',
  styleUrls: ['./namespace.component.scss']
})
export class NamespaceComponent implements OnInit {
  namespaces: any;

  constructor(
    private service: K8sService,
    private notification: NotificationService
  ) { }

  ngOnInit() {
    this.service.getNamespaces().subscribe(r => this.namespaces = r, error => {
      this.notification.sendError(error);
    });
  }

}
