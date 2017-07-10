import {Injectable} from '@angular/core';
import {Http, RequestOptions, Headers} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/from';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/groupBy';
import "rxjs/add/operator/mergeMap";
import "rxjs/add/operator/concat";
import "rxjs/add/operator/switchMap";
import {Subject} from "rxjs/Subject";
import "rxjs/add/operator/mergeAll";
import "rxjs/add/operator/merge";
import "rxjs/add/operator/delay";

/**
 * Kubernetes 组件类型
 */
export enum Kind {
    Pod,
    Service,
    Deployment,
    ReplicaSet,
    ReplicationController,
    StatefulSet,
    DaemonSet,
    ServiceAccount,
    ConfigMap,
    ClusterRoleBinding,
    Ingress,
    PersistentVolume,
    PersistentVolumeClaim
}

/**
 * 部署状态类
 */
export class Status {
    public success: boolean;
    public message: string;

    constructor(public kind: Kind,
                public kindName: string,
                public name: string,
                public namespace: string) {
    }

    ok(): void {
        this.success = true;
        this.message = 'OK';
    }

    error(message: string): void {
        this.success = false;
        this.message = message;
    }
}

/**
 * Kubernetes部署单元
 */
export class K8Unit {
    constructor(public kind: Kind,
                public namespace: string,
                public name: string,
                public ownerReferences: any) {
    }
}

@Injectable()
export class KubeService {

    constructor(private http: Http) {
    }

    getKind(kind: string): Kind {
        switch (kind) {
            case 'Pod':
                return Kind.Pod;
            case 'Service':
                return Kind.Service;
            case 'Deployment':
                return Kind.Deployment;
            case 'ReplicaSet':
                return Kind.ReplicaSet;
            case 'ReplicationController':
                return Kind.ReplicationController;
            case 'StatefulSet':
                return Kind.StatefulSet;
            case 'DaemonSet':
                return Kind.DaemonSet;
            case 'ServiceAccount':
                return Kind.ServiceAccount;
            case 'ConfigMap':
                return Kind.ConfigMap;
            case 'ClusterRoleBinding':
                return Kind.ClusterRoleBinding;
            case 'Ingress':
                return Kind.Ingress;
            case 'PersistentVolume':
                return Kind.PersistentVolume;
            case 'PersistentVolumeClaim':
                return Kind.PersistentVolumeClaim;
        }
    }

    /**
     * 获取Kubernetes服务url，用于获取列表，查找、以及增删改
     * @param kind
     * @param apiVersion
     * @param namespace
     * @returns {any}
     */
    getDeployUrl(kind: Kind, apiVersion: string, namespace: string): string {
        const prefix = '/kuber';
        switch (kind) {
            case Kind.Service:
                return prefix + '/api/' + (apiVersion || 'v1') + '/namespaces/' + namespace + '/services';
            case Kind.Deployment:
                return prefix + '/apis/' + (apiVersion || 'apps/v1beta1') + '/namespaces/' + namespace + '/deployments';
            case Kind.ReplicaSet:
                return prefix + '/apis/' + (apiVersion || 'extensions/v1beta1') + '/namespaces/' + namespace + '/replicasets';
            case Kind.Pod:
                return prefix + '/api/' + (apiVersion || 'v1') + '/namespaces/' + namespace + '/pods';
            case Kind.ReplicationController:
                return prefix + '/api/' + (apiVersion || 'v1') + '/namespaces/' + namespace + '/replicationcontrollers';
            case Kind.StatefulSet:
                return prefix + '/apis/' + (apiVersion || 'apps/v1beta1') + '/namespaces/' + namespace + '/statefulsets';
            case Kind.DaemonSet:
                return prefix + '/apis/' + (apiVersion || 'extensions/v1beta1') + '/namespaces/' + namespace + '/daemonsets';
            case Kind.ServiceAccount:
                return prefix + '/api/' + (apiVersion || 'v1') + '/namespaces/' + namespace + '/serviceaccounts';
            case Kind.ConfigMap:
                return prefix + '/api/' + (apiVersion || 'v1') + '/namespaces/' + namespace + '/configmaps';
            case Kind.Ingress:
                return prefix + '/apis/' + (apiVersion || 'extensions/v1beta1') + '/namespaces/' + namespace + '/ingresses';
            case Kind.PersistentVolumeClaim:
                return prefix + '/apis/' + (apiVersion || 'v1') + '/namespaces/' + namespace + '/persistentvolumeclaims';
            case Kind.ClusterRoleBinding:
                return prefix + '/apis/' + (apiVersion || 'rbac.authorization.k8s.io/v1beta1') + '/clusterrolebindings';
            case Kind.PersistentVolume:
                return prefix + '/api/' + (apiVersion || 'v1') + '/persistentvolumes';
            default:
                throw new Error('参数不合法.');
        }
    }

