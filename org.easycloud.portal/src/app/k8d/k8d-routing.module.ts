import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {K8dComponent} from './k8d.component';
import {TemplateListComponent} from './templates/template-list/template-list.component';
import {TemplateDetailComponent} from './templates/template-detail/template-detail.component';

const routes: Routes = [
    {
        path: '', component: K8dComponent,
        children: [{
            path: 'templates', component: TemplateListComponent
        }, {
            path: 'templates/:name', component: TemplateDetailComponent
        }]
    }
];

@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [
        RouterModule
    ]
})
export class K8dRoutingModule {
}
