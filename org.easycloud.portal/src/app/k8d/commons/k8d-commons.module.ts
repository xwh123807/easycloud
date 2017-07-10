import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SideBarComponent} from "./side-bar/side-bar.component";
import {StatusComponent} from "./status/status.component";
import {FormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {HeaderComponent} from "./header/header.component";
import {TranslateModule} from "@ngx-translate/core";

@NgModule({
    imports: [
        CommonModule,
        RouterModule,
        FormsModule,
        TranslateModule
    ],
    declarations: [
        SideBarComponent,
        StatusComponent,
        HeaderComponent
    ],
    exports: [
        SideBarComponent,
        StatusComponent,
        HeaderComponent
    ]
})
export class K8dCommonsModule {
}
