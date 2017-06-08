import { Component, OnInit } from '@angular/core';
import { K8sService } from '../../k8s.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-deploy',
  templateUrl: './deploy.component.html',
  styleUrls: ['./deploy.component.scss']
})
export class DeployComponent implements OnInit {
  info: any = {
    replicas: 1,
    service: 'none'
  };

  deployData: string;

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
    private router: Router
  ) { }

  ngOnInit() {
    this.deployData = JSON.stringify(this.template);
  }

  deploy(): void {
    this.service.appDeployment(this.deployData).subscribe(r => {
      console.log(r);
      this.cancel();
    });
  }

  cancel(): void {
    this.router.navigate(['/k8s']);
  }
}
