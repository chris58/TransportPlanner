/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marintek.tpm.dom.demand;

import com.google.common.base.Objects;
import com.marintek.tpm.dom.destination.*;
import java.util.List;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;
import org.apache.isis.applib.AbstractDomainObject;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.MultiLine;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.Optional;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.filter.Filter;
import org.joda.time.LocalDate;

/**
 *
 * @author chris
 */
@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY)
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "VERSION")
public class TransportDemand extends AbstractDomainObject implements Comparable<TransportDemand> {

    @Programmatic
    // exclude from the framework's metamodel
    @Override
    public int compareTo(TransportDemand t) {
        return title().compareToIgnoreCase(t.title());
    }

    public String title() {
        StringBuilder sb = new StringBuilder();
        sb.append((pickupDate != null) ? pickupDate.toString() : "??.??.??");
        sb.append(" ");
        sb.append((pickup != null) ? pickup.getCity() : "");
        sb.append(" -> ");
        sb.append((delivery != null) ? delivery.getCity() : " ");
        return sb.toString();
    }

    // {{ OwnedBy (property)
    private String ownedBy;

    @Hidden
    // not shown in the UI
    public String getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(final String ownedBy) {
        this.ownedBy = ownedBy;
    }

    private Destination pickup;
    public Destination getPickup() {
        return pickup;
    }
    public List<Destination> choicesPickup(){
        return destinations.allDestinations();
    }

    public void setPickup(Destination pickup) {
        this.pickup = pickup;
    }
    private LocalDate pickupDate;
    
    @javax.jdo.annotations.Persistent
    public LocalDate getPickupDate() {
        return pickupDate;
    }

    public void setPickupDate(LocalDate pickupDate) {
        this.pickupDate = pickupDate;
    }
    
    private Destination delivery;
    public Destination getDelivery() {
        return delivery;
    }

    public List<Destination> choicesDelivery(){
        return destinations.allDestinations();
    }

    public void setDelivery(Destination delivery) {
        this.delivery = delivery;
    }
    private LocalDate deliveryDate;

    @javax.jdo.annotations.Persistent
    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
    
    
    // {{ Notes (property)
    private String notes;

    @Optional
    @MultiLine(numberOfLines = 5)
    @Hidden(where = Where.ALL_TABLES)
    @MemberOrder(sequence = "6")
    public String getNotes() {
        return notes;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }
    // }}

    public static Filter<TransportDemand> thoseOwnedBy(final String currentUser) {
        return new Filter<TransportDemand>() {
            @Override
            public boolean accept(final TransportDemand item) {
                return Objects.equal(item.getOwnedBy(), currentUser);
            }
        };
    }
    
//    // {{ injected: DomainObjectContainer
//    @SuppressWarnings("unused")
//    private DomainObjectContainer container;
//
//    public void setDomainObjectContainer(final DomainObjectContainer container) {
//        this.container = container;
//    }
//    // }}

    // {{ injected: Destinations
    @SuppressWarnings("unused")
    private Destinations destinations;

    public void setDestinations(final Destinations destinations) {
        this.destinations = destinations;
//        float a = 5/0;
    }

    @SuppressWarnings("unused")
    private TransportDemands transportDemands;

    public void setTransportDemands(TransportDemands transportDemands) {
        this.transportDemands = transportDemands;
//        float a = 5/0;
    }

    
    // }}
}