    getDeployUrlWithLabelSelector(kind: Kind, apiVersion: string, namespace: string, selector: any): string {
        const items = [];
        if (selector) {
            const props = Object.keys(selector);
            props.forEach(name => items.push(name + (selector[name] ? ('=' + selector[name]) : '')));
        }
        const buffer = (items.length === 0) ? '' : '?labelSelector=' + items.join(',');
        return this.getDeployUrl(kind, apiVersion, namespace) + buffer;
    }

    getRequestOptions(contentType: string): RequestOptions {
        const headers = new Headers({'Content-Type': contentType});
        return new RequestOptions({headers: headers});
    }

    getDefinitions(content: string, namespace: string): any[] {
        const definitions = JSON.parse(content);
        let items = [];
        if (definitions.kind === 'List') {
            items = definitions.items;
        } else {
            items = [definitions];
        }
        items.forEach((item: any) => {
            if (!item.metadata.namespace) {
                item.metadata.namespace = namespace;
            }
        });
        return items;
    }

    /**
     * Kubernetes部署文件发布，格式符合kubectl create -f格式要求
     * @param content
     */
    appDeploymentFromFile(content: string, namespace: string): Observable<any> {
        const items = this.getDefinitions(content, namespace);
        return Observable.create(observer => {
                items.forEach((item: any) => {
                    const status: Status = new Status(this.getKind(item.kind), item.kind, item.metadata.name, item.metadata.namespace);
                    this.http.post(this.getDeployUrl(this.getKind(item.kind), item.apiVersion, item.metadata.namespace), item,
                        this.getRequestOptions('application/json'))
                        .subscribe(r1 => {
                            status.ok();
                            observer.next(status);
                        }, e => {
                            status.error(JSON.parse(e.text()).message);
                            observer.next(status);
                        });
                });
            }
        );
    }

    appUnDeploymentFromFile(content: string, namespace: string): Observable<any> {
        return this.unDeployFromFile(this.getDepends(this.getDefinitions(content, namespace)));
    }

    unDeployFromFile(items: Observable<any>): Observable<any> {
        return Observable.create(observer => {
            items.subscribe((item: any) => {
                const status: Status = new Status(this.getKind(item.kind), item.kind, item.metadata.name, item.metadata.namespace);
                this.http.delete(this.getDeployUrl(this.getKind(item.kind), item.apiVersion, item.metadata.namespace)
                    + '/' + item.metadata.name)
                    .subscribe(r1 => {
                        status.ok();
                        observer.next(status);
                    }, e1 => {
                        status.error(JSON.parse(e1.text()).message);
                        observer.next(status);
                    });
            });
        });
    }

    getReplicaSet(deploymentMetadata: any): Observable<any> {
        return this.http.get(this.getDeployUrl(Kind.ReplicaSet, null, deploymentMetadata.namespace))
            .switchMap(r => r.json().items)
            .filter((item: any) => {
                const ownerReferences: any[] = item.metadata.ownerReferences;
                return ownerReferences.filter((ref: any) => {
                        return (ref.name === deploymentMetadata.name) && (ref.kind === Kind[Kind.Deployment]);
                    }).length > 0;
            }).map((item2: any) => {
                return {
                    kind: Kind[Kind.ReplicaSet],
                    metadata: {
                        name: item2.metadata.name,
                        namespace: item2.metadata.namespace
                    }
                };
            });
    }

    getPodsByReplicaSet(parent: any): Observable<any> {
        return this.http.get(this.getDeployUrl(Kind.Pod, null, parent.metadata.namespace))
            .switchMap(r => r.json().items)
            .filter((item: any) => {
                const ownerReferences: any[] = item.metadata.ownerReferences;
                return ownerReferences.filter((ref: any) => {
                        return (ref.name === parent.metadata.name) && (ref.kind === Kind[Kind.ReplicaSet]);
                    }).length > 0;
            }).map((item2: any) => {
                return {
                    kind: Kind[Kind.Pod],
                    metadata: {
                        name: item2.metadata.name,
                        namespace: item2.metadata.namespace
                    }
                };
            });
    }

    getDepends(items: any[]): Observable<any> {
        return Observable.create(observer => {
            items.forEach((item: any) => {
                observer.next(item);
                if (item.kind === Kind[Kind.Deployment]) {
                    this.getReplicaSet(item.metadata).subscribe(rs => {
                        observer.next(rs);
                        this.getPodsByReplicaSet(rs).subscribe(rs2 => {
                            observer.next(rs2);
                        });
                    });
                }
            });
        });
    }

