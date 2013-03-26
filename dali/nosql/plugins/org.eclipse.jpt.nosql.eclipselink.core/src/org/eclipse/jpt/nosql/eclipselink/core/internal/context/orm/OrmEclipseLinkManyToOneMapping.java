/*******************************************************************************
 * Copyright (c) 2008, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.context.orm;

import java.util.List;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.jpa.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.jpa.core.internal.context.orm.AbstractOrmManyToOneMapping;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlManyToOne;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkJoinFetch;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkManyToOneMapping2_0;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.EclipseLinkPersistenceUnit;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class OrmEclipseLinkManyToOneMapping
	extends AbstractOrmManyToOneMapping<XmlManyToOne>
	implements EclipseLinkManyToOneMapping2_0
{
	protected final OrmEclipseLinkJoinFetch joinFetch;


	protected OrmEclipseLinkManyToOneMapping(OrmPersistentAttribute parent, XmlManyToOne xmlMapping) {
		super(parent, xmlMapping);
		this.joinFetch = new OrmEclipseLinkJoinFetch(this);
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.joinFetch.synchronizeWithResourceModel();
	}

	@Override
	public void update() {
		super.update();
		this.joinFetch.update();
	}


	// ********** join fetch **********

	public EclipseLinkJoinFetch getJoinFetch() {
		return this.joinFetch;
	}


	// ********** validation **********

	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		// TODO - join fetch validation
	}

	// ********** completion proposals **********

	@Override
	@SuppressWarnings("unchecked")
	protected Iterable<String> getCandidateTargetEntityClassNames() {
		return IterableTools.concatenate(
				super.getCandidateTargetEntityClassNames(),
				IterableTools.sort(((EclipseLinkPersistenceUnit) this.getPersistenceUnit()).getEclipseLinkDynamicPersistentTypeNames())
				);
	}
}