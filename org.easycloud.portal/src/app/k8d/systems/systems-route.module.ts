import {RouterModule, Routes} from "@angular/router";
import {SystemListComponent} from "./system-list/system-list.component";
import {NgModule} from "@angular/core";
const routes: Routes = [
    {path: '', component: SystemListComponent}
];

@NgModule({
    imports: [
        RouterModule.forChild(routes)
    ],
    exports: [
        RouterModule
    ]
})

export class SystemsRouteModule {
}
