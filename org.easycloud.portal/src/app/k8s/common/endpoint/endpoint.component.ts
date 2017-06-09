import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-endpoint',
  templateUrl: './endpoint.component.html',
  styleUrls: ['./endpoint.component.scss']
})
export class EndpointComponent implements OnInit {
  /**
   * { "host": "kubernetes", "ports": [ { "port": 443, "protocol": "TCP", "nodePort": 0 } ] }
   */
  @Input() endpoint;

  ports: Array<string>;

  constructor() { }

  ngOnInit() {
    this.ports = new Array();
    if (this.endpoint) {
      this.endpoint.ports.map(item => {
        this.ports.push(this.endpoint.host + ':' + item.port + ' ' + item.protocol);
        if (item.nodePort !== 0) {
          this.ports.push(this.endpoint.host + ':' + item.nodePort + ' ' + item.protocol);
        }
      });
    }
  }

}
