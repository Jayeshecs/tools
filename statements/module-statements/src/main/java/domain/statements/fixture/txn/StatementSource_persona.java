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

import domain.statements.dom.impl.ref.StatementSourceType;
import domain.statements.dom.impl.txn.StatementSource;
import domain.statements.dom.srv.txn.StatementSourceService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StatementSource_persona implements PersonaWithBuilderScript<StatementSource, StatementSourceBuilder>,
        PersonaWithFinder<StatementSource> {

	HDFC_1668("HDFC_1668", StatementSourceType.SAVING_ACCOUNT),
	HDFC_3789("HDFC_3789", StatementSourceType.SAVING_ACCOUNT),
	HDFC_9371("HDFC_9371", StatementSourceType.SAVING_ACCOUNT),
	CARD_NO_4854_99XX_XXXX_6394("Card No: 4854 99XX XXXX 6394", StatementSourceType.CREDIT_CARD),
	CARD_NO_4854_99XX_XXXX_0335("Card No: 4854 99XX XXXX 0335", StatementSourceType.CREDIT_CARD),
	CARD_NO_4639_17XX_XXXX_8535("Card No: 4639 17XX XXXX 8535", StatementSourceType.CREDIT_CARD)
	;
    private final String name;

    private final StatementSourceType statementSourcetype;

    public StatementSourceBuilder builder() {
        return new StatementSourceBuilder().setName(name).setStatementSourcetype(statementSourcetype);
    }

    public StatementSource findUsing(final ServiceRegistry serviceRegistry) {
    	StatementSourceService statementSourceService = serviceRegistry.lookupService(StatementSourceService.class);
        return statementSourceService.findByNameExact(name);
    }

    public static class PersistAll
            extends PersonaEnumPersistAll<StatementSource_persona, StatementSource, StatementSourceBuilder> {
        public PersistAll() {
            super(StatementSource_persona.class);
        }
    }
}