    /**
     * 转换集合查询结果
     * @param res
     */
    convertItems(res: any): any[] {
        const obj: any = res.json();
        // 设置kind属性，去掉最后的List
        obj.items.forEach(item => {
            item.kindName = obj.kind.substr(0, obj.kind.length - 4);
            item.kind = this.getKind(item.kindName);
        });
        return obj.items;
    }

    /**
     * 获取系统(标签包含system的部署单元)，并按系统名称分组
     * 1、首先查找有system标签的pods，api/v1/namespaces/{namespace}/pods?labelSelector=system
     * 2、查找有system标签的services，api/v1/namespaces/{namespace}/services?labelSelector=system
     * 3、将1、2查找结果拼接，并按metadata.labels.system即系统名分组
     */
    getSystems(namespace: string): Observable<any> {
        const items = Observable.from([Kind.Pod, Kind.Service, Kind.ConfigMap, Kind.ServiceAccount, Kind.ClusterRoleBinding, Kind.Ingress])
            .flatMap(kind => this.http.get(this.getDeployUrlWithLabelSelector(kind, null, namespace, {system: null})))
            .switchMap(res => {
                const obj: any = res.json();
                // 设置kind属性，去掉最后的List
                obj.items.forEach(item => {
                    item.kindName = obj.kind.substr(0, obj.kind.length - 4);
                    item.kind = this.getKind(item.kindName);
                });
                return obj.items;
            });

        return items.groupBy((item: any) => {
            return item.metadata.labels.system;
        });
    }

    /**
     * 删除部署组件
     * @param kind
     * @param namespace
     * @param name
     */
    deleteUnit(unit: K8Unit): Observable<Status> {
        const status: Status = new Status(unit.kind, Kind[unit.kind], unit.name, unit.namespace);
        return this.http.delete(this.getDeployUrl(unit.kind, null, unit.namespace) + '/' + unit.name)
            .map(r1 => {
                status.ok();
                return status;
            });
    }

    /**
     * 删除系统包含的pod和service以及关联单元
     * @param namespace
     * @param system
     */
    deleteSystem(namespace: string, system: string): Observable<any> {
        const pods: Subject<K8Unit> = new Subject();
        const sets: Subject<K8Unit> = new Subject();
        const deployments: Subject<K8Unit> = new Subject();
        const services: Subject<K8Unit> = new Subject();
        const status: Subject<Status> = new Subject();

        this.http.get(this.getDeployUrlWithLabelSelector(Kind.Pod, null, namespace, {system: system}))
            .switchMap(r => r.json().items)
            .map((item: any) => new K8Unit(Kind.Pod, item.metadata.namespace, item.metadata.name, item.metadata.ownerReferences))
            .subscribe((unit: K8Unit) => {
                pods.next(unit);
                unit.ownerReferences.forEach((ref: any) => {
                    const parentUnit = new K8Unit(this.getKind(ref.kind), namespace, ref.name, null);
                    sets.next(parentUnit);
                    this.getParent(parentUnit)
                        .subscribe((parent: K8Unit) => {
                            deployments.next(parent);
                        });
                });
            });
        Observable.from([Kind.Service, Kind.ConfigMap, Kind.ServiceAccount, Kind.ClusterRoleBinding, Kind.Ingress])
            .flatMap(kind => this.http.get(this.getDeployUrlWithLabelSelector(kind, null, namespace, {system: system})))
            .switchMap(res => {
                const obj = res.json();
                // 设置kind属性，去掉最后的List
                obj.items.forEach(item => {
                    item.kindName = obj.kind.substr(0, obj.kind.length - 4);
                    item.kind = this.getKind(item.kindName);
                });
                return obj.items;
            }).map((item: any) => new K8Unit(item.kind, item.metadata.namespace, item.metadata.name, item.metadata.ownerReferences))
            .subscribe(unit => {
                services.next(unit);
            });

        return deployments.merge(sets.delay(1000)).merge(pods.delay(2000)).merge(services)
            .flatMap(unit => this.deleteUnit(unit));
    }

    /**
     * 获取组件的父组件
     * @param unit
     * @returns {Observable<R>}
     */
    getParent(unit: K8Unit): Observable<any> {
        return this.http.get(this.getDeployUrl(unit.kind, null, unit.namespace) + '/' + unit.name)
            .map(res => res.json())
            .filter((item: any) => {
                return item.metadata.ownerReferences && item.metadata.ownerReferences.length > 0;
            })
            .switchMap((item: any) => {
                return item.metadata.ownerReferences;
            })
            .map((ref: any) => {
                return new K8Unit(this.getKind(ref.kind), unit.namespace, ref.name, null);
            });
    }
}
