/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.context;

import java.util.List;

import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.utility.internal.ArrayTools;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.jpa.core.context.AttributeMapping;
import org.eclipse.jpt.jpa.core.internal.context.JptValidator;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkConvert;
import org.eclipse.jpt.nosql.eclipselink.core.context.persistence.EclipseLinkPersistenceUnit;
import org.eclipse.jpt.nosql.eclipselink.core.internal.DefaultEclipseLinkJpaValidationMessages;
import org.eclipse.jpt.nosql.eclipselink.core.internal.EclipseLinkJpaValidationMessages;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class EclipseLinkConvertValidator
	implements JptValidator
{
	protected final EclipseLinkConvert convert;


	public EclipseLinkConvertValidator(EclipseLinkConvert convert) {
		super();
		this.convert = convert;
	}

	protected AttributeMapping getAttributeMapping() {
		return this.convert.getParent();
	}

	protected EclipseLinkPersistenceUnit getPersistenceUnit() {
		return this.convert.getPersistenceUnit();
	}

	/**
	 * The converters are validated in the persistence unit.
	 * @see org.eclipse.jpt.nosql.eclipselink.core.context.persistence.EclipseLinkPersistenceUnit#validateConverters(List, IReporter)
	 */
	public boolean validate(List<IMessage> messages, IReporter reporter) {
		// converters are validated in the persistence unit
		return this.validateConverterName(messages);
	}

	
	private boolean validateConverterName(List<IMessage> messages) {
		String converterName = this.convert.getConverterName();
		if (converterName == null) {
			return true;
		}

		if (IterableTools.contains(this.getPersistenceUnit().getUniqueConverterNames(), converterName)) {
			return true;
		}

		if (ArrayTools.contains(EclipseLinkConvert.RESERVED_CONVERTER_NAMES, converterName)) {
			return true;
		}

		messages.add(
			DefaultEclipseLinkJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				EclipseLinkJpaValidationMessages.ID_MAPPING_UNRESOLVED_CONVERTER_NAME,
				new String[] {
					converterName,
					this.getAttributeMapping().getName()
				},
				this.getAttributeMapping(),
				this.getValidationTextRange()
			)
		);
		return false;
	}

	protected TextRange getValidationTextRange() {
		return this.getAttributeMapping().getPersistentAttribute().isVirtual() ?
				this.getVirtualValidationTextRange() :
				this.convert.getValidationTextRange();		
	}

	protected TextRange getVirtualValidationTextRange() {
		return getAttributeMapping().getPersistentAttribute().getValidationTextRange();
	}

}
