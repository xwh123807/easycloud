import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbDropdownModule } from '@ng-bootstrap/ng-bootstrap';
import { TranslateModule } from '@ngx-translate/core';

import { LayoutRoutingModule } from './layout-routing.module';
import { LayoutComponent } from './layout.component';
import { SharedModule } from '../shared/shared.module';

@NgModule({
  imports: [
    CommonModule,
    NgbDropdownModule.forRoot(),
    TranslateModule,
    LayoutRoutingModule,
    SharedModule
  ],
  declarations: [
    LayoutComponent,
  ]
})
export class LayoutModule { }
