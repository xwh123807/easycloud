import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SideBarComponent} from "./side-bar/side-bar.component";
import {StatusComponent} from "./status/status.component";
import {FormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        FormsModule
    ],
    declarations: [
        SideBarComponent,
        StatusComponent
    ],
    exports: [
        SideBarComponent,
        StatusComponent
    ]
})
export class K8dCommonsModule {
}
