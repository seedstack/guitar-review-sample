package org.seedstack.samples.angular.interfaces.users;

import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.angular.domain.model.user.User;
import org.seedstack.samples.angular.domain.services.user.UserService;
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

@Path("users")
public class UsersResource {

    @Inject
    private UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public List<UserRepresentation> all(){
        return userService.all().map(UserRepresentation::new).collect(Collectors.toList());
    }

    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public UserRepresentation single(@PathParam("email") String email){
        try {
            Optional<User> usrOpt = userService.single(email);
            if (!usrOpt.isPresent()) {
                throw new NotFoundException("User " + email + " not found");
            }
            return new UserRepresentation(usrOpt.get());
        }
        catch (IllegalArgumentException iae){
            throw new BadRequestException(iae.getMessage());
        }
    }

    @DELETE
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public Response delete(@PathParam("email") String email){
        try {
            userService.delete(email);
            return Response.ok().build();
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException(iae.getMessage());
        }
        catch (UnsupportedOperationException uoe){
            return Response.noContent().build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public UserRepresentation create(UserRepresentation userRep){
        try {
            Optional<User> usrOpt=userService.create(userRep);
            if (!usrOpt.isPresent()) {
                throw new InternalServerErrorException("User " + userRep.getEmail() + " could not be created");
            }
            return new UserRepresentation(usrOpt.get());
        } catch (IllegalArgumentException iae) {
            throw  new BadRequestException(iae.getMessage());
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional @JpaUnit("appUnit")
    public UserRepresentation update(UserRepresentation userRep){
        try {
            Optional<User> usrOpt=userService.update(userRep);
            if (!usrOpt.isPresent()) {
                throw new InternalServerErrorException("User " + userRep.getEmail() + " could not be updated");
            }
            return new UserRepresentation(usrOpt.get());
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException(iae.getMessage());
        }
    }
}
