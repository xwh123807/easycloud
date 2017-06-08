import { AniAlertsComponent } from './ani-alerts/ani-alerts.component';
import { AniButtonComponent } from './ani-button/ani-button.component';
import { AniDropdownComponent } from './ani-dropdown/ani-dropdown.component';
import { AniIconsComponent } from './ani-icons/ani-icons.component';
import { AniOtherComponent } from './ani-other/ani-other.component';
import { AniPaginationComponent } from './ani-pagination/ani-pagination.component';
import { AniPanelsComponent } from './ani-panels/ani-panels.component';
import { AniProgressbarsComponent } from './ani-progressbars/ani-progressbars.component';
import { AniComponent } from './ani.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

const routes: Routes = [
  {
    path: '', component: AniComponent,
    children: [
      { path: 'alerts', component: AniAlertsComponent },
      { path: 'buttons', component: AniButtonComponent },
      { path: 'dropdown', component: AniDropdownComponent },
      { path: 'icons', component: AniIconsComponent },
      { path: 'pagination', component: AniPaginationComponent },
      { path: 'panels', component: AniPanelsComponent },
      { path: 'progressbars', component: AniProgressbarsComponent },
      { path: 'other', component: AniOtherComponent}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AniRoutingModule { }
