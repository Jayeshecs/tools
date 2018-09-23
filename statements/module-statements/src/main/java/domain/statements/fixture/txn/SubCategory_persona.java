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

package domain.statements.fixture.txn;

import org.apache.isis.applib.fixturescripts.PersonaWithBuilderScript;
import org.apache.isis.applib.fixturescripts.PersonaWithFinder;
import org.apache.isis.applib.fixturescripts.setup.PersonaEnumPersistAll;
import org.apache.isis.applib.services.registry.ServiceRegistry;

import domain.statements.dom.impl.txn.SubCategory;
import domain.statements.dom.srv.txn.SubCategoryService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SubCategory_persona implements PersonaWithBuilderScript<SubCategory, SubCategoryBuilder>,
        PersonaWithFinder<SubCategory> {
	ACC_TO_ACC("A/C to A/C"),
	ATM("ATM"),
	BANK_FEE("Bank Fee"),
	BILL_PAYMENT("Bill payment"),
	BOOKS("Books"),
	CAB("Cab"),
	CAR("Car"),
	CAR_MAINTENANCE("Car Maintenance"),
	CASHBACK("Cashback"),
	CHARGER("Charger"),
	CHINTAN("Chintan"),
	CLOTHES("Clothes"),
	COFFEE("Coffee"),
	COOKIES("Cookies"),
	COSMATICS("Cosmatics"),
	CRYPTO("Crypto"),
	DENTIST("Dentist"),
	DINNING("Dinning"),
	DOCTOR("Doctor"),
	DOCTOR_FEES("Doctor Fees"),
	E_BOOK("E-book"),
	ELECTRICALS("Electricals"),
	ELECTRICITY("Electricity"),
	ELECTRONICS("Electronics"),
	EMI("EMI"),
	FOOTWEAR("Footwear"),
	FOR_DAD("For dad"),
	FOR_TANUSHREE("For Tanushree"),
	FRUITS("Fruits"),
	FUEL("Fuel"),
	GROCERIES("Groceries"),
	HAIRCUT("Haircut"),
	HITESHBHAI("Hiteshbhai"),
	HOME("Home"),
	HOME_LOAN("Home Loan"),
	HOUSEHOLD("Household"),
	INCOME_TAX("Income Tax"),
	INTERNET("Internet"),
	JUICE("Juice"),
	KESHAVDHAM("Keshavdham"),
	LOAN_REPAID("Loan Repaid"),
	MATERNITY("Maternity"),
	MEDICINE("Medicine"),
	MEDICLAIM("Mediclaim"),
	MOBILE("Mobile"),
	MOVIE("Movie"),
	ONLINE("Online"),
	OTHER("Other"),
	PAYTM("PayTM"),
	PREMIUM("Premium"),
	PRE_PAYMENT("Pre-payment"),
	PROCESSING_FEES("Processing Fees"),
	SATISH("Satish"),
	SCHOOL_FEES("School Fees"),
	SNACKS("Snacks"),
	STERLING("Sterling"),
	SUNGLASSES("Sunglasses"),
	SWEETS("Sweets"),
	TELEVISION("Television"),
	TEST("Test"),
	TOYS("Toys"),
	VACCINATION("Vaccination"),
	WEB_HOSTING("Web Hosting"),
	WINE("Wine")
	;
    private final String name;

    public SubCategoryBuilder builder() {
        return new SubCategoryBuilder().setName(name);
    }

    public SubCategory findUsing(final ServiceRegistry serviceRegistry) {
    	SubCategoryService subCategoryService = serviceRegistry.lookupService(SubCategoryService.class);
        return subCategoryService.findByNameExact(name);
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<SubCategory_persona, SubCategory, SubCategoryBuilder> {
        public PersistAll() {
            super(SubCategory_persona.class);
        }
    }
}
