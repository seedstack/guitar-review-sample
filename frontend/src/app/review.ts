//The reviews interface - For exchange with the remote backend
export interface Review {
    user : string;
    userFullName : string;
    product : string;
    comment: string;
    stars: number;
}