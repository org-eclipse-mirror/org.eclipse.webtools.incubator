/*******************************************************************************
 * Copyright (c) 2009, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal;

import java.util.ArrayList;
import org.eclipse.jpt.common.core.JptResourceType;
import org.eclipse.jpt.common.utility.internal.collection.CollectionTools;
import org.eclipse.jpt.jpa.core.JpaPlatformProvider;
import org.eclipse.jpt.jpa.core.JpaResourceModelProvider;
import org.eclipse.jpt.jpa.core.ResourceDefinition;
import org.eclipse.jpt.jpa.core.context.java.DefaultJavaAttributeMappingDefinition;
import org.eclipse.jpt.jpa.core.context.java.JavaAttributeMappingDefinition;
import org.eclipse.jpt.jpa.core.context.java.JavaTypeMappingDefinition;
import org.eclipse.jpt.jpa.core.internal.AbstractJpaPlatformProvider;
import org.eclipse.jpt.jpa.core.internal.JarResourceModelProvider;
import org.eclipse.jpt.jpa.core.internal.JavaResourceModelProvider;
import org.eclipse.jpt.jpa.core.internal.OrmResourceModelProvider;
import org.eclipse.jpt.jpa.core.internal.PersistenceResourceModelProvider;
import org.eclipse.jpt.jpa.core.internal.context.java.JarDefinition;
import org.eclipse.jpt.jpa.core.internal.context.java.JavaSourceFileDefinition;
import org.eclipse.jpt.jpa.core.internal.context.java.JavaTransientMappingDefinition;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.orm.GenericOrmXmlDefinition;
import org.eclipse.jpt.jpa.core.internal.jpa2.context.orm.GenericOrmXml2_0Definition;
import org.eclipse.jpt.jpa.core.internal.jpa2.context.persistence.GenericPersistenceXml2_0Definition;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaBasicCollectionMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaBasicMapMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaBasicMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaElementCollectionMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaEmbeddableDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaEmbeddedIdMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaEmbeddedMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaEntityDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaIdMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaManyToManyMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaManyToOneMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaMappedSuperclassDefinition2_1;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaOneToManyMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaOneToOneMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaTransformationMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaVariableOneToOneMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.java.EclipseLinkJavaVersionMappingDefinition2_0;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.orm.EclipseLinkOrmXml1_1Definition;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.orm.EclipseLinkOrmXml1_2Definition;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.orm.EclipseLinkOrmXml2_0Definition;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.orm.EclipseLinkOrmXml2_1Definition;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.orm.EclipseLinkOrmXmlDefinition;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.persistence.EclipseLink2_0PersistenceXmlDefinition;
import org.eclipse.jpt.nosql.eclipselink.core.internal.context.persistence.EclipseLinkPersistenceXmlDefinition;

/**
 * EclipseLink 2.1 platform config
 */
