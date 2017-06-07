import { K8sService } from '../../k8s.service';
import { Component, OnInit } from '@angular/core';
import 'rxjs/add/operator/do';

@Component({
  selector: 'app-node-list',
  templateUrl: './node-list.component.html',
  styleUrls: ['./node-list.component.scss']
})
export class NodeListComponent implements OnInit {
  nodes: any;

  constructor(
    private service: K8sService
  ) { }

  ngOnInit() {
    this.service.getNodes().subscribe(r => { this.nodes = r });
  }

}
