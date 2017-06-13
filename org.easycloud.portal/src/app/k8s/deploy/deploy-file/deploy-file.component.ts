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
  content: string;

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

  loadContent(file: any): void {
    this.service.readFile(file).subscribe(r => this.content = r, e => this.notification.sendError(e));
  }

  deploy(): void {
    this.service.appDeploymentFromFileContent(this.content)
      .subscribe(r => { window.alert('发布成功.') }, e => this.notification.sendError(e));
  }

  delete(): void {
    this.service.appUnDeploymentFromFileContent(this.content)
      .subscribe(r => { window.alert('删除成功.') }, e => this.notification.sendError(e));
  }
}
