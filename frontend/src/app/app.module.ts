import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { ProductsComponent } from './products/products.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { AppRoutingModule } from './app-routing.module';
import { DashboardComponent } from './dashboard/dashboard.component';
import { UserReviewsComponent } from './user-reviews/user-reviews.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { StarRatingComponent } from './star-rating/star-rating.component';

@NgModule({
  declarations: [
    AppComponent,
    StarRatingComponent,
    ProductsComponent,
    ProductDetailComponent,
    DashboardComponent,
    UserReviewsComponent,
    
  ],
  imports: [
    BrowserModule, FormsModule, AppRoutingModule, HttpClientModule, NgbModule
  ],
  providers: [
    { provide: 'BASE_BACKEND_URL', useValue:'http://localhost:8080/api' }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
