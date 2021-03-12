package org.seedstack.samples.guitar.infrastructure.jpa;

import com.google.common.base.Strings;
import org.seedstack.business.domain.AggregateExistsException;
import org.seedstack.business.specification.Specification;
import org.seedstack.business.specification.dsl.SpecificationBuilder;
import org.seedstack.jpa.BaseJpaRepository;
import org.seedstack.samples.guitar.domain.model.product.Product;
import org.seedstack.samples.guitar.domain.model.product.ProductRepository;
import org.seedstack.samples.guitar.domain.model.review.Review;
import org.seedstack.samples.guitar.domain.model.review.ReviewId;
import org.seedstack.samples.guitar.domain.model.review.ReviewRepository;
import org.seedstack.samples.guitar.domain.model.user.User;
import org.seedstack.samples.guitar.domain.model.user.UserRepository;
import org.seedstack.samples.guitar.interfaces.review.ReviewRepresentation;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The Review service implementation
 */
public class ReviewJpaRepository extends BaseJpaRepository<Review, ReviewId> implements ReviewRepository {
    @Inject
    private SpecificationBuilder specificationBuilder;
    @Inject
    private UserRepository userRepository;
    @Inject
    private ProductRepository productRepository;

    @Override
    public Stream<Review> all() {
        Specification<Review> allSpec= specificationBuilder
                .of(Review.class)
                .all().build();
        return get(allSpec);
    }

    @Override
    public Stream<Review> userReviews(String userId) {
        Specification<Review> userReviewSpec= specificationBuilder
                .of(Review.class)
                .property("id.user.email")
                .equalTo(userId)
                .build();
        return get(userReviewSpec);
    }

    @Override
    public Stream<Review> productReviews(String productId) {
        Specification<Review> productSpecification = specificationBuilder
                .of(Review.class)
                .property("id.product.id")
                .equalTo(productId)
                .build();
        return get(productSpecification);
    }

    @Override
    public Optional<Review> single(String userId, String productId) {
        Optional<User> usrOpt= userRepository.single(userId);
        Optional<Product> prodOpt= productRepository.single(productId);
        if(!usrOpt.isPresent()){
            throw new IllegalArgumentException("Can't get review from a non existing user");
        }
        if(!prodOpt.isPresent()){
            throw new IllegalArgumentException("Can't get a review for a non existing product");
        }
        ReviewId identifier = new ReviewId(usrOpt.get(), prodOpt.get());
        return get(identifier);
    }

    @Override
    public Optional<Review> create(ReviewRepresentation reviewRep) {
        ReviewId identifier= extractId(reviewRep);
        Review review = new Review(identifier);
        review.comment(reviewRep.getStars(), reviewRep.getComment());
        try {
            add(review);
            return get(identifier);
        }
        catch (AggregateExistsException ae){
            throw new IllegalArgumentException("Review already exists for this user & product");
        }
    }

    @Override
    public Optional<Review> update(ReviewRepresentation reviewRep) {
        ReviewId identifier= extractId(reviewRep);
        Optional<Review> reviewOpt = get(identifier);
        if(!reviewOpt.isPresent()){
            throw new IllegalArgumentException("The review to update could not be found");
        }
        reviewOpt.get().comment(reviewRep.getStars(), reviewRep.getComment());
        update(reviewOpt.get());
        return get(identifier);
    }

    @Override
    public void delete(String userId, String productId) {
        Optional<Review> existingReview= single(userId, productId);
        if(!existingReview.isPresent()){
            throw new UnsupportedOperationException("Can't delete a non existing review");
        }
        remove(existingReview.get());
    }

    private ReviewId extractId(ReviewRepresentation reviewRep){
        if(reviewRep==null || Strings.isNullOrEmpty(reviewRep.getUser())){
            throw new IllegalArgumentException("Review's user can't be null or empty");
        }
        if(Strings.isNullOrEmpty(reviewRep.getProduct())){
            throw new IllegalArgumentException("Review's product id can't be null or empty");
        }
        Optional<User> usrOpt= userRepository.single(reviewRep.getUser());
        Optional<Product> prodOpt= productRepository.single(reviewRep.getProduct());
        if(!usrOpt.isPresent()){
            throw new IllegalArgumentException("Invalid user ID");
        }
        if(!prodOpt.isPresent()){
            throw new IllegalArgumentException("Invalid product ID");
        }
        return new ReviewId(usrOpt.get(), prodOpt.get());
    }


}
