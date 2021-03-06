/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.context.java;

import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkTenantDiscriminatorColumnAnnotation2_3;
import org.eclipse.jpt.nosql.eclipselink.core.context.TenantDiscriminatorColumn2_3;

/**
 * tenant discriminator column
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * 
 * @version 3.3
 * @since 3.1
 */
public interface JavaTenantDiscriminatorColumn2_3
	extends TenantDiscriminatorColumn2_3
{
	EclipseLinkTenantDiscriminatorColumnAnnotation2_3 getColumnAnnotation();

}
