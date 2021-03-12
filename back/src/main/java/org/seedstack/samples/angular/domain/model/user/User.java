package org.seedstack.samples.angular.domain.model.user;

import com.google.common.base.Strings;
import org.seedstack.business.domain.BaseAggregateRoot;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User extends BaseAggregateRoot<String> {
    @Id
    String email;
    String fullName;
    String lastName;
    String firstName;

    public User(String email){
        this.email=email;
    }

    /**Required by hibernate*/
    private User(){}

    public void updateIdentity(String firstName, String lastName, String fullName){
        if(Strings.isNullOrEmpty(firstName) ||Strings.isNullOrEmpty(lastName)){
            throw new IllegalArgumentException("User should have a first name and a last name");
        }
        this.firstName=firstName;
        this.lastName=lastName;
        this.fullName = Strings.isNullOrEmpty(fullName) ? firstName+" "+lastName : fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLastName() {
        return lastName;
    }
}
