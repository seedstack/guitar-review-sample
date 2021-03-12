import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ProductsComponent } from './products/products.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { UserReviewsComponent } from './user-reviews/user-reviews.component';

const routes :Routes=[
  {path:'products', component: ProductsComponent},
  {path:'products/category/:category', component: ProductsComponent},
  {path:"dashboard", component:DashboardComponent},
  {path:"detail/:id", component:ProductDetailComponent},
  {path:'userreviews/:iduser', component:UserReviewsComponent},
  {path:'', redirectTo:'/dashboard', pathMatch:'full'}
];

@NgModule({
  declarations: [],
  imports: [ RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
