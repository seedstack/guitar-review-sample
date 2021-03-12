package org.seedstack.samples.angular.domain.services.review;

import com.google.common.base.Strings;
import org.seedstack.business.domain.AggregateExistsException;
import org.seedstack.business.domain.Repository;
import org.seedstack.business.specification.Specification;
import org.seedstack.business.specification.dsl.SpecificationBuilder;
import org.seedstack.jpa.Jpa;
import org.seedstack.samples.angular.domain.model.product.Product;
import org.seedstack.samples.angular.domain.model.review.Review;
import org.seedstack.samples.angular.domain.model.review.ReviewId;
import org.seedstack.samples.angular.domain.model.user.User;
import org.seedstack.samples.angular.domain.services.product.ProductService;
import org.seedstack.samples.angular.domain.services.user.UserService;
import org.seedstack.samples.angular.interfaces.review.ReviewRepresentation;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The Review service implementation
 */
public class ReviewServiceImpl implements ReviewService{

    @Inject
    @Jpa
    private Repository<Review, ReviewId> reviewRepository;

    @Inject
    private SpecificationBuilder specificationBuilder;
    @Inject
    private UserService userService;
    @Inject
    private ProductService productService;

    @Override
    public Stream<Review> all() {
        Specification<Review> allSpec= specificationBuilder
                .of(Review.class)
                .all().build();
        return reviewRepository.get(allSpec);
    }

    @Override
    public Stream<Review> userReviews(String userId) {
        Specification<Review> userReviewSpec= specificationBuilder
                .of(Review.class)
                .property("id.user.email")
                .equalTo(userId)
                .build();
        return reviewRepository.get(userReviewSpec);
    }

    @Override
    public Stream<Review> productReviews(String productId) {
        Specification<Review> productSpecification = specificationBuilder
                .of(Review.class)
                .property("id.product.id")
                .equalTo(productId)
                .build();
        return reviewRepository.get(productSpecification);
    }

    @Override
    public Optional<Review> single(String userId, String productId) {
        Optional<User> usrOpt= userService.single(userId);
        Optional<Product> prodOpt= productService.single(productId);
        if(!usrOpt.isPresent()){
            throw new IllegalArgumentException("Can't get review from a non existing user");
        }
        if(!prodOpt.isPresent()){
            throw new IllegalArgumentException("Can't get a review for a non existing product");
        }
        ReviewId identifier = new ReviewId(usrOpt.get(), prodOpt.get());
        return reviewRepository.get(identifier);
    }

    @Override
    public Optional<Review> create(ReviewRepresentation reviewRep) {
        ReviewId identifier= extractId(reviewRep);
        Review review = new Review(identifier);
        review.comment(reviewRep.getStars(), reviewRep.getComment());
        try {
            reviewRepository.add(review);
            return reviewRepository.get(identifier);
        }
        catch (AggregateExistsException ae){
            throw new IllegalArgumentException("Review already exists for this user & product");
        }
    }

    @Override
    public Optional<Review> update(ReviewRepresentation reviewRep) {
        ReviewId identifier= extractId(reviewRep);
        Optional<Review> reviewOpt = reviewRepository.get(identifier);
        if(!reviewOpt.isPresent()){
            throw new IllegalArgumentException("The review to update could not be found");
        }
        reviewOpt.get().comment(reviewRep.getStars(), reviewRep.getComment());
        reviewRepository.update(reviewOpt.get());
        return reviewRepository.get(identifier);
    }

    @Override
    public void delete(String userId, String productId) {
        Optional<Review> existingReview= single(userId, productId);
        if(!existingReview.isPresent()){
            throw new UnsupportedOperationException("Can't delete a non existing review");
        }
        reviewRepository.remove(existingReview.get());
    }

    private ReviewId extractId(ReviewRepresentation reviewRep){
        if(reviewRep==null || Strings.isNullOrEmpty(reviewRep.getUser())){
            throw new IllegalArgumentException("Review's user can't be null or empty");
        }
        if(Strings.isNullOrEmpty(reviewRep.getProduct())){
            throw new IllegalArgumentException("Review's product id can't be null or empty");
        }
        Optional<User> usrOpt= userService.single(reviewRep.getUser());
        Optional<Product> prodOpt= productService.single(reviewRep.getProduct());
        if(!usrOpt.isPresent()){
            throw new IllegalArgumentException("Invalid user ID");
        }
        if(!prodOpt.isPresent()){
            throw new IllegalArgumentException("Invalid product ID");
        }
        return new ReviewId(usrOpt.get(), prodOpt.get());
    }


}
