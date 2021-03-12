import { Injectable, Inject } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { Observable, of} from 'rxjs';
import { catchError, map, tap} from 'rxjs/operators'
import { User } from '../../user';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json', 'Access-Control-Allow-Origin':'*' })
  };

  constructor(
    private http : HttpClient, 
    @Inject('BASE_BACKEND_URL') private baseUrl : string
  ) { }

  getUserDetail(userId : string) : Observable<User>{
    const url =`${this.baseUrl}/users/${userId}`;
    return this.http.get<User>(url, this.httpOptions)
      .pipe(
        catchError(this.handleError<User>(`User detail id : ${userId}`))
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