public class EclipseLink2_1JpaPlatformProvider
	extends AbstractJpaPlatformProvider
{
	// singleton
	private static final JpaPlatformProvider INSTANCE = new EclipseLink2_1JpaPlatformProvider();

	/**
	 * Return the singleton
	 */
	public static JpaPlatformProvider instance() {
		return INSTANCE;
	}

	/**
	 * Enforce singleton usage
	 */
	private EclipseLink2_1JpaPlatformProvider() {
		super();
	}


	// ********** resource models **********

	@Override
	protected void addMostRecentSupportedResourceTypesTo(ArrayList<JptResourceType> types) {
		CollectionTools.addAll(types, MOST_RECENT_SUPPORTED_RESOURCE_TYPES);
	}

	// order should not be important here
	protected static final JptResourceType[] MOST_RECENT_SUPPORTED_RESOURCE_TYPES = new JptResourceType[] {
		JavaSourceFileDefinition.instance().getResourceType(),
		JarDefinition.instance().getResourceType(),
		GenericPersistenceXml2_0Definition.instance().getResourceType(),
		GenericOrmXml2_0Definition.instance().getResourceType(),
		org.eclipse.jpt.nosql.eclipselink.core.internal.context.orm.EclipseLinkOrmXml2_1Definition.instance().getResourceType(),
	};

	@Override
	protected void addResourceModelProvidersTo(ArrayList<JpaResourceModelProvider> providers) {
		CollectionTools.addAll(providers, RESOURCE_MODEL_PROVIDERS);
	}

	// order should not be important here
	protected static final JpaResourceModelProvider[] RESOURCE_MODEL_PROVIDERS = new JpaResourceModelProvider[] {
		JavaResourceModelProvider.instance(),
		JarResourceModelProvider.instance(),
		PersistenceResourceModelProvider.instance(),
		OrmResourceModelProvider.instance(),
		EclipseLinkOrmResourceModelProvider.instance()
	};


	// ********* Java type mappings *********

	@Override
	protected void addJavaTypeMappingDefinitionsTo(ArrayList<JavaTypeMappingDefinition> definitions) {
		CollectionTools.addAll(definitions, JAVA_TYPE_MAPPING_DEFINITIONS);
	}

	// order matches that used by EclipseLink
	// NB: no EclipseLink-specific mappings
	protected static final JavaTypeMappingDefinition[] JAVA_TYPE_MAPPING_DEFINITIONS = new JavaTypeMappingDefinition[] {
		EclipseLinkJavaEntityDefinition2_0.instance(),
		EclipseLinkJavaEmbeddableDefinition2_0.instance(),
		EclipseLinkJavaMappedSuperclassDefinition2_1.instance()
	};


	// ********* Java attribute mappings *********

	@Override
	protected void addDefaultJavaAttributeMappingDefinitionsTo(ArrayList<DefaultJavaAttributeMappingDefinition> definitions) {
		CollectionTools.addAll(definitions, DEFAULT_JAVA_ATTRIBUTE_MAPPING_DEFINITIONS);
	}

	// order matches that used by EclipseLink
	// NB: no change from EclipseLink 1.2 to 2.0
	protected static final DefaultJavaAttributeMappingDefinition[] DEFAULT_JAVA_ATTRIBUTE_MAPPING_DEFINITIONS = new DefaultJavaAttributeMappingDefinition[] {
		EclipseLinkJavaEmbeddedMappingDefinition2_0.instance(),
		EclipseLinkJavaOneToManyMappingDefinition2_0.instance(),
		EclipseLinkJavaOneToOneMappingDefinition2_0.instance(),
		EclipseLinkJavaVariableOneToOneMappingDefinition2_0.instance(),
		EclipseLinkJavaBasicMappingDefinition2_0.instance()
	};

	@Override
	protected void addSpecifiedJavaAttributeMappingDefinitionsTo(ArrayList<JavaAttributeMappingDefinition> definitions) {
		CollectionTools.addAll(definitions, SPECIFIED_JAVA_ATTRIBUTE_MAPPING_DEFINITIONS);
	}

	// order matches that used by EclipseLink
	protected static final JavaAttributeMappingDefinition[] SPECIFIED_JAVA_ATTRIBUTE_MAPPING_DEFINITIONS = new JavaAttributeMappingDefinition[] {
		JavaTransientMappingDefinition.instance(),
		EclipseLinkJavaBasicCollectionMappingDefinition2_0.instance(),
		EclipseLinkJavaBasicMapMappingDefinition2_0.instance(),
		EclipseLinkJavaElementCollectionMappingDefinition2_0.instance(),
		EclipseLinkJavaIdMappingDefinition2_0.instance(),
		EclipseLinkJavaVersionMappingDefinition2_0.instance(),
		EclipseLinkJavaBasicMappingDefinition2_0.instance(),
		EclipseLinkJavaEmbeddedMappingDefinition2_0.instance(),
		EclipseLinkJavaEmbeddedIdMappingDefinition2_0.instance(),
		EclipseLinkJavaTransformationMappingDefinition2_0.instance(),
		EclipseLinkJavaManyToManyMappingDefinition2_0.instance(),
		EclipseLinkJavaManyToOneMappingDefinition2_0.instance(),
		EclipseLinkJavaOneToManyMappingDefinition2_0.instance(),
		EclipseLinkJavaOneToOneMappingDefinition2_0.instance(),
		EclipseLinkJavaVariableOneToOneMappingDefinition2_0.instance()
	};


	// ********** resource definitions **********

	@Override
	protected void addResourceDefinitionsTo(ArrayList<ResourceDefinition> definitions) {
		CollectionTools.addAll(definitions, RESOURCE_DEFINITIONS);
	}

	protected static final ResourceDefinition[] RESOURCE_DEFINITIONS = new ResourceDefinition[] {
		JavaSourceFileDefinition.instance(),
		JarDefinition.instance(),
		EclipseLinkPersistenceXmlDefinition.instance(),
		EclipseLink2_0PersistenceXmlDefinition.instance(),
		GenericOrmXmlDefinition.instance(),
		GenericOrmXml2_0Definition.instance(),
		EclipseLinkOrmXmlDefinition.instance(),
		EclipseLinkOrmXml1_1Definition.instance(),
		EclipseLinkOrmXml1_2Definition.instance(),
		EclipseLinkOrmXml2_0Definition.instance(),
		EclipseLinkOrmXml2_1Definition.instance()
	};
}
