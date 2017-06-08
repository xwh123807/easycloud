import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';

@Injectable()
export class DockerHubService {

  constructor(
    private http: Http
  ) { }

  /**
   * return samples:
   * {"repositories":["coreos/flannel","docker-elasticsearch","elasticsearch"]}
   */
  getCatalog(): Observable<any> {
    return this.http.get('/v2/_catalog').map(r => r.json());
  }

  /**
   * return sampes:
   * {"name":"redis","tags":["3.2.6"]}
   */
  getTags(name: string): Observable<any> {
    return this.http.get('/v2/' + name + '/tags/list').map(r => r.json());
  }
}
