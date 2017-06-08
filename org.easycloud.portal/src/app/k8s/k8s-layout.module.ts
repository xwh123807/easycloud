import { HeaderComponent, SidebarComponent } from '../shared';
import { SharedModule } from '../shared/shared.module';
import { K8sLayoutRoutingModule } from './k8s-layout-routing.module';
import { K8sLayoutComponent } from './k8s-layout.component';
import { NgModule } from '@angular/core';
import { K8sSdiebarComponent } from './k8s-sdiebar/k8s-sdiebar.component';
import { K8sService } from './k8s.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { NodesComponent } from './nodes/nodes.component';
import { NodeListComponent } from './nodes/node-list/node-list.component';
import { KvPipe } from './kv.pipe';
import { PodsComponent } from './pods/pods.component';
import { PodListComponent } from './pods/pod-list/pod-list.component';
import { ServicesComponent } from './services/services.component';
import { ServiceListComponent } from './services/service-list/service-list.component';
import { NodeDetailComponent } from './nodes/node-detail/node-detail.component';
import { PodDetailComponent } from './pods/pod-detail/pod-detail.component';
import { ServiceDetailComponent } from './services/service-detail/service-detail.component';
import { CpuChartComponent } from './charts/cpu-chart/cpu-chart.component';
import { ChartsModule } from 'ng2-charts';
import { DockerListComponent } from './dockerhub/docker-list/docker-list.component';
import { DockerDetailComponent } from './dockerhub/docker-detail/docker-detail.component';
import { DockerHubService } from './dockerhub/docker-hub.service';
import { DeployComponent } from './deploy/deploy/deploy.component';
import { DeployFileComponent } from './deploy/deploy-file/deploy-file.component';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    TranslateModule,
    ChartsModule,
    K8sLayoutRoutingModule,
    SharedModule
  ],
  declarations: [
    K8sLayoutComponent,
    K8sSdiebarComponent,
    NodesComponent,
    NodeListComponent,
    KvPipe,
    PodsComponent,
    PodListComponent,
    ServicesComponent,
    ServiceListComponent,
    NodeDetailComponent,
    PodDetailComponent,
    ServiceDetailComponent,
    CpuChartComponent,
    DockerListComponent,
    DockerDetailComponent,
    DeployComponent,
    DeployFileComponent
  ],
  providers: [
    K8sService,
    DockerHubService
  ]
})

export class K8sLayoutModule { }
