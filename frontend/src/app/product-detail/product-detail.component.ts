import { Component, Input, OnInit,Inject, Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import { ProductService } from '../services/product/product.service';
import { ReviewsService } from '../services/reviews/reviews.service';
import { CategoriesService } from '../services/categories/categories.service';
import { Product } from '../product';
import { Review } from '../review';
import { Category} from '../category';


@Component({
  selector: 'app-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.css']
})
export class ProductDetailComponent implements OnInit {

  product : Product;
  reviews : Review[];
  newReview : Review;
  missingCategories : Category[];
  selectedCategory : string;

  constructor(
    private productService : ProductService, 
    private reviewsService : ReviewsService,
    private categoriesService : CategoriesService,
    private route : ActivatedRoute, 
    private location : Location,
    @Inject('BASE_BACKEND_URL') private baseUrl : string) { }

  ngOnInit(): void {
    this.reInitPage();
  }

  reInitPage() : void {
    this.getProduct();
    this.initNewReview();
    this.getProductReviews();
  }

  initNewReview() : void {
    this.newReview ={
      "user" : 'truc@machin.com',
      "userFullName":'',
      "product" : '',
      "comment": '',
      "stars": 0
    };
  }

  fillCategories() : void{
    this.categoriesService.getCategories()
      .subscribe(allCats => {
        this.missingCategories = [];
        for(const cat of allCats){
          if(this.product.categories.indexOf(cat.tag) == -1){
            this.missingCategories.push(cat);
          }
        }          
      });
  }

  addCategoryProduct() : void {
    console.log(`Adding ${this.selectedCategory} to ${this.product.id}`);
    this.productService.addCategory(this.product, this.selectedCategory)
      .subscribe(updatedProd => {
        this.product=updatedProd;
        this.selectedCategory=null;
        this.fillCategories();
      });
  }

  saveReview( productId:string) :void {
    this.newReview.product=productId;
    this.reviewsService.addReview(this.newReview)
      .subscribe( () => this.reInitPage());
  }

  getProduct() : void {
    const id = this.route.snapshot.paramMap.get('id');
    this.productService.getProduct(id)
      .subscribe(prod => {
        this.product=prod;
        this.fillCategories(); //Once the product is loaded, we can fill the missing categories
      });
  }

  getProductReviews() :void {
    const id = this.route.snapshot.paramMap.get('id');
    this.reviewsService.getProductReviews(id)
      .subscribe( reviews => this.reviews=reviews);
  }

  saveProduct() :void{
    this.productService.updateProduct(this.product)
      .subscribe( ()=> this.goBack());
  }

  deleteProduct() : void{
    this.productService.deleteProduct(this.product.id)
      .subscribe(() => this.goBack());
  }

  removeCat(cat : string) : void{
    this.productService.removeCategory(this.product, cat)
    .subscribe( updatedProduct => {
      this.product=updatedProduct;
      this.fillCategories();
    });
  }
  
  handleFileInput(files: FileList) : void {
    console.log("Uploading image");
    this.productService.uploadProductImage(this.product,files.item(0))
    .subscribe( _=> window.location.reload());
  }

  goBack():void {
    this.location.back();
  }
}
