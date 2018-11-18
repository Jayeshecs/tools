/*
 *  Licensed to the Apache Software Foundation (ASF
) under one
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

import javax.inject.Inject;
import javax.jdo.JDOQLTypedQuery;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.isis.applib.events.domain.ActionDomainEvent;
import org.apache.isis.applib.services.config.ConfigurationService;
import org.apache.isis.applib.services.factory.FactoryService;

import com.google.common.base.Throwables;

import domain.statements.dom.impl.cfg.QReaderType;
import domain.statements.dom.impl.cfg.ReaderType;
import domain.statements.dom.srv.AbstractService;
import lombok.extern.slf4j.Slf4j;

@DomainService(
        nature = NatureOfService.DOMAIN,
        objectType = "statements.ReaderTypeService",
        repositoryFor = ReaderType.class
)
@Slf4j
public class ReaderTypeService extends AbstractService {
	
    @Programmatic
	public ReaderType findByNameExact(String name) {
    	JDOQLTypedQuery<ReaderType> q = isisJdoSupport.getJdoPersistenceManager().newJDOQLTypedQuery(ReaderType.class);
        final QReaderType cand = QReaderType.candidate();
        q = q.filter(cand.name.eq(q.stringParameter("name"))
        );
        return q.setParameter("name", name)
                .executeUnique();
	}
    
    @SuppressWarnings("serial")
	public static class CreateDomainEvent extends ActionDomainEvent<ReaderType> {}
    
    @Action(domainEvent = CreateDomainEvent.class)
    public ReaderType register(String name, String setting) {
    	if (name == null || name.trim().isEmpty()) {
			log.warn("Argument 'name' and/or 'setting' cannot be null or empty" + setting);
    		return null;
    	}
    	Class<?> clazz = null;
		try {
			clazz = getClassFromSetting(setting);
			ReaderType readerType = findByNameExact(name);
			if (readerType != null) {
				readerType.setClassName(clazz.getName());
				return repositoryService.persist(readerType);
			}
			/**
			 * Else add it
			 */
			readerType = factoryService.instantiate(ReaderType.class); ///new ReaderType(name, clazz);
			readerType.setName(name);
			readerType.setClassName(clazz.getName());
			return repositoryService.persist(readerType);
		} catch (ClassNotFoundException e) {
			log.error("Class not found for setting - " + setting);
			Throwables.propagate(e);
		}
		return null; // not reachable code
    }

	/**
	 * TODO: API to be promoted in ConfigurationService
	 * 
	 * @param setting
	 * @return
	 * @throws ClassNotFoundException
	 */
	private Class<?> getClassFromSetting(String setting) throws ClassNotFoundException {
		String className = "statements.application.reader.NoopStatmentReader"; // default setting TODO: Externalize in future
		if (setting != null && !setting.trim().isEmpty()) {
			className = configurationService.getProperty(setting);
		}
		Class<?> clazz = Class.forName(className);
		return clazz;
	}
	
    @Inject
	private FactoryService factoryService;
    
    @Inject
    private ConfigurationService configurationService;

}
