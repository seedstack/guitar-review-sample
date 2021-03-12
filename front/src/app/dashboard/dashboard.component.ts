import { Component, Input, OnInit,Inject, Injectable } from '@angular/core';
import { ProductService} from '../services/product/product.service';
import { Product } from '../product';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  products : Product[];
  constructor(
    private productService : ProductService,
    @Inject('BASE_BACKEND_URL') private baseUrl : string) { }

  ngOnInit(): void {
    this.getProducts();
  }

  getProducts() : void{
    this.productService.getProducts()
      .subscribe(prods => this.products=prods.slice(0,5));
  }
}
