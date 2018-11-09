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

import domain.statements.dom.impl.ref.Category;
import domain.statements.dom.srv.ref.CategoryService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Category_persona implements PersonaWithBuilderScript<Category, CategoryBuilder>,
        PersonaWithFinder<Category> {

	CASH("Cash"),
	CREDIT_CARD("Creditcard"),
	CREDIT_CARD_LOADN("Creditcard Loan"),
	DEBIBT_CHARGES("Debit/Charges"),
	EDUCATION("Education"),
	ENTERTAINMENT("Entertainment"),
	FOOD("Food"),
	HEALTHCARE("Healthcare"),
	HOME_LOAN("Home Loan"),
	INSURANCE("Insurance"),
	INVESTMENT("Investment"),
	LOAN("Loan"),
	MAINTENANCE("Maintenance"),
	MEDICAL("Medical"),
	MEMBERSHIP("Membership"),
	MISCELLANEOUS("Miscellaneous"),
	PROFESSION("Profession"),
	SHOPPING("Shopping"),
	TRAVEL("Travel"),
	UTILITY_BILL("Utilities Bill"),
	EMI("EMI")
	;
    private final String name;

    public CategoryBuilder builder() {
        return new CategoryBuilder().setName(name);
    }

    public Category findUsing(final ServiceRegistry serviceRegistry) {
    	CategoryService categoryService = serviceRegistry.lookupService(CategoryService.class);
        return categoryService.findByNameExact(name);
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<Category_persona, Category, CategoryBuilder> {
        public PersistAll() {
            super(Category_persona.class);
        }
    }
}
