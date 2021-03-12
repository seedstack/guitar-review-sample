package org.seedstack.samples.guitar.interfaces.products;

import org.seedstack.samples.guitar.domain.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepresentation {

    private String title;
    private String description;
    private double price;
    private String id;
    private List<String> categories;

    public ProductRepresentation(Product product){
        this.title=product.getTitle();
        this.description=product.getDescription();
        this.id=product.getId();
        this.price=product.getPrice();
        this.categories=new ArrayList<>();
        product.getCategories().forEach(cat->categories.add(cat.getId()));
    }

    public ProductRepresentation(){}

    public List<String> getCategories() {
        return categories;
    }

    public String getTitle() {
        return title;
    }
    public double getPrice() {
        return price;
    }
    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }
}
