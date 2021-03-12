import { Inject, Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable, of} from 'rxjs';
import { catchError, map, tap} from 'rxjs/operators'
import { Category } from '../../category';

@Injectable({
  providedIn: 'root'
})
export class CategoriesService {

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin':'*' })
  };

  constructor(private http : HttpClient, @Inject('BASE_BACKEND_URL') private baseUrl : string) { }

  getCategory(cat : string) : Observable<Category>{
    return this.http.get<Category>(`${this.baseUrl}/categories/${cat}`, this.httpOptions)
    .pipe(
      catchError(this.handleError<Category>(`Get category with tag ${cat}`))
    );
  }

  getCategories() :Observable<Category[]>{
    return this.http.get<Category[]>(`${this.baseUrl}/categories`,this.httpOptions)
      .pipe(
        catchError(this.handleError<Category[]>('Get All categories',[]))
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
