package org.seedstack.samples.angular.interfaces.review;

import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.angular.domain.model.review.Review;
import org.seedstack.samples.angular.domain.services.review.ReviewService;
import org.seedstack.seed.transaction.Transactional;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("reviews")
public class ReviewResource {

    @Inject
    private ReviewService reviewService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public List<ReviewRepresentation> all(){
        return reviewService.all().map(ReviewRepresentation::new).collect(Collectors.toList());
    }

    @GET
    @Path("/product/{product}/{user}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public ReviewRepresentation single(@PathParam("user") String userId,@PathParam("product") String productId){
        try {
            Optional<Review> revOpt=reviewService.single(userId, productId);
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
            return reviewService.productReviews(productId).map(ReviewRepresentation::new).collect(Collectors.toList());
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
            return reviewService.userReviews(userId).map(ReviewRepresentation::new).collect(Collectors.toList());
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
            Optional<Review> revOpt=reviewService.create(reviewRep);
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
            Optional<Review> revOpt=reviewService.update(reviewrep);
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
            reviewService.delete(userId, productId);
            return Response.ok().build();
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException(iae.getMessage());
        }
        catch (UnsupportedOperationException uoe){
            return Response.noContent().build();
        }
    }
}
