//The product interface - For exchange with the remote backend
export interface Product {
    id : string;
    title : string;
    description: string;
    price: number;
    categories : string[];
}