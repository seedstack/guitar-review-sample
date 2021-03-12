package org.seedstack.samples.angular.domain.model.review;

import org.seedstack.business.domain.BaseValueObject;
import org.seedstack.samples.angular.domain.model.product.Product;
import org.seedstack.samples.angular.domain.model.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@Embeddable
public class ReviewId extends BaseValueObject {

    @OneToOne(cascade = CascadeType.DETACH)
    private User user;

    @OneToOne(cascade = CascadeType.DETACH)
    private Product product;

    public ReviewId(User user, Product product){
        this.user=user;
        this.product=product;
    }

    /**Required by hibernate*/
    private ReviewId(){}

    public Product getProduct() {
        return product;
    }

    public User getUser() {
        return user;
    }
}
