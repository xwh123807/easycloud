import { Component, OnInit } from '@angular/core';
import { K8sService } from '../../k8s.service';
import { NotificationService } from '../../notification.service';
import { FormGroup, FormBuilder, FormArray } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-deploy',
  templateUrl: './deploy.component.html',
  styleUrls: ['./deploy.component.scss']
})
export class DeployComponent implements OnInit {
  info: any = {
    replicas: 1,
    service: 'none',
    portMappings: [
      { port: '', targetPort: '', protocol: 'TCP' }
    ],
    labels: [
      { key: 'app', value: '' },
      { key: 'version', value: '' }
    ],
    namespace: 'default'
  };

  deployData: any;

  template: any = {
    "name": "redis",
    "containerImage": "dockerhub.ygsoft.com:5000/redis:3.2.6",
    "imagePullSecret": null,
    "containerCommand": null,
    "containerCommandArgs": null,
    "replicas": 1,
    "portMappings": [],
    "variables": [],
    "isExternal": false,
    "description": null,
    "namespace": "kube-system",
    "memoryRequirement": null,
    "cpuRequirement": null,
    "labels": [
      {
        "key": "app",
        "value": "redis"
      },
      {
        "key": "version",
        "value": "3.2.6"
      }
    ],
    "runAsPrivileged": false
  };

  constructor(
    private service: K8sService,
    private router: Router,
    private notification: NotificationService
  ) {
  }

  addEndPoint(): void {
    this.info.portMappings.push({ protocol: 'TCP' });
  }

  addLabel(): void {
    this.info.labels.push({});
  }

  ngOnInit() {
    this.deployData = JSON.stringify(this.template);
  }

  nameChange(): void {
    this.info.labels[0].value = this.info.name;
  }

  imageChange(): void {
    let str: string = this.info.containerImage;
    let label: string = str.substring(str.lastIndexOf(":") + 1);
    this.info.labels[1].value = label;
  }

  deploy(): void {
    let data = {
      name: this.info.name,
      containerImage: this.info.containerImage,
      replicas: this.info.replicas,
      imagePullSecret: null,
      containerCommand: null,
      containerCommandArgs: null,
      namespace: 'default',
      isExternal: false,
      portMappings: [],
      labels: this.info.labels
    };
    console.info(data);
    this.service.appDeployment(data).subscribe(r => {
      this.router.navigate(['/k8s', 'pods']);
    }, e => this.notification.sendError(e));
  }

  deployFile(): void {
    this.service.appDeployment(this.deployData).subscribe(r => {
      console.log(r);
      this.cancel();
    });
  }

  cancel(): void {
    this.router.navigate(['/k8s']);
  }
}
