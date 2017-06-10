import { K8sService } from '../../k8s.service';
import { NotificationService } from '../../notification.service';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-pod-list',
  templateUrl: './pod-list.component.html',
  styleUrls: ['./pod-list.component.scss']
})
export class PodListComponent implements OnInit {
  @Input() pods: any;

  constructor(
    private service: K8sService,
    private notification: NotificationService
  ) { }

  ngOnInit() {
    if (!this.pods) {
      this.service.getPods().subscribe(r => this.pods = r, error => {
        this.notification.sendError(error);
      });
    }
  }

}
