/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.marintek.tpm.fixture;

/**
 *
 * @author chris
 */



import com.marintek.tpm.dom.cargo.CargoItem;
import com.marintek.tpm.dom.cargo.CargoItems;
import com.marintek.tpm.dom.destination.Destination;
import com.marintek.tpm.dom.destination.Destinations;
import org.apache.isis.applib.fixtures.AbstractFixture;

public class TpmFixture  extends AbstractFixture{

    @Override
    public void install() {
        createCargo("Some fish", CargoItem.Category.Bulk, CargoItem.HazCode.Frozen, 10, CargoItem.Unit.Pallets);
        createCargo("Some bricks", CargoItem.Category.Bulk, CargoItem.HazCode.Hazardous, 5, CargoItem.Unit.Pallets);

        createDestination("Fishing Company", "Harbour street 5", "London", "GB-001", "Greate Britain");
        createDestination("Customer", "Ola Nordman gate 55", "Trondheim", "N-7000", "Norway");
        getContainer().flush();
    }
    
    protected CargoItem createCargo(String description, 
            CargoItem.Category category, 
            CargoItem.HazCode hazcode,
            double amount,
            CargoItem.Unit unit){
       return cargoItems.newCargo(description, category, hazcode, amount, unit);
    }

    protected Destination createDestination(String name,
            String street,
            String city,
            String postalCode,
            String country){
        return destinations.newDestination(name, street, city, postalCode, country);
    }
    
    
    private CargoItems cargoItems;
    public void setCargoItems(final CargoItems ci){
        cargoItems = ci;
    }

    private Destinations destinations;
    public void setDestinations(final Destinations d){
        destinations = d;
    }


}
