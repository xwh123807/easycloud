import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {K8dComponent} from './k8d.component';

const routes: Routes = [
    {path: '', redirectTo: '/k8d/systems', pathMatch: 'full'},
    {
        path: '', component: K8dComponent,
        children: [{
            path: 'templates', loadChildren: './templates/templates.module#TemplatesModule'
        }, {
            path: 'systems', loadChildren: './systems/systems.module#SystemsModule'
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
