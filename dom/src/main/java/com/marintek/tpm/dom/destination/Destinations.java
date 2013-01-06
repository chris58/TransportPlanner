/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marintek.tpm.dom.destination;

import java.util.Collections;
import java.util.List;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.AutoComplete;
import org.apache.isis.applib.annotation.Hidden;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Named;
import org.apache.isis.applib.filter.Filter;

/**
 *
 * @author chris
 */
public class Destinations extends AbstractFactoryAndRepository{
    
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
    public Destination newDestination(
            @Named("Name") String name, 
            @Named("Street") String street, 
            @Named("City") String city,
            @Named("Postal Code") String postalCode,
            @Named("Country") String country
            )
            {
//        final String ownedBy = getContainer().getUser().getName();
        final Destination destination = newTransientInstance(Destination.class);
        destination.setCity(city);
        destination.setCountry(country);
        destination.setName(name);
        destination.setPostalCode(postalCode);
        destination.setStreet(street);
        
        persist(destination);
        return destination;
    }
    // }}
   @MemberOrder(sequence = "2")
    public List<Destination> allDestinations() {
        final List<Destination> items = allInstances(Destination.class);
        Collections.sort(items);
        return items;
    }    
    // {{ autoComplete (hidden)
    @Hidden
    public List<Destination> autoComplete(final String description) {
        return allMatches(Destination.class, new Filter<Destination>() {
            @Override
            public boolean accept(final Destination t) {
                return t.title().contains(description);
            }

        });
    }
    // }}
//
//    @NotInServiceMenu
//    @ActionSemantics(Of.SAFE)
//    //@MemberOrder(sequence = "5")
//    public List<CargoItem> similarTo(final CargoItem cargoItem) {
//        return allMatches(CargoItem.class, new Filter<CargoItem>() {
//            @Override
//            public boolean accept(CargoItem t) {
//                return t != cargoItem && Objects.equal(cargoItem.getCategory(), t.getCategory());
//            }
//        });
//    }
//   
//    // {{ helpers
////    protected boolean ownedByCurrentUser(final CargoItem t) {
////        return Objects.equal(t.getOwnedBy(), currentUserName());
////    }
//    protected String currentUserName() {
//        return getContainer().getUser().getName();
//    }
//    // }}
   
}
