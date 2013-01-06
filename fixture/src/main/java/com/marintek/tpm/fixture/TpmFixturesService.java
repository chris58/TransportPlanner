/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package com.marintek.tpm.fixture;


import com.marintek.tpm.dom.cargo.CargoItems;
import com.marintek.tpm.dom.destination.Destinations;
import org.apache.isis.applib.AbstractService;
import org.apache.isis.applib.annotation.Named;

/**
 * Enables fixtures to be installed from the application.
 */
@Named("TPM Fixtures")
public class TpmFixturesService extends AbstractService {

    public String install() {
        final TpmFixture fixture = new TpmFixture();
        fixture.setContainer(getContainer());
        fixture.setCargoItems(cargoItems);
        fixture.setDestinations(destinations);
        fixture.install();
        return "TPM fixtures installed";
    }

    private CargoItems cargoItems;

    public void setCargoItems(final CargoItems cargoItems) {
        this.cargoItems = cargoItems;
    }

    private Destinations destinations;
    public void setDestinations(final Destinations d){
        destinations = d;
    }
}
