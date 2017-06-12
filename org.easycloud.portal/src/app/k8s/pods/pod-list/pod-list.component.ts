import { K8sService } from '../../k8s.service';
import { NotificationService } from '../../notification.service';
import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-pod-list',
  templateUrl: './pod-list.component.html',
  styleUrls: ['./pod-list.component.scss']
})
export class PodListComponent implements OnInit {
  @Input() pods: any;

  constructor(
    private service: K8sService,
    private notification: NotificationService
  ) { }

  ngOnInit() {
    if (!this.pods) {
      this.service.getPods().subscribe(r => this.pods = r, error => {
        this.notification.sendError(error);
      });
    }
  }

  /**
   *     "podStatus": {
     "status": "failed",
     "podPhase": "Pending",
     "containerStates": [
      {
       "waiting": {
        "reason": "ImagePullBackOff",
        "message": "Back-off pulling image \"redis:dfs\""
       }
      }
     ]
    }
   */
  getPodStatus(pod: any): string {
    let status = pod.podStatus.podPhase;
    if (status === 'Pending') {
      const states = pod.podStatus.containerStates;
      if (states && states.length > 0 && states[0].waiting) {
        status = 'Waiting: ' + states[0].waiting.reason;
      }
    }
    return status;
  }

  /**
   *
   */
  getPodState(pod: any): string {
    let stateClass;
    if (pod.podStatus.podPhase === 'Running') {
      stateClass = 'fa fa-check-circle text-navy';
    } else if (pod.podStatus.podPhase === 'Pending') {
      stateClass = pod.warnings.length === 0 ? 'fa fa-adjust' : 'fa fa-times-circle text-danger';
    }
    return stateClass;
  }
}
