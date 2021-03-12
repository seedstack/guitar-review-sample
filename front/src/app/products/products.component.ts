import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Category } from '../category';
import { Product } from '../product';
import { ProductService } from '../services/product/product.service';
import { CategoriesService } from '../services/categories/categories.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit {

  products : Product[];
  newProduct : Product;
  currentCategory : Category;

  constructor(
    private productService :ProductService,
    private categoriesService : CategoriesService,
    private route : ActivatedRoute) { }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      const category = params['category'];
      if(category){
          this.getProductsFromCategory(category);
          this.getCurrentCategory(category);
      }
      else{
        this.getProducts();
      }
    });    
    this.newProduct =this.emptyProduct();    
  }

  getCurrentCategory( cat : string ) :void{
    this.categoriesService.getCategory(cat)
      .subscribe(found => this.currentCategory=found);
  }

  emptyProduct() : Product{
    return {
      id:'',
      title:'',
      description:'',
      categories:[],
      price:0
    }
  }

  saveProduct() :void {
    this.productService.createProduct(this.newProduct)
      .subscribe( prod => this.reInit());
  }

  reInit() : void {
    this.newProduct=this.emptyProduct(); 
    //this.getProducts();
  }

  getProducts() : void {
    const category = this.route.snapshot.paramMap.get('category');
    this.productService.getProducts()
    .subscribe(prods => this.products=prods); 
  }

  getProductsFromCategory(category: String){
    this.productService.searchByCategory(category)
    .subscribe(prods => this.products=prods);
  }
}
