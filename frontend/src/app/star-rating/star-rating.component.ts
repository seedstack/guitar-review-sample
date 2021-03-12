import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-star-rating',
  templateUrl: './star-rating.component.html',
  styleUrls: ['./star-rating.component.css']
})
export class StarRatingComponent implements OnInit {

  @Input() rating :number;
  @Input('mode') mode : string;
  @Output() ratingChange = new EventEmitter<number>();


  constructor() { }

  ngOnInit(): void {
  }

  setRating(value : number) : void {
    console.log(`Click with value : ${value} Mode is ${this.mode}`);
    if(this.mode =="edit"){
      this.rating=value;
      this.ratingChange.emit(this.rating);
    }
  }
}
