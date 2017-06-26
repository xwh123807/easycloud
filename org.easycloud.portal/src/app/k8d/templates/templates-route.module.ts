import {RouterModule, Routes} from "@angular/router";
import {TemplateListComponent} from "./template-list/template-list.component";
import {NgModule} from "@angular/core";
import {TemplateDetailComponent} from "./template-detail/template-detail.component";
const routes: Routes = [
    {
        path: '', component: TemplateListComponent
    },
    {path: ':name', component: TemplateDetailComponent}
];

@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [
        RouterModule
    ]
})

export class TemplatesRouteModule {
}
