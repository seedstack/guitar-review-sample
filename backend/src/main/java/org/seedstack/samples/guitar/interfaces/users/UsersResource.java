package org.seedstack.samples.guitar.interfaces.users;

import org.seedstack.jpa.JpaUnit;
import org.seedstack.samples.guitar.domain.model.user.User;
import org.seedstack.samples.guitar.domain.model.user.UserRepository;
import org.seedstack.seed.transaction.Transactional;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * User resource.
 * Provide user related API endpoints.
 */
@Path("users")
public class UsersResource {

    @Inject
    private UserRepository userRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @JpaUnit("appUnit")
    public List<UserRepresentation> all() {
        return userRepository.all().map(UserRepresentation::new).collect(Collectors.toList());
    }

    @GET
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @JpaUnit("appUnit")
    public UserRepresentation single(@PathParam("email") String email) {
        try {
            Optional<User> usrOpt = userRepository.single(email);
            if (!usrOpt.isPresent()) {
                throw new NotFoundException("User " + email + " not found");
            }
            return new UserRepresentation(usrOpt.get());
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException(iae.getMessage());
        }
    }

    @DELETE
    @Path("/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @JpaUnit("appUnit")
    public Response delete(@PathParam("email") String email) {
        try {
            userRepository.delete(email);
            return Response.ok().build();
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException(iae.getMessage());
        } catch (UnsupportedOperationException uoe) {
            return Response.noContent().build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @JpaUnit("appUnit")
    public UserRepresentation create(UserRepresentation userRep) {
        try {
            Optional<User> usrOpt = userRepository.create(userRep);
            if (!usrOpt.isPresent()) {
                throw new InternalServerErrorException("User " + userRep.getEmail() + " could not be created");
            }
            return new UserRepresentation(usrOpt.get());
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException(iae.getMessage());
        }
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    @JpaUnit("appUnit")
    public UserRepresentation update(UserRepresentation userRep) {
        try {
            Optional<User> usrOpt = userRepository.update(userRep);
            if (!usrOpt.isPresent()) {
                throw new InternalServerErrorException("User " + userRep.getEmail() + " could not be updated");
            }
            return new UserRepresentation(usrOpt.get());
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException(iae.getMessage());
        }
    }
}
