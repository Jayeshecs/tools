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
package domain.statements.dom.srv.cfg;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import domain.statements.dom.impl.cfg.ReaderType;

@DomainService(
        nature = NatureOfService.DOMAIN,
        objectType = "statements.ReaderTypeService",
        repositoryFor = ReaderType.class
)
public class ReaderTypeService {
	
	private Collection<ReaderType> readerTypes = new LinkedHashSet<>();
	
    @Programmatic
	public ReaderType findByNameExact(String name) {
    	if (name == null || name.trim().isEmpty() || readerTypes.isEmpty()) {
    		return null;
    	}
    	for (ReaderType readerType : readerTypes) {
    		if (readerType.getName().equals(name)) {
    			return readerType;
    		}
    	}
    	return null;
	}
    
    @Programmatic
    public ReaderType register(String name, Class<?> clazz) {
    	if (name == null || name.trim().isEmpty() || clazz == null) {
    		return null;
    	}
    	/**
    	 * if exists then update it
    	 */
    	for (ReaderType readerType : readerTypes) {
    		if (readerType.getName().equals(name)) {
    			readerType.setClazz(clazz);
    			return readerType;
    		}
    	}
    	/**
    	 * Else add it
    	 */
    	ReaderType readerType = new ReaderType(name, clazz);
		readerTypes.add(readerType);
		return readerType;
    }

}
