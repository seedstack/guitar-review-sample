package org.seedstack.samples.angular.domain.services.user;

import org.seedstack.business.Service;
import org.seedstack.samples.angular.domain.model.user.User;
import org.seedstack.samples.angular.interfaces.users.UserRepresentation;

import java.util.Optional;
import java.util.stream.Stream;

@Service
public interface UserService {

    /**
     * Provides all registered users
     * @return @{@link Stream} of @{@link User}
     */
    Stream<User> all();

    /**
     * Provides a user by it od
     * @param email the user email (ID)
     * @return @{@link Optional} of @{@link User}
     */
    Optional<User> single(String email);

    /**
     * Adds a User
     * @param usrRep the user details to set
     * @return @{@link Optional} of @{@link User} the new user
     */
    Optional<User> create(UserRepresentation usrRep);

    /**
     * Updates a user.
     * Email can not be updated.
     * @param usrRep the updated userData
     * @return @{@link Optional} of @{@link User} the updated user
     */
    Optional<User> update(UserRepresentation usrRep);

    /**
     * Deletes a user identified by its ID
     * @param email
     */
    void delete(String email);
}
