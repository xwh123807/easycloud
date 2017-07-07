import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SystemsRouteModule} from "./systems-route.module";
import {SystemListComponent} from "./system-list/system-list.component";
import {PipesModule} from "../../commons/pipes/pipes.module";
import {K8dCommonsModule} from "../commons/k8d-commons.module";
import {SystemCardComponent} from "./system-card/system-card.component";

@NgModule({
    imports: [
        CommonModule,
        PipesModule,
        SystemsRouteModule,
        K8dCommonsModule
    ],
    declarations: [
        SystemListComponent,
        SystemCardComponent
    ]
})
export class SystemsModule {
}
