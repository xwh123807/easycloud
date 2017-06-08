import { AniRoutingModule } from './an-routing.module';
import { AniButtonComponent } from './ani-button/ani-button.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AniDropdownComponent } from './ani-dropdown/ani-dropdown.component';
import { AniIconsComponent } from './ani-icons/ani-icons.component';
import { AniPanelsComponent } from './ani-panels/ani-panels.component';
import { AniAlertsComponent } from './ani-alerts/ani-alerts.component';
import { AniProgressbarsComponent } from './ani-progressbars/ani-progressbars.component';
import { AniPaginationComponent } from './ani-pagination/ani-pagination.component';
import { AniOtherComponent } from './ani-other/ani-other.component';
import { AniComponent } from './ani.component';


@NgModule({
  imports: [
    CommonModule,
    AniRoutingModule
  ],
  declarations: [AniButtonComponent, AniDropdownComponent, AniIconsComponent,
    AniPanelsComponent, AniAlertsComponent, AniProgressbarsComponent,
    AniPaginationComponent, AniOtherComponent, AniComponent]
})
export class AniModule { }
