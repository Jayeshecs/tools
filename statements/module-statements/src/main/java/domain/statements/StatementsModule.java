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
package domain.statements;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.isis.applib.ModuleAbstract;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.fixturescripts.teardown.TeardownFixtureAbstract2;

import domain.statements.dom.impl.SimpleObject;
import domain.statements.dom.impl.txn.Category;
import domain.statements.dom.impl.txn.StatementSource;
import domain.statements.dom.impl.txn.SubCategory;
import domain.statements.dom.impl.txn.Transaction;

@XmlRootElement(name = "module")
public class StatementsModule extends ModuleAbstract {

    @Override
    public FixtureScript getTeardownFixture() {
        return new TeardownFixtureAbstract2() {
            @Override
            protected void execute(ExecutionContext executionContext) {
                deleteFrom(SimpleObject.class);
                deleteFrom(Transaction.class);
                deleteFrom(StatementSource.class);
                deleteFrom(Category.class);
                deleteFrom(SubCategory.class);
            }
        };
    }

    public static class PropertyDomainEvent<S,T>
            extends org.apache.isis.applib.events.domain.PropertyDomainEvent<S,T> {}
    public static class CollectionDomainEvent<S,T>
            extends org.apache.isis.applib.events.domain.CollectionDomainEvent<S,T> {}
    public static class ActionDomainEvent<S> extends
            org.apache.isis.applib.events.domain.ActionDomainEvent<S> {}
}
