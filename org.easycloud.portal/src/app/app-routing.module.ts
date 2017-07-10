import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {AuthGuard} from './shared';

const routes: Routes = [
    {
        path: 'layout',
        loadChildren: './layout/layout.module#LayoutModule',
        canActivate: [AuthGuard]
    },
    {path: '', loadChildren: './k8d/k8d.module#K8dModule'},
    {path: 'k8s', loadChildren: './k8s/k8s-layout.module#K8sLayoutModule'},
    {path: 'login', loadChildren: './login/login.module#LoginModule'},
    {path: 'signup', loadChildren: './signup/signup.module#SignupModule'},
    {path: 'not-found', loadChildren: './not-found/not-found.module#NotFoundModule'},
    // {path: '**', redirectTo: 'not-found'}
    {path: '**', redirectTo: 'systems'}
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
