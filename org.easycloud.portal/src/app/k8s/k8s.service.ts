import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable()
export class K8sService {
  private serviceUrl = '/api/v1';
  //'http://kubermaster.ygsoft.com:8080/api/v1';
  private isDebug = false;

  constructor(
    private http: Http
  ) { }

  getUrl(path: string): string {
    return this.isDebug ? '/assets/mock' + path + '.json' : this.serviceUrl + path;
  }

  getNodes(): Observable<any> {
    return this.http.get(this.getUrl('/node')).map(r => {
      const data = r.json();
      return data;
    });
  }

  getPods(): Observable<any> {
    return this.http.get(this.getUrl('/pod')).map(r => {
      const data = r.json();
      return data;
    });
  }

  getServices(): Observable<any> {
    return this.http.get(this.getUrl('/service')).map(r => {
      const data = r.json();
      return data;
    });
  }

  getNode(name: string): Observable<any> {
    return this.http.get(this.getUrl('/node/' + name)).map(r => {
      const data = r.json();
      return data;
    });
  }

  getService(namespace: string, name: string): Observable<any> {
    return this.http.get(this.getUrl('/service/' + namespace + '/' + name)).map(r => {
      const data = r.json();
      return data;
    });
  }

  getPod(namespace: string, name: string): Observable<any> {
    return this.http.get(this.getUrl('/pod/' + namespace + '/' + name)).map(r => {
      const data = r.json();
      return data;
    });
  }

  /**
   * {
  "name": "redis",
  "containerImage": "dockerhub.ygsoft.com:5000/redis:3.2.6",
  "imagePullSecret": null,
  "containerCommand": null,
  "containerCommandArgs": null,
  "replicas": 1,
  "portMappings": [],
  "variables": [],
  "isExternal": false,
  "description": null,
  "namespace": "kube-system",
  "memoryRequirement": null,
  "cpuRequirement": null,
  "labels": [
   {
    "key": "app",
    "value": "redis"
   },
   {
    "key": "version",
    "value": "3.2.6"
   }
  ],
  "runAsPrivileged": false
 }
   */
  appDeployment(): void {
    //this.http.post('/api/v1/appdeployment');
  }
    
  appDeploymentFromFile(): void{
    //this.http.post('/api/v1/appdeploymentfromfile')
  }
}
