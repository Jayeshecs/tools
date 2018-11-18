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

package domain.statements.fixture.cfg;

import org.apache.isis.applib.fixturescripts.PersonaWithBuilderScript;
import org.apache.isis.applib.fixturescripts.PersonaWithFinder;
import org.apache.isis.applib.fixturescripts.setup.PersonaEnumPersistAll;
import org.apache.isis.applib.services.registry.ServiceRegistry;

import domain.statements.dom.impl.cfg.StatementReader;
import domain.statements.dom.srv.cfg.StatementReaderService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatementReader_persona implements PersonaWithBuilderScript<StatementReader, StatementReaderBuilder>,
        PersonaWithFinder<StatementReader> {

	HDFC_BANK("hdfc-bank"),
	HDFC_CREDITCARD("hdfc-creditcard")
	;
    private final String name;

    public StatementReaderBuilder builder() {
        return new StatementReaderBuilder().setName(name).setReaderType(name());
    }

    public StatementReader findUsing(final ServiceRegistry serviceRegistry) {
    	StatementReaderService statementReaderService = serviceRegistry.lookupService(StatementReaderService.class);
        return statementReaderService.findByNameExact(name);
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<StatementReader_persona, StatementReader, StatementReaderBuilder> {
        public PersistAll() {
            super(StatementReader_persona.class);
        }
    }
}
