/*******************************************************************************
 * Copyright (c) 2010, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.context.orm;

import java.util.List;
import org.eclipse.jpt.jpa.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlBasicMap;
import org.eclipse.jpt.nosql.eclipselink.core.internal.DefaultEclipseLinkJpaValidationMessages;
import org.eclipse.jpt.nosql.eclipselink.core.internal.EclipseLinkJpaValidationMessages;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class OrmEclipseLinkBasicMapMapping2_1
	extends AbstractOrmEclipseLinkBasicMapMapping
{
	public OrmEclipseLinkBasicMapMapping2_1(OrmPersistentAttribute parent, XmlBasicMap xmlMapping) {
		super(parent, xmlMapping);
	}

	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		messages.add(
			DefaultEclipseLinkJpaValidationMessages.buildMessage(
				IMessage.NORMAL_SEVERITY,
				EclipseLinkJpaValidationMessages.BASIC_MAP_MAPPING_DEPRECATED,
				new String[] {this.getName()},
				this,
				this.getValidationTextRange()
			)
		);
	}
}
