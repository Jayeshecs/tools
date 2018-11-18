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

import org.apache.isis.applib.fixturescripts.BuilderScriptAbstract;

import domain.statements.dom.impl.cfg.ReaderType;
import domain.statements.dom.impl.cfg.StatementReader;
import domain.statements.dom.srv.cfg.ReaderTypeService;
import domain.statements.dom.srv.cfg.StatementReaderService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class StatementReaderBuilder extends BuilderScriptAbstract<StatementReader, StatementReaderBuilder> {

    @Getter @Setter
    private String name;

    @Getter @Setter
    private String readerType;

    @Getter
    private StatementReader object;

    @Override
    protected void execute(final ExecutionContext ec) {

        checkParam("name", ec, String.class);
        checkParam("readerType", ec, String.class);
        ReaderType readerTypeObj = readerTypeService.findByNameExact(readerType);
        object = wrap(statementReaderService).create(name, readerTypeObj);
    }

    @javax.inject.Inject
    ReaderTypeService readerTypeService;

    @javax.inject.Inject
    StatementReaderService statementReaderService;

}
