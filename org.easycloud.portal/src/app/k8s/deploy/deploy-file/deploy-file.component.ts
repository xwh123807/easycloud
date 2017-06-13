import { K8sService } from '../../k8s.service';
import { NotificationService } from '../../notification.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-deploy-file',
  templateUrl: './deploy-file.component.html',
  styleUrls: ['./deploy-file.component.scss']
})
export class DeployFileComponent implements OnInit {
  constructor(
    private service: K8sService,
    private notification: NotificationService,
    private router: Router
  ) { }

  ngOnInit() {
  }

  upload(file: any): void {
    this.service.appDeploymentFromFile(file)
      .subscribe(r => { this.router.navigate(['/k8s', 'pods']); }, e => this.notification.sendError(e));
  }

  close(): void {
    this.router.navigate(['/k8s', 'pods']);
  }
}
