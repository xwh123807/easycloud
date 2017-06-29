import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {TemplateListComponent} from "./template-list/template-list.component";
import {TemplateDetailComponent} from "./template-detail/template-detail.component";
import {TemplatesRouteModule} from "./templates-route.module";
import {FormsModule} from "@angular/forms";
import {K8dCommonsModule} from "../commons/k8d-commons.module";
@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        TemplatesRouteModule,
        K8dCommonsModule
    ],
    declarations: [
        TemplateListComponent,
        TemplateDetailComponent
    ]
})

export class TemplatesModule {
}
