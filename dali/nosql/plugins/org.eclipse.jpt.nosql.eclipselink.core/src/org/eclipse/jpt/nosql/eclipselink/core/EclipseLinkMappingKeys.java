/*******************************************************************************
 * Copyright (c) 2008, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core;

/**
 * 
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
@SuppressWarnings("nls")
public interface EclipseLinkMappingKeys {

	String BASIC_COLLECTION_ATTRIBUTE_MAPPING_KEY = "basicCollection";
	String BASIC_MAP_ATTRIBUTE_MAPPING_KEY = "basicMap";
	String TRANSFORMATION_ATTRIBUTE_MAPPING_KEY = "transformation";
	String VARIABLE_ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY = "variableOneToOne";

	//EclipseLink 2.3 mapping keys
	String STRUCTURE_ATTRIBUTE_MAPPING_KEY = "structure";
	String ARRAY_ATTRIBUTE_MAPPING_KEY = "array";
}