package org.seedstack.samples.angular.domain.model.review;

import com.google.common.base.Strings;
import org.seedstack.business.domain.BaseAggregateRoot;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Review extends BaseAggregateRoot<ReviewId> {
    private static final int MAX_STARS=5;
    @EmbeddedId
    private ReviewId id;

    private int stars;
    private String comment;

    public Review(ReviewId id){
        this.id=id;
    }

    /**Required by hibernate*/
    private Review(){}

    public void comment(int stars, String comment){
        if(Strings.isNullOrEmpty(comment)){
            throw new IllegalArgumentException("A review should have a comment");
        }
        if(stars<0 || stars>MAX_STARS){
            throw new IllegalArgumentException("The note can't be more than 5 stars or less than 0");
        }
        this.stars=stars;
        this.comment = comment;
    }

    public int getStars() {
        return stars;
    }

    public String getComment() {
        return comment;
    }
}
