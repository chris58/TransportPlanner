/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marintek.tpm.dom.cargo;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.annotation.Where;

/**
 *
 * @author chris
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
//@MemberGroups({"General", "Detail"})

public class CargoItem  implements Comparable<CargoItem> {

    @Programmatic
    // exclude from the framework's metamodel
    @Override
    public int compareTo(CargoItem t) {
        return getDescription().compareToIgnoreCase(t.getDescription());
    }

    public static enum Category {

        Container, Breakbulk, Bulk;
    }

    public static enum HazCode {

        None, Hazardous, Refirgerated, Frozen;
    }

    public static enum Unit {

        Pieces, Pallets, ton, kg, l;
    }
    // {{ Description
    private String description;

    @MemberOrder(sequence = "1")
    @Title
    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
    // }}
    // {{ Category
    private CargoItem.Category category;

    @MemberOrder(sequence = "2")
    public CargoItem.Category getCategory() {
        return category;
    }

    public void setCategory(final CargoItem.Category category) {
        this.category = category;
    }
    // }}
    // {{ HazCode
    private CargoItem.HazCode hazcode;

    @MemberOrder(sequence = "3")
    public CargoItem.HazCode getHazCode() {
        return hazcode;
    }

    public void setHazCode(final CargoItem.HazCode hazcode) {
        this.hazcode = hazcode;
    }
    // }}
    private double amount;

    @MemberOrder(name="Detail", sequence = "4")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    // {{ Unit
    private CargoItem.Unit unit;

    @MemberOrder(name="Detail", sequence = "5")
    public CargoItem.Unit getUnit() {
        return unit;
    }

    public void setUnit(final CargoItem.Unit unit) {
        this.unit = unit;
    }
    // }}
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
