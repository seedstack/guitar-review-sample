package org.seedstack.samples.guitar.domain.model.review;


import org.seedstack.business.domain.Repository;
import org.seedstack.samples.guitar.interfaces.review.ReviewRepresentation;

import java.util.Optional;
import java.util.stream.Stream;

public interface ReviewRepository extends Repository<Review, ReviewId> {
    /**
     * Provides all reviews
     *
     * @return @{@link Stream} of @{@link Review}
     */
    Stream<Review> all();

    /**
     * Provides all user (identified by ID) reviews
     *
     * @param userId The user identifier
     * @return @{@link Stream} of @{@link Review}
     */
    Stream<Review> userReviews(String userId);

    /**
     * Provides all product (identified by ID) reviews
     *
     * @param productId The product identifier
     * @return @{@link Stream} of @{@link Review}
     */
    Stream<Review> productReviews(String productId);

    /**
     * Provides a Single review identified by userId and productId
     *
     * @param userId    the user identifier
     * @param productId the productIdentifier
     * @return @{@link Optional} of @{@link Review} The required review
     */
    Optional<Review> single(String userId, String productId);

    /**
     * Creates and persists a review
     *
     * @param reviewRep The review representation
     * @return @{@link Optional} of @{@link Review} The created review
     */
    Optional<Review> create(ReviewRepresentation reviewRep);

    /**
     * Updates and a review
     *
     * @param reviewRep The review representation
     * @return @{@link Optional} of @{@link Review} The updated review
     */
    Optional<Review> update(ReviewRepresentation reviewRep);

    /**
     * Deletes a review identified by its user and product identifier
     *
     * @param userId    the user identifier
     * @param productId the  product identifier
     */
    void delete(String userId, String productId);
}
