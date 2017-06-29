import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {K8dRoutingModule} from './k8d-routing.module';
import {K8dComponent} from './k8d.component';
import {TemplateService} from './services/template.service';
import {FormsModule} from "@angular/forms";
import {KubeService} from "./services/kube.service";

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        K8dRoutingModule
    ],
    declarations: [
        K8dComponent
    ],
    providers: [
        TemplateService,
        KubeService
    ]
})

export class K8dModule {
}
