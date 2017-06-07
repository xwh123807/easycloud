import { K8sService } from '../../k8s.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, Params } from '@angular/router';

@Component({
  selector: 'app-pod-detail',
  templateUrl: './pod-detail.component.html',
  styleUrls: ['./pod-detail.component.scss']
})
export class PodDetailComponent implements OnInit {
  pod: any;

  constructor(
    private service: K8sService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit() {
    this.route.params
      .switchMap((params: Params) => this.service.getPod(params['namespace'], params['name']))
      .subscribe(r => this.pod = r);
  }

}
