import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {TemplateListComponent} from "./template-list/template-list.component";
import {TemplateDetailComponent} from "./template-detail/template-detail.component";
import {TemplatesRouteModule} from "./templates-route.module";
import {FormsModule} from "@angular/forms";
@NgModule({
    imports: [
        CommonModule,
        FormsModule,
        TemplatesRouteModule
    ],
    declarations: [
        TemplateListComponent,
        TemplateDetailComponent
    ]
})

export class TemplatesModule {
}
