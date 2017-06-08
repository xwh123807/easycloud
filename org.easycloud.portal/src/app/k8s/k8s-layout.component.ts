import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-k8s-layout',
  templateUrl: './k8s-layout.component.html',
  styleUrls: ['./k8s-layout.component.scss']
})
export class K8sLayoutComponent implements OnInit {

  constructor(
    private router: Router
  ) { }

  ngOnInit() {
    this.router.navigate(['/k8s/nodes']);
  }

}
