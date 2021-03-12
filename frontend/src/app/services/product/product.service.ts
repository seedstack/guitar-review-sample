import { Inject, Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable, of} from 'rxjs';
import { catchError, map, tap} from 'rxjs/operators'
import { Product } from '../../product';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin':'*' })
  };

  constructor(private http : HttpClient, @Inject('BASE_BACKEND_URL') private baseUrl : string) { }

  getProducts() : Observable<Product[]>{    
    return this.http.get<Product[]>(`${this.baseUrl}/products`, this.httpOptions)
      .pipe(
        catchError(this.handleError<Product[]>('getProducts',[]))
      );
  }

  getProduct(id :string) : Observable<Product>{
    const url=`${this.baseUrl}/products/${id}`;
    return this.http.get<Product>(url,this.httpOptions)
      .pipe(
        catchError(this.handleError<Product>(`getProduct with ID ${id}`))
      );
  }

  updateProduct(prod : Product) : Observable<any>{
    return this.http.put(`${this.baseUrl}/products`, prod, this.httpOptions)
      .pipe(
          catchError(this.handleError<any>('Update product'))
      );
  }

  searchByCategory(cat:String) : Observable<Product[]>{
    const url=`${this.baseUrl}/products/category/${cat}`;
    return this.http.get<Product[]>(url, this.httpOptions)
      .pipe(
        catchError(this.handleError<Product[]>(`Search products of category ${cat}`,[]))
      );
  }

  removeCategory(prod : Product, cat : string) : Observable<Product>{
    const url =`${this.baseUrl}/products/${prod.id}/categories/${cat}`;
    return this.http.delete<Product>(url, this.httpOptions)
    .pipe(
      catchError(this.handleError<Product>(`Remove category ${cat}`))
    );
  }

  addCategory(prod : Product, cat : string) : Observable<Product>{
    const url =`${this.baseUrl}/products/${prod.id}/categories/${cat}`;
    return this.http.put<Product>(url,null, this.httpOptions).pipe(
      catchError(this.handleError<Product>(`Adding ${cat} category to product ${prod.id}`))
    );
  }

  createProduct(prod :Product) : Observable<Product>{
    return this.http.post<Product>(`${this.baseUrl}/products`, prod, this.httpOptions)
      .pipe(
        catchError(this.handleError<Product>('Create product'))
      );
  }

  deleteProduct(id :string) : Observable<any>{
    const url=`${this.baseUrl}/products/${id}`;
    return this.http.delete(url,this.httpOptions)
      .pipe(
        catchError(this.handleError<any>(`Delete product id ${id}`))
      );
  }

  uploadProductImage(prod :Product, image : File) : Observable<any>{
    const url=`${this.baseUrl}/products/${prod.id}/image`;
    const httpCurrentOptions = {
      headers: new HttpHeaders({ 'Content-Type': 'application/octet-stream', 'Access-Control-Allow-Origin':'*' })
    };
    return this.http.post(url, image, httpCurrentOptions)
    .pipe(
      catchError(this.handleError(`Uploading Image for product ${prod.id}`))
    );
  }

  /**Records the error to the console */
  handleError<T>(operation : string ='operation', result?:T){
    return (error : any) : Observable<T> => {
      //TODO : send the error to the remote loggin infrastructure
      console.error(error);

      //TODO : Better job for transforming error after consumption
      console.log(`${operation} failed : ${error.message}`);

      //Let the app running by returning an empty result
      return of(result as T);
    };
  }
}
