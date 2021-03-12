import { Component, OnInit } from '@angular/core';
import { Category } from './category';
import { CategoriesService } from './services/categories/categories.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent  implements OnInit{
  title = '6-Strings reviews';
  categories : Category[];

  constructor(
    private categoriesService : CategoriesService ) { }

  ngOnInit(): void {
    this.getCategories();
  }

  getCategories() :void {
    this.categoriesService.getCategories()
      .subscribe( all=> this.categories=all);
  }

}
