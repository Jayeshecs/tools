/**
 * 
 */
package domain.statements.dom.srv.txn;

import java.util.List;

import javax.jdo.JDOQLTypedQuery;
import javax.jdo.query.BooleanExpression;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.CommandPersistence;
import org.apache.isis.applib.annotation.Contributed;
import org.apache.isis.applib.annotation.InvokeOn;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.Optionality;
import org.apache.isis.applib.annotation.Parameter;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.PromptStyle;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.annotation.Where;
import org.apache.isis.applib.events.domain.ActionDomainEvent;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport_v3_2;
import org.apache.isis.applib.services.repository.RepositoryService;

import domain.statements.dom.impl.txn.QStatementSource;
import domain.statements.dom.impl.txn.StatementSource;
import domain.statements.dom.impl.txn.StatementSourceType;

/**
 * @author Prajapati
 *
 */
@Mixin
public class StatementSource_search {
	
	private StatementSource statementSource;

	public StatementSource_search(StatementSource statementSource) {
		this.statementSource = statementSource;
	}

    @Action(
    		semantics = SemanticsOf.SAFE,
    		invokeOn = InvokeOn.OBJECT_AND_COLLECTION
    )
    public List<StatementSource> $$(
            @Parameter(optionality = Optionality.OPTIONAL)
            @ParameterLayout(named="Type")
            final StatementSourceType type,
            @Parameter(optionality = Optionality.OPTIONAL)
            @ParameterLayout(named="Name")
            final String name
    ) {
    	JDOQLTypedQuery<StatementSource> q = isisJdoSupport.newTypesafeQuery(StatementSource.class);
        final QStatementSource cand = QStatementSource.candidate();
        BooleanExpression expression = null;
        expression = addTypeCriteria(type, cand, expression);
        expression = addNameCriteria(name, cand, expression);
        q = q.filter(
    		expression
        );
        return q.executeList();
    }

	private BooleanExpression addNameCriteria(final String name, final QStatementSource cand, BooleanExpression expression) {
		if (name != null) {
        	BooleanExpression nameExpression = cand.name.indexOf(name).ne(-1);
        	expression = expression != null ? expression.and(nameExpression) : nameExpression;
        }
		return expression;
	}

	private BooleanExpression addTypeCriteria(final StatementSourceType type, final QStatementSource cand, BooleanExpression expression) {
		if (type != null) {
        	BooleanExpression typeExpression = cand.type.eq(type);
        	expression = expression != null ? expression.and(typeExpression) : typeExpression;
        }
		return expression;
	}

    @javax.inject.Inject
    RepositoryService repositoryService;

    @javax.inject.Inject
    IsisJdoSupport_v3_2 isisJdoSupport;
}