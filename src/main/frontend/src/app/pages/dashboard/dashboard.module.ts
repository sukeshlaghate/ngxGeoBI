import { NgModule } from '@angular/core';


import { FrameworkModule } from '../../framework/framework.module';
import { DashboardComponent } from './dashboard.component';


@NgModule({
  imports: [
    FrameworkModule,
  ],
  declarations: [
    DashboardComponent,
  ],
})
export class DashboardModule { }
