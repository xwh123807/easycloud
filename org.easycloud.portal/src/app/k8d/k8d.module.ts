import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {K8dRoutingModule} from './k8d-routing.module';
import {K8dComponent} from './k8d.component';
import {TemplateService} from './services/template.service';
import {FormsModule} from "@angular/forms";
import {KubeService} from "./services/kube.service";
import {SideBarComponent} from './side-bar/side-bar.component';

@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        K8dRoutingModule
    ],
    declarations: [
        K8dComponent,
        SideBarComponent
    ],
    providers: [
        TemplateService,
        KubeService
    ]
})

export class K8dModule {
}
