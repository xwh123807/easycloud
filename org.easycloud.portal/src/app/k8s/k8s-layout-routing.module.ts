import { K8sLayoutComponent } from './k8s-layout.component';
import { NodeDetailComponent } from './nodes/node-detail/node-detail.component';
import { NodeListComponent } from './nodes/node-list/node-list.component';
import { PodDetailComponent } from './pods/pod-detail/pod-detail.component';
import { PodListComponent } from './pods/pod-list/pod-list.component';
import { ServiceDetailComponent } from './services/service-detail/service-detail.component';
import { ServiceListComponent } from './services/service-list/service-list.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
const k8sLayoutRoutes: Routes = [
  {
    path: '',
    component: K8sLayoutComponent,
    children: [
      { path: 'nodes', component: NodeListComponent },
      { path: 'nodes/:name', component: NodeDetailComponent },
      { path: 'pods', component: PodListComponent },
      { path: 'pod/:namespace/:name', component: PodDetailComponent},
      { path: 'services', component: ServiceListComponent },
      { path: 'service/:namespace/:name', component: ServiceDetailComponent}
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(k8sLayoutRoutes)],
  exports: [RouterModule]
})

export class K8sLayoutRoutingModule { }
