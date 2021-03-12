package org.seedstack.samples.guitar.interfaces.review;

import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.guitar.domain.model.review.Review;
import org.seedstack.samples.guitar.domain.model.review.ReviewRepository;
import org.seedstack.seed.transaction.Transactional;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("reviews")
public class ReviewResource {
    @Inject
    private ReviewRepository reviewRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public List<ReviewRepresentation> all(){
        return reviewRepository.all().map(ReviewRepresentation::new).collect(Collectors.toList());
    }

    @GET
    @Path("/product/{product}/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public ReviewRepresentation single(@PathParam("user") String userId,@PathParam("product") String productId){
        try {
            Optional<Review> revOpt= reviewRepository.single(userId, productId);
            if(!revOpt.isPresent()){
                throw new NotFoundException("Review not found");
            }
            return new ReviewRepresentation(revOpt.get());
        } catch (IllegalArgumentException iae) {
           throw new BadRequestException(iae.getMessage());
        }
    }

    @GET
    @Path("/product/{product}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public List<ReviewRepresentation> searchByProduct(@PathParam("product") String productId){
        try {
            return reviewRepository.productReviews(productId).map(ReviewRepresentation::new).collect(Collectors.toList());
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException(iae.getMessage());
        }
    }

    @GET
    @Path("/user/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public List<ReviewRepresentation> searchByUser(@PathParam("user") String userId){
        try {
            return reviewRepository.userReviews(userId).map(ReviewRepresentation::new).collect(Collectors.toList());
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException(iae.getMessage());
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public ReviewRepresentation create(ReviewRepresentation reviewRep){
        try {
            Optional<Review> revOpt= reviewRepository.create(reviewRep);
            if(!revOpt.isPresent()){
                throw new InternalServerErrorException("Review could not be created");
            }
            return new ReviewRepresentation(revOpt.get());
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException(iae.getMessage());
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public ReviewRepresentation update(ReviewRepresentation reviewrep){
        try {
            Optional<Review> revOpt= reviewRepository.update(reviewrep);
            if(!revOpt.isPresent()){
                throw new InternalServerErrorException("Review could not be updated");
            }
            return new ReviewRepresentation(revOpt.get());
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException(iae.getMessage());
        }
    }

    @DELETE
    @Path("/product/{product}/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public Response delete(@PathParam("user") String userId,@PathParam("product") String productId){
        try {
            reviewRepository.delete(userId, productId);
            return Response.ok().build();
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException(iae.getMessage());
        }
        catch (UnsupportedOperationException uoe){
            return Response.noContent().build();
        }
    }
}
