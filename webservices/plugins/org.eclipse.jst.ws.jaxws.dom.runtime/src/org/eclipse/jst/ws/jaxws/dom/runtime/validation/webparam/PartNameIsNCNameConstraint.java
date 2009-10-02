/*******************************************************************************
 * Copyright (c) 2009 by SAP AG, Walldorf. 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     SAP AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.dom.runtime.validation.webparam;

import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.PART_NAME_ATTRIBUTE;
import static org.eclipse.jst.ws.jaxws.dom.runtime.persistence.WPAnnotationFeatures.WP_ANNOTATION;

import javax.jws.WebParam;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.validation.IValidationContext;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jst.ws.jaxws.dom.runtime.api.IWebParam;
import org.eclipse.jst.ws.jaxws.dom.runtime.util.Jee5DomUtils;
import org.eclipse.jst.ws.jaxws.dom.runtime.validation.AbstractValidationConstraint;
import org.eclipse.jst.ws.jaxws.utils.WsdlNamesValidator;

/**
 * Check if partName attribute of {@link WebParam} annotation is correct NCName.  
 * 
 * @author Georgi Vachkov
 */
public class PartNameIsNCNameConstraint  extends AbstractValidationConstraint
{
	private static final String RETURN = "return";//$NON-NLS-1$

	/**
	 * Constructor - no specific processing here 
	 */
	public PartNameIsNCNameConstraint() {
		super(new WpConstraintDescriptor());
	}

	@Override
	protected IStatus doValidate(final IValidationContext ctx) throws JavaModelException
	{
		final IWebParam webParam = (IWebParam)ctx.getTarget();
		if (RETURN.equals(webParam.getImplementation()) || !Jee5DomUtils.getInstance().isPartNameUsed(webParam)) {
			return createOkStatus(webParam);
		}
		
		final IStatus status = WsdlNamesValidator.validateNCName2(PART_NAME_ATTRIBUTE, webParam.getPartName());
		if (!status.isOK()) {
			return createStatus(webParam, status.getMessage(), WP_ANNOTATION, PART_NAME_ATTRIBUTE);
		}
		
		return createOkStatus(webParam);
	}
}