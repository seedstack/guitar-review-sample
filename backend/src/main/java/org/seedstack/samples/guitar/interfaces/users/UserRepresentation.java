package org.seedstack.samples.guitar.interfaces.users;

import org.seedstack.samples.guitar.domain.model.user.User;

/**
 * A user's representation
 */
public class UserRepresentation {

    private String email;
    private String firstName;
    private String lastName;
    private String fullName;

    public UserRepresentation(User usr){
        this.email=usr.getEmail();
        this.firstName=usr.getFirstName();
        this.lastName=usr.getLastName();
        this.fullName=usr.getFullName();
    }

    private UserRepresentation(){}

    public String getLastName() { return lastName; }
    public String getFullName() { return fullName; }
    public String getFirstName() { return firstName; }
    public String getEmail() { return email; }
}
