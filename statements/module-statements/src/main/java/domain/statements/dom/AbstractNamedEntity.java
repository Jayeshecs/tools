/**
 * 
 */
package domain.statements.dom;

import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;

import com.google.common.collect.ComparisonChain;

import domain.statements.dom.impl.txn.StatementSource;
import domain.statements.dom.types.Name;

/**
 * Abstract implementation of named entity
 * 
 * @author Prajapati
 */
@PersistenceCapable
@Inheritance(strategy=InheritanceStrategy.SUBCLASS_TABLE)
@lombok.RequiredArgsConstructor
public abstract class AbstractNamedEntity<T extends IEntity> extends AbstractEntity<T> implements Comparable<StatementSource>, IEntity {
	
    @lombok.Getter @lombok.Setter @lombok.NonNull
    @Name private String name;

    public String title() {
        return getTitlePrefix() + getName();
    }

    protected abstract String getTitlePrefix();

	@SuppressWarnings("unchecked")
	@Action(semantics = SemanticsOf.IDEMPOTENT, command = CommandReification.ENABLED, publishing = Publishing.ENABLED, associateWith = "name")
    public T updateName(
            @Name final String name) {
        setName(name);
        return (T)this;
    }
    
    public String default0UpdateName() {
        return getName();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(final StatementSource other) {
        return ComparisonChain.start()
                .compare(this.getName(), other.getName())
                .result();
    }

}
