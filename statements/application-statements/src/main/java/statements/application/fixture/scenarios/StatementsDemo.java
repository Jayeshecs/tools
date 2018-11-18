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
package statements.application.fixture.scenarios;

import javax.inject.Inject;

import org.apache.isis.applib.AppManifest2;
import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.metamodel.MetaModelService;

import domain.statements.fixture.cfg.ReaderType_persona;
import domain.statements.fixture.cfg.StatementReader_persona;
import domain.statements.fixture.ref.Category_persona;
import domain.statements.fixture.ref.SubCategory_persona;
import domain.statements.fixture.txn.StatementSource_persona;
import domain.statements.fixture.txn.Transaction_persona;

public class StatementsDemo extends FixtureScript {

    public StatementsDemo() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }

    @Override
    protected void execute(final ExecutionContext ec) {
        AppManifest2 appManifest2 = metaModelService.getAppManifest2();
        ec.executeChild(this, appManifest2.getTeardownFixture());
        ec.executeChild(this, appManifest2.getRefDataSetupFixture());
        
        // Static Data
        ec.executeChild(this, new Category_persona.PersistAll());
        ec.executeChild(this, new SubCategory_persona.PersistAll());
        
        // CFG
        ec.executeChild(this, new ReaderType_persona.PersistAll());
        ec.executeChild(this, new StatementReader_persona.PersistAll());
        
        // Operational
        ec.executeChild(this, new StatementSource_persona.PersistAll());
        ec.executeChild(this, new Transaction_persona.PersistAll());
    }

    @Inject
    MetaModelService metaModelService;
}
