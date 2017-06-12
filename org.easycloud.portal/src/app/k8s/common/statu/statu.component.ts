import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-statu',
  templateUrl: './statu.component.html',
  styleUrls: ['./statu.component.scss']
})
export class StatuComponent implements OnInit {
  /**
   * "podStatus": {
     "podPhase": "Running",
     "containerStates": [
      {
       "running": {
        "startedAt": "2017-05-15T07:49:55Z"
       }
      }
     ]
    }
   */
  @Input() status: any;

  /**
   * <span [ngClass]="pod.podStatus.podPhase==='Running' ? 'fa fa-check-circle text-navy' : 'fa fa-times'"></span>
   */
  statusClass: string;

  constructor() { }

  ngOnInit() {
    if (this.status) {
      if (this.status.podPhase === 'Running') {
        this.statusClass = 'fa fa-check-circle text-navy';
      } else {
        this.statusClass = 'fa fa-warning text-danger';
      }
    }
  }

}
