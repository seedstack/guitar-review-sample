package org.seedstack.samples.guitar.domain.model.category;

import com.google.common.base.Strings;
import org.seedstack.business.domain.BaseAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Model for category
 */
@Entity
public class Category extends BaseAggregateRoot<String> {

    private String title;
    @Id
    private String tag;

    public Category(String tag){
        this.tag=tag;
    }

    /**Hibernate required*/
    private Category(){}

    public void updateTitle(String title){
        if(Strings.isNullOrEmpty(title)){
            throw new IllegalArgumentException("Category title cannot be null or empty");
        }
        this.title=title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getId() {
        return tag;
    }
}
