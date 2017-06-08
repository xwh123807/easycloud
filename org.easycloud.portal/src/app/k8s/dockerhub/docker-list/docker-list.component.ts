import { DockerHubService } from '../docker-hub.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-docker-list',
  templateUrl: './docker-list.component.html',
  styleUrls: ['./docker-list.component.scss']
})
export class DockerListComponent implements OnInit {
  repos: any;

  constructor(
    private service: DockerHubService
  ) { }

  ngOnInit() {
    this.service.getCatalog().subscribe(r => this.repos = r.repositories);
  }

}
