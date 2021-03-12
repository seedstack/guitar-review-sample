import { Review } from '../review';
import { Product } from '../product';
//The user review interface - For exchange with the remote backend
export interface UserReview {
    review : Review;
    product : Product;

}