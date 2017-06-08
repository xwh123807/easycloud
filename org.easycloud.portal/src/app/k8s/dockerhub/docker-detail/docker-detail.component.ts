import { DockerHubService } from '../docker-hub.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';

@Component({
  selector: 'app-docker-detail',
  templateUrl: './docker-detail.component.html',
  styleUrls: ['./docker-detail.component.scss']
})
export class DockerDetailComponent implements OnInit {
  tags: any;

  dockerHubServer = 'dockerhub.ygsoft.com:5000';

  constructor(
    private route: ActivatedRoute,
    private service: DockerHubService
  ) { }

  ngOnInit() {
    this.route.params
      .switchMap((params: Params) => this.service.getTags(params['name']))
      .subscribe(r => this.tags = r);
  }

}
