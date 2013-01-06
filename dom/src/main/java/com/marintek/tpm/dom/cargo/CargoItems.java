/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marintek.tpm.dom.cargo;

import com.google.common.base.Objects;
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
public class CargoItems extends AbstractFactoryAndRepository{
    
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
    public CargoItem newCargo(
            @Named("Description") String description, 
            @Named("Category") CargoItem.Category category, 
            @Named("HazCode") CargoItem.HazCode hazcode,
            @Named("Amount") double amount,
            @Named("Unit") CargoItem.Unit unit
            )
            {
//        final String ownedBy = getContainer().getUser().getName();
        final CargoItem cargoItem = newTransientInstance(CargoItem.class);
        cargoItem.setDescription(description);
        cargoItem.setCategory(category);
        cargoItem.setHazCode(hazcode);
        cargoItem.setAmount(amount);
        cargoItem.setUnit(unit);

        persist(cargoItem);
        return cargoItem;
    }
    // }}
   @MemberOrder(sequence = "2")
    public List<CargoItem> allCargoItems() {
        final String currentUser = currentUserName();
        final List<CargoItem> items = allInstances(CargoItem.class);
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
   
}
