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

import org.apache.isis.applib.fixturescripts.BuilderScriptAbstract;

import domain.statements.dom.impl.ref.Category;
import domain.statements.dom.srv.ref.CategoryService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
public class CategoryBuilder extends BuilderScriptAbstract<Category, CategoryBuilder> {

    @Getter @Setter
    private String name;

    @Getter
    private Category object;

    @Override
    protected void execute(final ExecutionContext ec) {

        checkParam("name", ec, String.class);

        object = wrap(categoryService).create(name);
    }

    @javax.inject.Inject
    CategoryService categoryService;

}
