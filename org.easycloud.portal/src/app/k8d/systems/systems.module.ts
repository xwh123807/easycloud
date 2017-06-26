import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SystemsRouteModule} from "./systems-route.module";
import {SystemListComponent} from "./system-list/system-list.component";
import {PipesModule} from "../../commons/pipes/pipes.module";

@NgModule({
    imports: [
        CommonModule,
        PipesModule,
        SystemsRouteModule
    ],
    declarations: [
        SystemListComponent
    ]
})
export class SystemsModule {
}
