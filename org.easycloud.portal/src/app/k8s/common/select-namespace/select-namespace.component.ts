import { K8sService } from '../../k8s.service';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-select-namespace',
  templateUrl: './select-namespace.component.html',
  styleUrls: ['./select-namespace.component.scss']
})
export class SelectNamespaceComponent implements OnInit {
  namespaces: any;

  @Input() value: any;

  constructor(
    private service: K8sService
  ) { }

  ngOnInit() {
    this.service.getNamespaces().subscribe(r => this.namespaces);
  }

}
