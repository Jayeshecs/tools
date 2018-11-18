/**
 * 
 */
package domain.statements.dom.srv;

import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;
import org.apache.isis.applib.services.repository.RepositoryService;

/**
 * @author Prajapati
 *
 */
public abstract class AbstractService {

    @javax.inject.Inject
    protected RepositoryService repositoryService;

    @javax.inject.Inject
    protected IsisJdoSupport isisJdoSupport;

}
