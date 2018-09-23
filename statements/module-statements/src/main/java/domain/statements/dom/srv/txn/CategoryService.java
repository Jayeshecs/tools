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
package domain.statements.dom.srv.txn;

import javax.jdo.JDOQLTypedQuery;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.events.domain.ActionDomainEvent;

import domain.statements.dom.impl.txn.Category;
import domain.statements.dom.impl.txn.QCategory;
import domain.statements.dom.srv.AbstractEntityService;

@DomainService(
        nature = NatureOfService.DOMAIN,
        objectType = "statements.CategoryService",
        repositoryFor = Category.class
)
public class CategoryService extends AbstractEntityService<Category> {

    @SuppressWarnings("serial")
	public static class CreateDomainEvent extends ActionDomainEvent<CategoryService> {}
    
    @Action(domainEvent = CreateDomainEvent.class)
    public Category create(final String name) {
        return repositoryService.persist(new Category(name));
    }

    @Programmatic
	public Category findByNameExact(String name) {
    	JDOQLTypedQuery<Category> q = isisJdoSupport.newTypesafeQuery(Category.class);
        final QCategory cand = QCategory.candidate();
        q = q.filter(cand.name.eq(q.stringParameter("name"))
        );
        return q.setParameter("name", name)
                .executeUnique();
	}

}
