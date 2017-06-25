import {Injectable} from '@angular/core';
import {Http, RequestOptions, Headers} from '@angular/http';
import {Observable} from 'rxjs/Observable';
import 'rxjs/add/observable/from';
import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/groupBy';
import "rxjs/add/operator/mergeMap";
import "rxjs/add/operator/concat";

/**
 * 部署状态类
 */
export class Status {
    public success: boolean;
    public message: string;

    constructor(public kind: string,
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

export enum Kind {
    Pod
}

@Injectable()
export class KubeService {

    constructor(private http: Http) {
    }

    getDeployUrl(kind: string, apiVersion: string, namespace: string): string {
        switch (kind) {
            case 'Service':
                return '/api/' + apiVersion + '/namespaces/' + namespace + '/services';
            case 'Deployment':
                return '/apis/' + apiVersion + '/namespaces/' + namespace + '/deployments';
            case 'ReplicaSet':
                return '/apis/' + apiVersion + '/namespaces/' + namespace + '/replicasets';
            case 'Pod':
                return '/api/' + apiVersion + '/namespaces/' + namespace + '/pods';
            default:
                return '';
        }
    }

    getRequestOptions(contentType: string): RequestOptions {
        const headers = new Headers({'Content-Type': contentType});
        return new RequestOptions({headers: headers});
    }

    getDefinitions(content: string): any[] {
        const definitions = JSON.parse(content);
        let items = [];
        if (definitions.kind === 'List') {
            items = definitions.items;
        } else {
            items = [definitions];
        }
        items.forEach((item: any) => {
            if (!item.metadata.namespace) {
                item.metadata.namespace = 'default';
            }
        });
        return items;
    }

    /**
     * Kubernetes部署文件发布，格式符合kubectl create -f格式要求
     * @param content
     */
    appDeploymentFromFile(content: string): Observable<any> {
        const items = this.getDefinitions(content);
        return Observable.create(observer => {
                items.forEach((item: any) => {
                    const status: Status = new Status(item.kind, item.metadata.name, item.metadata.namespace);
                    this.http.post(this.getDeployUrl(item.kind, item.apiVersion, item.metadata.namespace), item,
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

    appUnDeploymentFromFile(content: string): Observable<any> {
        return this.unDeployFromFile(this.getDepends(this.getDefinitions(content)));
        // const items = this.getDefinitions(content);
        // return Observable.create(observer => {
        //     items.forEach((item: any) => {
        //         const namespace = item.metadata.namespace || 'default';
        //         const status: Status = new Status(item.kind, item.metadata.name, namespace);
        //         this.http.delete(this.getDeployUrl(item.kind, item.apiVersion, namespace) + '/' + item.metadata.name)
        //             .subscribe(r1 => {
        //                 status.ok();
        //                 observer.next(status);
        //             }, e1 => {
        //                 status.error(JSON.parse(e1.text()).message);
        //                 observer.next(status);
        //             });
        //     });
        // });
    }

    unDeployFromFile(items: Observable<any>): Observable<any> {
        return Observable.create(observer => {
            items.subscribe((item: any) => {
                const status: Status = new Status(item.kind, item.metadata.name, item.metadata.namespace);
                this.http.delete(this.getDeployUrl(item.kind, item.apiVersion, item.metadata.namespace) + '/' + item.metadata.name)
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
        return this.http.get('/apis/extensions/v1beta1/namespaces/' + deploymentMetadata.namespace + '/replicasets')
            .switchMap(r => r.json().items)
            .filter((item: any) => {
                const ownerReferences: any[] = item.metadata.ownerReferences;
                return ownerReferences.filter((ref: any) => {
                        return (ref.name === deploymentMetadata.name) && (ref.kind === 'Deployment');
                    }).length > 0;
            }).map((item2: any) => {
                return {
                    kind: 'ReplicaSet',
                    apiVersion: 'extensions/v1beta1',
                    metadata: {
                        name: item2.metadata.name,
                        namespace: item2.metadata.namespace
                    }
                };
            });
    }

    getPodsByReplicaSet(parent: any): Observable<any> {
        return this.http.get('/api/v1/namespaces/' + parent.metadata.namespace + '/pods')
            .switchMap(r => r.json().items)
            .filter((item: any) => {
                const ownerReferences: any[] = item.metadata.ownerReferences;
                return ownerReferences.filter((ref: any) => {
                        return (ref.name === parent.metadata.name) && (ref.kind === 'ReplicaSet');
                    }).length > 0;
            }).map((item2: any) => {
                return {
                    kind: 'Pod',
                    apiVersion: 'v1',
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
                if (item.kind === 'Deployment') {
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
     * 获取系统，并按系统名称分组
     * 1、首先查找有system标签的pods，api/v1/namespaces/{namespace}/pods?labelSelector=system
     * 2、查找有system标签的services，api/v1/namespaces/{namespace}/services?labelSelector=system
     * 3、将1、2查找结果拼接，并按metadata.labels.system即系统名分组
     */
    getSystems(namespace: string): Observable<any> {
        const pods = this.http.get(this.getDeployUrl('Pod', 'v1', namespace) + '?labelSelector=system')
            .map(r => r.json().items)
            .flatMap(items => Observable.from(items));
        const services = this.http.get(this.getDeployUrl('Service', 'v1', namespace) + '?labelSelector=system')
            .map(r => r.json().items)
            .flatMap(items => Observable.from(items));
        return pods.concat(services).groupBy((item: any) => {
            return item.metadata.labels.system;
        });
    }
}
