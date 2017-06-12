import { NotificationService } from './notification.service';
import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';
import 'rxjs/add/observable/throw';

@Injectable()
export class K8sService {
  private serviceUrl = '/api/v1';
  // 'http://kubermaster.ygsoft.com:8080/api/v1';
  private isDebug = false;

  constructor(
    private http: Http,
    private notification: NotificationService
  ) { }

  getUrl(path: string): string {
    return this.isDebug ? '/assets/mock' + path + '.json' : this.serviceUrl + path;
  }

  private handleError(error: Response | any): Observable<any> {
    let errMsg: string;
    if (error instanceof Response) {
      errMsg = error.toString() + '\nDetail: ' + error.text();
    } else {
      errMsg = error.message ? error.message : error.toString();
    }
    console.error(errMsg);
    return Observable.throw(errMsg);
  }

  getNamespaces(): Observable<any> {
    return this.http.get(this.getUrl('/namespace')).map(r => {
      const data = r.json();
      return data;
    }).catch(this.handleError);
  }

  getNodes(): Observable<any> {
    return this.http.get(this.getUrl('/node')).map(r => {
      const data = r.json();
      return data;
    }).catch(this.handleError);
  }

  getPods(): Observable<any> {
    return this.http.get(this.getUrl('/pod')).map(r => {
      const data = r.json();
      return data;
    }).catch(this.handleError);
  }

  getServices(): Observable<any> {
    return this.http.get(this.getUrl('/service')).map(r => {
      const data = r.json();
      return data;
    }).catch(this.handleError);
  }

  getNode(name: string): Observable<any> {
    return this.http.get(this.getUrl('/node/' + name)).map(r => {
      const data = r.json();
      return data;
    }).catch(this.handleError);
  }

  getService(namespace: string, name: string): Observable<any> {
    return this.http.get(this.getUrl('/service/' + namespace + '/' + name)).map(r => {
      const data = r.json();
      return data;
    }).catch(this.handleError);
  }

  getPod(namespace: string, name: string): Observable<any> {
    return this.http.get(this.getUrl('/pod/' + namespace + '/' + name)).map(r => {
      const data = r.json();
      return data;
    }).catch(this.handleError);
  }

  /**
   * {
  "token": "5iyCYeEpXwhSPbaVaxWnScifvE8:1497236093333"
 }
   */
  getCSRFTokenForAppDeployment(): Observable<any> {
    return this.http.get(this.getUrl('/csrftoken/appdeployment')).map(r => {
      const data = r.json();
      return data;
    }).catch(this.handleError);
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
  appDeployment(deployData: any): Observable<any> {
    return this.getCSRFTokenForAppDeployment().switchMap(r => {
      const headers = new Headers({ 'X-CSRF-TOKEN': r.token });
      const options = new RequestOptions({ headers: headers });
      return this.http.post('/api/v1/appdeployment', deployData, options).map(r1 => r1.json());
    }).catch(this.handleError);
  }

  appDeploymentFromFile(file: any): Observable<any> {
    //this.http.post('/api/v1/appdeploymentfromfile')
    var fr = new FileReader();
    fr.onerror = (e) => this.handleError;
    fr.readAsText(file);
    fr.onload = (event) => {
      var data = {
        name: file.name,
        content: fr.result
      }
      const headers = new Headers({ 'Content-Type': 'application/json;charset=UTF-8' });
      const options = new RequestOptions({ headers: headers });
      return this.http.post('/api/v1/appdeploymentfromfile', data, options).map(r => {
        console.info(r);
        return r.json();
      });
    };
  }
}
