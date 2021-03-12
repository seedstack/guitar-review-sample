package org.seedstack.samples.guitar.infrastructure.jpa;

import com.google.common.base.Strings;
import org.seedstack.business.specification.Specification;
import org.seedstack.jpa.BaseJpaRepository;
import org.seedstack.samples.guitar.domain.model.user.User;
import org.seedstack.samples.guitar.domain.model.user.UserRepository;
import org.seedstack.samples.guitar.interfaces.users.UserRepresentation;
import org.seedstack.seed.Logging;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * UserService implementation
 */
public class UserJpaRepository extends BaseJpaRepository<User, String> implements UserRepository {
    @Logging
    private Logger logger;

    @Override
    public Stream<User> all() {
        logger.debug("Retrieving all users");
        Specification<User> allSpec = getSpecificationBuilder()
                .of(User.class)
                .all().build();
        return get(allSpec);
    }

    @Override
    public Optional<User> single(String email) {
        logger.debug("Fetching user with ID {}", email);
        if (Strings.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("Request for single user without ID");
        }
        return get(email);
    }

    @Override
    public Optional<User> create(UserRepresentation usrRep) {
        if (usrRep == null || Strings.isNullOrEmpty(usrRep.getEmail())) {
            throw new IllegalArgumentException("Can't create a user without email");
        }
        if (single(usrRep.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists :" + usrRep.getEmail());
        }
        logger.debug("Creating user {}", usrRep.getEmail());
        User usr = new User(usrRep.getEmail());
        usr.updateIdentity(usrRep.getFirstName(), usrRep.getLastName(), usrRep.getFullName());
        add(usr);
        return single(usr.getEmail());
    }

    @Override
    public Optional<User> update(UserRepresentation usrRep) {
        if (usrRep == null || Strings.isNullOrEmpty(usrRep.getEmail())) {
            throw new IllegalArgumentException("Can't update a user without email");
        }
        Optional<User> existingUserOpt = single(usrRep.getEmail());
        if (!existingUserOpt.isPresent()) {
            throw new IllegalArgumentException("Can't update a non existing user :" + usrRep.getEmail());
        }
        existingUserOpt.get().updateIdentity(usrRep.getFirstName(), usrRep.getLastName(), usrRep.getFullName());
        update(existingUserOpt.get());
        return single(usrRep.getEmail());
    }

    @Override
    public void delete(String email) {
        if (Strings.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("Can't delete a user without email");
        }
        Optional<User> existingUserOpt = single(email);
        if (!existingUserOpt.isPresent()) {
            throw new UnsupportedOperationException("Can't delete a non existing user :" + email);
        }
        remove(existingUserOpt.get());
    }
}
