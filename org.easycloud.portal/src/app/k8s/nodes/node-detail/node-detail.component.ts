import { K8sService } from '../../k8s.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, Params } from '@angular/router';
import 'rxjs/add/operator/switchMap';

@Component({
  selector: 'app-node-detail',
  templateUrl: './node-detail.component.html',
  styleUrls: ['./node-detail.component.scss']
})
export class NodeDetailComponent implements OnInit {
  node: any;

  constructor(
    private service: K8sService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.route.params.switchMap((params: Params) => {
      const name = params['name'];
      return this.service.getNode(name);
    }).subscribe(r => this.node = r);
  }

}
