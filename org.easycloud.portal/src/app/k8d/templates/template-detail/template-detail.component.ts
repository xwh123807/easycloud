import {Component, OnInit} from '@angular/core';
import {Template, TemplateService} from '../../services/template.service';
import {ActivatedRoute, Params, Router} from '@angular/router';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/filter';
import {KubeService} from "../../services/kube.service";
import * as jsyaml from "js-yaml";

@Component({
    selector: 'app-template-detail',
    templateUrl: './template-detail.component.html',
    styleUrls: ['./template-detail.component.scss']
})
export class TemplateDetailComponent implements OnInit {
    /**
     * 当前模版信息
     */
    template: Template;

    /**
     * 模版内容
     */
    content: string;

    /**
     * 模版参数
     */
    params: any[] = [];

    /**
     * 运行日志
     * @type {Array}
     */
    status: any[];

    constructor(private route: ActivatedRoute,
                private router: Router,
                private service: TemplateService,
                private kubeService: KubeService) {
    }

    ngOnInit() {
        this.route.params.switchMap((params: Params) => this.service.getTemplateInfo(params['name']))
            .subscribe(tpl => {
                this.template = tpl;
                this.service.getTemplateByPath(this.template.path)
                    .subscribe(res => {
                        this.content = res;
                        this.params = this.parseTemplate(this.content).params || [];
                    });
            });
    }

    parseTemplate(content: string): any {
        if (this.template.path.endsWith('.json')) {
            return JSON.parse(content);
        } else {
            return jsyaml.load(content);
        }
    }

    getContentWithParams(): string {
        let buffer = this.content;
        this.params.forEach(param => {
            const reg = new RegExp('{' + param.name + '}', 'g');
            buffer = buffer.replace(reg, param.value);
        });
        const obj = this.parseTemplate(buffer);
        return JSON.stringify(obj);
    }

    deploy() {
        this.status = [];
        this.kubeService.appDeploymentFromFile(this.getContentWithParams(), 'default')
            .subscribe(item => {
                this.status.push(item);
            }, error => {
                this.status.push(error);
            });
    }

    delete() {
        this.status = [];
        this.kubeService.appUnDeploymentFromFile(this.getContentWithParams(), 'default')
            .subscribe(r1 => this.status.push(r1), e1 => this.status.push(e1));
    }

    close() {
        this.router.navigate(['/k8d/templates']);
    }
}
