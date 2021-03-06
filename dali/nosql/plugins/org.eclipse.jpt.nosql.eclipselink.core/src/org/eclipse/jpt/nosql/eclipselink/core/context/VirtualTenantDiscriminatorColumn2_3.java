/*******************************************************************************
 * Copyright (c) 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.context;

import org.eclipse.jpt.jpa.core.context.VirtualNamedDiscriminatorColumn;
import org.eclipse.jpt.jpa.core.context.VirtualTableColumn;

/**
 * Virtual tenant discriminator column
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface VirtualTenantDiscriminatorColumn2_3
	extends VirtualNamedDiscriminatorColumn, VirtualTableColumn, ReadOnlyTenantDiscriminatorColumn2_3
{
	ReadOnlyTenantDiscriminatorColumn2_3 getOverriddenColumn();

	// ********** owner **********

	/**
	 * Interface allowing the virtual column to be get the column it overrides.
	 */
	interface Owner
		extends ReadOnlyTenantDiscriminatorColumn2_3.Owner
	{
		/**
		 * Return the column overridden by the virtual column.
		 */
		ReadOnlyTenantDiscriminatorColumn2_3 resolveOverriddenColumn();
	}
}
