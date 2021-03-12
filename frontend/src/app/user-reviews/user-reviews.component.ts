import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ProductService } from '../services/product/product.service';
import { ReviewsService } from '../services/reviews/reviews.service';
import { UsersService } from '../services/users/users.service';
import { Product } from '../product';
import { Review } from '../review';
import { User } from '../user';
import { UserReview } from './UserReview';

@Component({
  selector: 'app-user-reviews',
  templateUrl: './user-reviews.component.html',
  styleUrls: ['./user-reviews.component.css']
})
export class UserReviewsComponent implements OnInit {

  currentUser : User;
  userReviews : UserReview[];


  constructor(
    private reviewsService : ReviewsService,
    private productService : ProductService,
    private usersService : UsersService,
    private route : ActivatedRoute, 
    private location : Location
  ) { }

  ngOnInit(): void {
    this.userReviews=[];
    this.getUserDetails();
  }

  getUserDetails() : void {
    const id = this.route.snapshot.paramMap.get('iduser');
    this.usersService.getUserDetail(id)
      .subscribe( usr => this.loadDetailsAndReviews(usr));
  }

  loadDetailsAndReviews(usr : User) : void{
    this.currentUser=usr;
    this.reviewsService.getUserReviews(usr.email)
      .subscribe(revs =>this.fillUserReviews(revs) );
  }

  fillUserReviews(revs : Review[]):void{
    revs.forEach(review => this.addUserReview(review) );
  }

  addUserReview (review :Review) : void {
    this.productService.getProduct(review.product)
      .subscribe(prod => this.userReviews.push(
        {
          product: prod,
          review: review
        }
      ));
  }  
}
