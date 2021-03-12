import { Inject, Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable, of} from 'rxjs';
import { catchError, map, tap} from 'rxjs/operators'
import { Review } from '../../review';

@Injectable({
  providedIn: 'root'
})
export class ReviewsService {

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin':'*' })
  };

  constructor(
    private http : HttpClient, 
    @Inject('BASE_BACKEND_URL') private baseUrl : string) { }

  getProductReviews(productId :string) : Observable<Review[]>{
    const url=`${this.baseUrl}/reviews/product/${productId}`;
    return this.http.get<Review[]>(url, this.httpOptions)
      .pipe(
        catchError(this.handleError<Review[]>(`Get reviews for product ${productId}`,[]))
      );
  }

  getUserReviews(userId:string) : Observable<Review[]>{
    const url=`${this.baseUrl}/reviews/user/${userId}`;
    console.log(`Getting user reviews with id ${userId}`);
    return this.http.get<Review[]>(url, this.httpOptions)
    .pipe(
      catchError(this.handleError<Review[]>(`Get reviews for user ${userId}`,[]))
    );
  }

  addReview(rev : Review) : Observable<any>{
      return this.http.post(`${this.baseUrl}/reviews`,rev,this.httpOptions)
        .pipe(
          catchError(this.handleError<any>('Add Review'))
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
