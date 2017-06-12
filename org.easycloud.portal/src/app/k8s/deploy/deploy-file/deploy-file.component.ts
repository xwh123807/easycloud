import { K8sService } from '../../k8s.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-deploy-file',
  templateUrl: './deploy-file.component.html',
  styleUrls: ['./deploy-file.component.scss']
})
export class DeployFileComponent implements OnInit {
  constructor(
    private service: K8sService
  ) { }

  ngOnInit() {
  }

  upload(file: any): void {
    this.service.appDeploymentFromFile(file).subscribe(r => console.info(r));
  }
}
