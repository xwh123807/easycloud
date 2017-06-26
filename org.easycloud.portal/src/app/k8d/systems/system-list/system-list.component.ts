import {Component, OnInit} from '@angular/core';
import {KubeService} from "../../services/kube.service";

@Component({
    selector: 'app-system-list',
    templateUrl: './system-list.component.html',
    styleUrls: ['./system-list.component.scss']
})
export class SystemListComponent implements OnInit {
    /**
     * 系统
     * @type {Array}
     */
    systems: any[] = [];

    constructor(private service: KubeService) {
    }

    ngOnInit() {
        this.service.getSystems('default').subscribe(r => {
            this.systems.push({system: r.key, items: []});
            const index = this.systems.length - 1;
            r.subscribe(item => {
                this.systems[index].items.push(item);
            });
        });
    }

    /**
     * 判断部署部件是pod还是service
     * @param pod
     */
    isService(pod: any): boolean {
        return !pod.status.phase;
    }

    /**
     * 获取部署组件的状态
     */
    getState(pod: any): string {
        let stateClass;
        if (pod.status.phase === 'Running' || this.isService(pod)) {
            stateClass = 'fa fa-check-circle text-navy';
        } else if (pod.status.phase === 'Pending') {
            stateClass = 'fa fa-adjust';
        } else {
            stateClass = 'fa fa-times-circle text-danger';
        }
        return stateClass;
    }

    /**
     * 获取在kubernetes Dashborad中打开pod和service的地址
     * @param pod
     * @returns {string}
     */
    getOpenK8sUrl(pod: any): string {
        return 'http://kube.ygsoft.com:30000/#!/' + (this.isService(pod) ? 'service' : 'pod') +
            '/' + pod.metadata.namespace + '/' + pod.metadata.name;
    }

    /**
     * 删除系统
     * @param system
     */
    deleteSystem(system: string): void {
        this.service.deleteSystem('default', system).subscribe(r => console.info(r));
    }
}
