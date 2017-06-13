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

  getCSRFTokenForAppDeploymentFromFile(): Observable<any> {
    return this.http.get(this.getUrl('/csrftoken/appdeploymentfromfile')).map(r => {
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

  readFile(file: any): Observable<any> {
    return Observable.create((observer) => {
      const fr = new FileReader();
      fr.onload = (event) => {
        observer.next(fr.result);
        observer.complete();
      };
      fr.onerror = (error) => {
        observer.error(error);
      };
      fr.readAsText(file);
    });
  }

  appDeploymentFromFile(file: any): Observable<any> {
    let data;
    return this.readFile(file).map(r => {
      return { name: file.name, content: r };
    }).switchMap(r => {
      data = r;
      return this.getCSRFTokenForAppDeploymentFromFile().map(r1 => r1.token);
    }).switchMap(token => {
      const headers = new Headers({ 'Content-Type': 'application/json;charset=UTF-8', 'X-CSRF-TOKEN': token });
      const options = new RequestOptions({ headers: headers });
      return this.http.post('/api/v1/appdeploymentfromfile', data, options).map(r => {
        return r.json();
      });
    }).catch(this.handleError);
  }

  appDeploymentFromFileContent(content: string): Observable<any> {
    const data = {
      name: 'appDeploymentFromFileContent.yaml',
      content: content
    };
    return this.getCSRFTokenForAppDeploymentFromFile().switchMap(r => {
      const headers = new Headers({ 'Content-Type': 'application/json;charset=UTF-8', 'X-CSRF-TOKEN': r.token });
      const options = new RequestOptions({ headers: headers });
      return this.http.post('/api/v1/appdeploymentfromfile', data, options).map(r1 => r1.json());
    }).catch(this.handleError);
  }

  appUnDeploymentFromFileContent(content: string): Observable<any> {
    const data = {
      name: 'appDeploymentFromFileContent.yaml',
      content: content
    };
    return this.getCSRFTokenForAppDeploymentFromFile().switchMap(r => {
      const headers = new Headers({ 'Content-Type': 'application/json;charset=UTF-8', 'X-CSRF-TOKEN': r.token });
      const options = new RequestOptions({ headers: headers });
      return this.http.delete('/api/v1/appdeploymentfromfile', options).map(r1 => r1.json());
    }).catch(this.handleError);
  }

  /**
   * _raw/deployment/namespace/default/name/redis3
   * Method:DELETE
   */
  deleteDeployment() {

  }

  /**
   * _raw/pod/namespace/default/name/redis4-2452501106-0nhgw
   * 
   */
  deletePod() {

  }
}
