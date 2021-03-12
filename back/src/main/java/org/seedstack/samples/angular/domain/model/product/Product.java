package org.seedstack.samples.angular.domain.model.product;

import com.google.common.base.Strings;
import org.seedstack.business.domain.BaseAggregateRoot;
import org.seedstack.samples.angular.domain.model.category.Category;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product extends BaseAggregateRoot<String> {

    @Id
    private String id;
    private String title;
    private String description;
    private double price;
    @ManyToMany(targetEntity = Category.class)
    private List<Category> categories;

    public Product(String id){
        this.id=id;
        this.categories= new ArrayList<>();
    }
    /**Hibernate Required*/
    private Product(){}

    @Override
    public String getId() {
        return id;
    }

    public void changeInformations(String title, String description){
        if(Strings.isNullOrEmpty(title)){
            throw new IllegalArgumentException("The product title can't be empty");
        }
        this.title=title;
        this.description=description;
    }

    public void addCategory(Category cat){
        if(categories.stream().noneMatch(categ-> categ.getId().equals(cat.getId()))){
            //The category is not already present
            categories.add(cat);
        }
    }

    public void removeCategory(Category category){
        categories.removeIf(cat -> cat.getId().equals(category.getId()));
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
