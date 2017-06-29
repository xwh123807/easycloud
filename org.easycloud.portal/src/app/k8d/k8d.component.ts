import {Component, OnInit} from '@angular/core';
import {K8dConfig} from "./k8d-config";

@Component({
    selector: 'app-k8d',
    templateUrl: './k8d.component.html',
    styleUrls: ['./k8d.component.scss']
})
export class K8dComponent implements OnInit {
    kubernetesDashboardUrl = K8dConfig.kubernetesDashboardUrl;

    constructor() {
    }

    ngOnInit() {
    }

}
