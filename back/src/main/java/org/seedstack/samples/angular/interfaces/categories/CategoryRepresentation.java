package org.seedstack.samples.angular.interfaces.categories;

import org.seedstack.samples.angular.domain.model.category.Category;

/**
 * Representation of a category
 */
public class CategoryRepresentation {

    private String title;
    private String tag;

    public CategoryRepresentation(){}

    public CategoryRepresentation(Category category){
        this.title=category.getTitle();
        this.tag=category.getId();
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }
}
