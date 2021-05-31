package org.seedstack.samples.guitar.interfaces.review;

import org.seedstack.samples.guitar.domain.model.review.Review;

/**
 * The API representation for a review
 */
public class ReviewRepresentation {

    private String user;
    private String userFullName;
    private String product;
    private int stars;
    private String comment;

    public ReviewRepresentation(Review review){
        this.comment=review.getComment();
        this.stars= review.getStars();
        this.user=review.getId().getUser().getEmail();
        this.userFullName= review.getId().getUser().getFullName();
        this.product= review.getId().getProduct().getId();
    }

    private ReviewRepresentation(){}

    public String getComment() { return comment; }
    public int getStars() { return stars; }
    public String getProduct() { return product; }
    public String getUser() { return user; }
    public String getUserFullName() { return userFullName; }
}
