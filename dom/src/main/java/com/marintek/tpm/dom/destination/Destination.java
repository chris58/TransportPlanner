/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marintek.tpm.dom.destination;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Bounded;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Where;

/**
 *
 * @author chris
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
//@MemberGroups({"General", "Detail"})

// Works ok with Bounded and Autocomplete
// 
//@Bounded
//@AutoComplete(repository=Destinations.class, action="autoComplete")
public class Destination  implements Comparable<Destination> {

    @Programmatic
    // exclude from the framework's metamodel
    @Override
    public int compareTo(Destination t) {
        return title().compareToIgnoreCase(t.title());
    }

    public String title() {
        return getName() + ", " + getCity();
    }

    private String name;
    @MemberOrder(sequence = "1")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private String Street;

    @Hidden(where = Where.ALL_TABLES)
    @MemberOrder(sequence = "2")
    public String getStreet() {
        return Street;
    }

    public void setStreet(String Street) {
        this.Street = Street;
    }

    private String city;
    @MemberOrder(sequence = "3")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    private String postalCode;

    @MemberOrder(sequence = "4")
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    private String country;
    @MemberOrder(sequence = "5")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    // {{ Notes (property)
    private String notes;

    @Optional
    @MultiLine(numberOfLines = 5)
    @Hidden(where = Where.ALL_TABLES)
    @MemberOrder(name="Detail", sequence = "6")
    public String getNotes() {
        return notes;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }
    // }}
}
