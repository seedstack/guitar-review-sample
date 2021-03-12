package org.seedstack.samples.angular.domain.services.user;

import com.google.common.base.Strings;
import org.seedstack.business.domain.Repository;
import org.seedstack.business.specification.Specification;
import org.seedstack.jpa.Jpa;
import org.seedstack.samples.angular.domain.model.user.User;
import org.seedstack.samples.angular.interfaces.users.UserRepresentation;
import org.seedstack.seed.Logging;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * UserService implementation
 */
public class UserServiceImpl implements UserService{

    @Logging
    private Logger logger;

    @Inject
    @Jpa
    private Repository<User, String> userRepository;

    @Override
    public Stream<User> all() {
        logger.debug("Retrieving all users");
        Specification<User> allSpec= userRepository.getSpecificationBuilder()
                .of(User.class)
                .all().build();
        return userRepository.get(allSpec);
    }

    @Override
    public Optional<User> single(String email) {
        logger.debug("Fetching user with ID {}", email);
        if(Strings.isNullOrEmpty(email)){
            throw new IllegalArgumentException("Request for single user without ID");
        }
        return userRepository.get(email);
    }

    @Override
    public Optional<User> create(UserRepresentation usrRep) {
        if(usrRep==null || Strings.isNullOrEmpty(usrRep.getEmail())){
            throw new IllegalArgumentException("Can't create a user without email");
        }
        if(single(usrRep.getEmail()).isPresent()){
            throw new IllegalArgumentException("User already exists :"+usrRep.getEmail());
        }
        logger.debug("Creating user {}", usrRep.getEmail());
        User usr = new User(usrRep.getEmail());
        usr.updateIdentity(usrRep.getFirstName(), usrRep.getLastName(), usrRep.getFullName());
        userRepository.add(usr);
        return single(usr.getEmail());
    }

    @Override
    public Optional<User> update(UserRepresentation usrRep) {
        if(usrRep==null || Strings.isNullOrEmpty(usrRep.getEmail())){
            throw new IllegalArgumentException("Can't update a user without email");
        }
        Optional<User> existingUserOpt= single(usrRep.getEmail());
        if(!existingUserOpt.isPresent()){
            throw new IllegalArgumentException("Can't update a non existing user :"+usrRep.getEmail());
        }
        existingUserOpt.get().updateIdentity(usrRep.getFirstName(), usrRep.getLastName(), usrRep.getFullName());
        userRepository.update(existingUserOpt.get());
        return single(usrRep.getEmail());
    }

    @Override
    public void delete(String email) {
        if(Strings.isNullOrEmpty(email)){
            throw new IllegalArgumentException("Can't delete a user without email");
        }
        Optional<User> existingUserOpt= single(email);
        if(!existingUserOpt.isPresent()){
            throw new UnsupportedOperationException("Can't delete a non existing user :"+email);
        }
        userRepository.remove(existingUserOpt.get());
    }
}
