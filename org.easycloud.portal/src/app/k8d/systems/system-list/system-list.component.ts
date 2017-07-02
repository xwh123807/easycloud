import {Component, OnInit} from '@angular/core';
import {Kind, KubeService} from "../../services/kube.service";
import {K8dConfig} from "../../k8d-config";

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

    status: any[];

    constructor(private service: KubeService) {
    }

    ngOnInit() {
        this.refresh();
    }

    refresh() {
        this.systems = [];
        this.status = null;
        this.service.getSystems('default').subscribe(r => {
            this.systems.push({system: r.key, items: []});
            const index = this.systems.length - 1;
            r.subscribe(item => {
                this.systems[index].items.push(item);
                // console.info(item);
            });
        });
    }

    /**
     * 判断部署部件是pod还是service
     * @param pod
     */
    isPod(pod: any): boolean {
        return pod.status && pod.status.phase;
    }

    /**
     * 获取部署组件的状态
     */
    getState(pod: any): string {
        let stateClass;
        if (pod.kind === Kind.Pod) {
            if (pod.status.phase === 'Running') {
                stateClass = 'fa fa-check-circle text-navy';
            } else if (pod.status.phase === 'Pending') {
                stateClass = 'fa fa-adjust';
            } else {
                stateClass = 'fa fa-times-circle text-danger';
            }
        } else {
            stateClass = 'fa fa-check-circle text-navy';
        }

        return stateClass;
    }

    /**
     * 获取可执行的操作
     * @param pod
     * @returns {Array}
     */
    getActions(pod: any): string[] {
        const items = [];
        switch (pod.kind) {
            case Kind.Service:
                const ports: any[] = pod.spec.ports || [];
                ports.forEach(port => {
                    if (port.nodePort) {
                        items.push({label: port.nodePort, action: K8dConfig.kubernetesMasterUrl + ':' + port.nodePort});
                    }
                });
                break;
            case Kind.Ingress:
                const rules = pod.spec.rules || [];
                rules.forEach(rule => {
                    items.push({label: rule.host, action: 'http://' + rule.host});
                });
                break;
            case Kind.ConfigMap:
                break;
        }
        return items;
    }

    /**
     * 获取在kubernetes Dashborad中打开pod和service的地址
     * @param pod
     * @returns {string}
     */
    getOpenK8sUrl(pod: any): string {
        return K8dConfig.kubernetesDashboardUrl + '/#!/' + pod.kindName.toLowerCase() +
            '/' + pod.metadata.namespace + '/' + pod.metadata.name;
    }

    /**
     * 删除系统
     * @param system
     */
    deleteSystem(system: string): void {
        this.status = [];
        this.service.deleteSystem('default', system).subscribe(r => this.status.push(r));
    }
}
