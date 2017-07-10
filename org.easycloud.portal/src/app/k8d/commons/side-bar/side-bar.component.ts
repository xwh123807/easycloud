import {Component, OnInit} from '@angular/core';
import {K8dConfig} from "app/k8d/k8d-config";

@Component({
    selector: 'app-side-bar',
    templateUrl: './side-bar.component.html',
    styleUrls: ['./side-bar.component.scss']
})
export class SideBarComponent implements OnInit {
    isActive = false;
    showMenu = '';
    kubernetesDashboardUrl = K8dConfig.kubernetesDashboardUrl;

    eventCalled() {
        this.isActive = !this.isActive;
    }

    addExpandClass(element: any) {
        if (element === this.showMenu) {
            this.showMenu = '0';
        } else {
            this.showMenu = element;
        }
    }

    ngOnInit(): void {
    }
}
