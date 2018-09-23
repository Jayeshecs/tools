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
package statements.application.manifest;

import java.util.List;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import domain.statements.fixture.SimpleObject_persona;
import domain.statements.fixture.txn.Category_persona;
import domain.statements.fixture.txn.StatementSource_persona;
import domain.statements.fixture.txn.SubCategory_persona;

/**
 * Run the app but setting up any fixtures.
 */
public class DomainAppAppManifestWithFixtures extends DomainAppAppManifest {

    @Override
    protected void overrideFixtures(final List<Class<? extends FixtureScript>> fixtureScripts) {
        fixtureScripts.add(SimpleObject_persona.PersistAll.class);
        fixtureScripts.add(Category_persona.PersistAll.class);
        fixtureScripts.add(SubCategory_persona.PersistAll.class);
        fixtureScripts.add(StatementSource_persona.PersistAll.class);
//        fixtureScripts.add(Transaction_persona.PersistAll.class);
    }

}
