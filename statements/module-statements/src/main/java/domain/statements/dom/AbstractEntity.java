/**
 * 
 */
package domain.statements.dom;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.services.title.TitleService;

/**
 * @author Prajapati
 *
 */
//@PersistenceCapable
//@Inheritance(strategy=InheritanceStrategy.SUBCLASS_TABLE)
public abstract class AbstractEntity<T> {
	
    @Action(semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE)
    public void delete() {
        final String title = titleService.titleOf(this);
        repositoryService.removeAndFlush(this);
        messageService.informUser(String.format("'%s' deleted", title));
    }

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    protected RepositoryService repositoryService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    protected TitleService titleService;

    @javax.inject.Inject
    @javax.jdo.annotations.NotPersistent
    protected MessageService messageService;

}
