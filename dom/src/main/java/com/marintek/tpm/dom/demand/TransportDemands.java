/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marintek.tpm.dom.demand;

import com.marintek.tpm.dom.cargo.*;
import com.google.common.base.Objects;
import com.marintek.tpm.dom.destination.Destination;
import com.marintek.tpm.dom.destination.Destinations;
import com.marintek.tpm.dom.todo.ToDoItem;
import java.util.Collections;
import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionSemantics;
import org.apache.isis.applib.annotation.ActionSemantics.Of;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.annotation.NotInServiceMenu;
import org.apache.isis.applib.filter.Filter;

/**
 *
 * @author chris
 */
public class TransportDemands extends AbstractFactoryAndRepository{
    
    // {{ newToDo  (action)
    /**
     *
     * @param description
     * @param category
     * @param hazcode
     * @param amount
     * @param unit
     * @return
     */
    @MemberOrder(sequence = "1")
    public TransportDemand newTransportDemand(
            @Named("Pickup") Destination pickup, 
            @Named("Delivery") Destination delivery
            )
            {
        final String ownedBy = getContainer().getUser().getName();
        final TransportDemand transportDemand = newTransientInstance(TransportDemand.class);
        transportDemand.setPickup(pickup);
        transportDemand.setDelivery(delivery);
        transportDemand.setOwnedBy(currentUserName());
        persist(transportDemand);
        return transportDemand;
    }
    // }}

    public List<TransportDemand> allTransportDemands(){
        final String currentUser = currentUserName();
        final List<TransportDemand> items = allMatches(TransportDemand.class, TransportDemand.thoseOwnedBy(currentUser));
        Collections.sort(items);
        return items;
    }
        

        
        
    @NotInServiceMenu
    @ActionSemantics(Of.SAFE)
    //@MemberOrder(sequence = "5")
    public List<CargoItem> similarTo(final CargoItem cargoItem) {
        return allMatches(CargoItem.class, new Filter<CargoItem>() {
            @Override
            public boolean accept(CargoItem t) {
                return t != cargoItem && Objects.equal(cargoItem.getCategory(), t.getCategory());
            }
        });
    }
   
    // {{ helpers
//    protected boolean ownedByCurrentUser(final CargoItem t) {
//        return Objects.equal(t.getOwnedBy(), currentUserName());
//    }
    protected String currentUserName() {
        return getContainer().getUser().getName();
    }
    // }}
    private Destinations destinations;

    public void setDestinations(final Destinations destinations) {
        this.destinations = destinations;
    }
   
}
