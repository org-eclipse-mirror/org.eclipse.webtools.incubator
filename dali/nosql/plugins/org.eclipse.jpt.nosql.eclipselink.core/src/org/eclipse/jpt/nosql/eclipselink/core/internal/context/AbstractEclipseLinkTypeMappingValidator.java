/*******************************************************************************
 * Copyright (c) 2010, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.context;

import java.util.List;
import org.eclipse.jpt.common.core.resource.java.JavaResourceType;
import org.eclipse.jpt.jpa.core.context.TypeMapping;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.AbstractTypeMappingValidator;
import org.eclipse.jpt.jpa.core.validation.JptJpaCoreValidationMessages;
import org.eclipse.jpt.nosql.eclipselink.core.internal.DefaultEclipseLinkJpaValidationMessages;
import org.eclipse.jpt.nosql.eclipselink.core.internal.EclipseLinkJpaValidationMessages;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

public abstract class AbstractEclipseLinkTypeMappingValidator<T extends TypeMapping>
	extends AbstractTypeMappingValidator<T>
{
	protected AbstractEclipseLinkTypeMappingValidator(T typeMapping) {
		super(typeMapping);
	}


	@Override
	protected void validateType(List<IMessage> messages) {
		JavaResourceType jrt = this.getJavaResourceType();
		if (jrt.getTypeBinding().isMemberTypeDeclaration() && ! jrt.isStatic()) {
			messages.add(this.buildEclipseLinkTypeMessage(EclipseLinkJpaValidationMessages.TYPE_MAPPING_MEMBER_CLASS_NOT_STATIC));
		}
		if ( ! jrt.hasNoArgConstructor()) {
			messages.add(this.buildTypeMessage(JptJpaCoreValidationMessages.TYPE_MAPPING_CLASS_MISSING_NO_ARG_CONSTRUCTOR));
		}
	}

	protected IMessage buildEclipseLinkTypeMessage(String msgID) {
		return DefaultEclipseLinkJpaValidationMessages.buildMessage(
				IMessage.HIGH_SEVERITY,
				msgID,
				new String[] {this.typeMapping.getName()},
				this.typeMapping,
				this.typeMapping.getValidationTextRange()
			);
	}
}
