import {Injectable} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {Http} from '@angular/http';
import 'rxjs/add/operator/map';

/**
 * 模版类
 */
export class Template {
    /**
     * 名称，要求唯一
     */
    name: string;
    /**
     * 详细信息
     */
    description: string;
    /**
     * 路径
     */
    path: string;
}

/**
 * 模版服务，模版存储在/assets/templates目录下。新增模版时，先要将模版文件拷贝到/assets/templates目录下，并在list.json中注册
 */
@Injectable()
export class TemplateService {

    constructor(private http: Http) {
    }

    /**
     * 获取模版清单
     */
    getTemplates(): Observable<Template[]> {
        return this.http.get('/assets/templates/list.json').map(res => res.json());
    }

    /**
     * 获取模版信息
     * @param name
     */
    getTemplateInfo(name: string): Observable<Template> {
        return this.getTemplates().switchMap(items => items.filter(item => item.name === name));
    }

    /**
     * 获取指定模版内容
     * @param path
     */
    getTemplateByPath(path: string): Observable<any> {
        return this.http.get('/assets/templates/' + path).map(res => res.text());
    }
}
