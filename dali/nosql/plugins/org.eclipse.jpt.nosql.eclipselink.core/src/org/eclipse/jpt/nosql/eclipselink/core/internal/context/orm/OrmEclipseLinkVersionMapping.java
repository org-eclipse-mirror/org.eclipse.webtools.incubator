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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jpt.common.core.internal.utility.JDTTools;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.utility.internal.ArrayTools;
import org.eclipse.jpt.common.utility.internal.StringTools;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.jpa.core.context.orm.OrmConverter;
import org.eclipse.jpt.jpa.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.jpa.core.internal.context.MappingTools;
import org.eclipse.jpt.jpa.core.internal.context.orm.AbstractOrmVersionMapping;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlVersion;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkAccessType;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkMutable;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkVersionMapping;
import org.eclipse.jpt.nosql.eclipselink.core.context.orm.EclipseLinkOrmConvertibleMapping;
import org.eclipse.jpt.nosql.eclipselink.core.context.orm.OrmEclipseLinkConverterContainer;
import org.eclipse.jpt.nosql.eclipselink.core.internal.DefaultEclipseLinkJpaValidationMessages;
import org.eclipse.jpt.nosql.eclipselink.core.internal.EclipseLinkJpaValidationMessages;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class OrmEclipseLinkVersionMapping
	extends AbstractOrmVersionMapping<XmlVersion>
	implements
		EclipseLinkVersionMapping,
		EclipseLinkOrmConvertibleMapping, 
		OrmEclipseLinkConverterContainer.Owner
{	
	protected final OrmEclipseLinkMutable mutable;
	
	protected final OrmEclipseLinkConverterContainer converterContainer;
	
	
	public OrmEclipseLinkVersionMapping(OrmPersistentAttribute parent, XmlVersion xmlMapping) {
		super(parent, xmlMapping);
		this.mutable = new OrmEclipseLinkMutable(this);
		this.converterContainer = this.buildConverterContainer();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.mutable.synchronizeWithResourceModel();
		this.converterContainer.synchronizeWithResourceModel();
	}

	@Override
	public void update() {
		super.update();
		this.mutable.update();
		this.converterContainer.update();
	}


	// ********** attribute type **********

	@Override
	protected String buildSpecifiedAttributeType() {
		return this.xmlAttributeMapping.getAttributeType();
	}

	@Override
	protected void setSpecifiedAttributeTypeInXml(String attributeType) {
		this.xmlAttributeMapping.setAttributeType(attributeType);
	}


	// ********** mutable **********

	public EclipseLinkMutable getMutable() {
		return this.mutable;
	}

	// ********** converters **********

	public OrmEclipseLinkConverterContainer getConverterContainer() {
		return this.converterContainer;
	}

	protected OrmEclipseLinkConverterContainer buildConverterContainer() {
		return new OrmEclipseLinkConverterContainerImpl(this, this, this.xmlAttributeMapping);
	}

	public int getNumberSupportedConverters() {
		return 1;
	}


	// ********** converter adapters **********

	/**
	 * put the EclipseLink convert adapter first - this is the order EclipseLink searches
	 */
	@Override
	protected Iterable<OrmConverter.Adapter> getConverterAdapters() {
		return IterableTools.insert(OrmEclipseLinkConvert.Adapter.instance(), super.getConverterAdapters());
	}


	//************ refactoring ************

	@Override
	@SuppressWarnings("unchecked")
	public Iterable<ReplaceEdit> createMoveTypeEdits(IType originalType, IPackageFragment newPackage) {
		return IterableTools.concatenate(
			super.createMoveTypeEdits(originalType, newPackage),
			this.converterContainer.createMoveTypeEdits(originalType, newPackage)
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterable<ReplaceEdit> createRenamePackageEdits(IPackageFragment originalPackage, String newName) {
		return IterableTools.concatenate(
			super.createRenamePackageEdits(originalPackage, newName),
			this.converterContainer.createRenamePackageEdits(originalPackage, newName)
		);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Iterable<ReplaceEdit> createRenameTypeEdits(IType originalType, String newName) {
		return IterableTools.concatenate(
			super.createRenameTypeEdits(originalType, newName),
			this.converterContainer.createRenameTypeEdits(originalType, newName)
		);
	}


	// ********** validation **********
	
	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		// TODO mutable validation
	}

	@Override
	protected void validateAttributeType(List<IMessage> messages) {
		//TODO copied from OrmEclipseLinkBasicMapping
		if (this.isVirtualAccess()) {
			if (StringTools.isBlank(this.getAttributeType())) {
				messages.add(
					DefaultEclipseLinkJpaValidationMessages.buildMessage(
						IMessage.HIGH_SEVERITY,
						EclipseLinkJpaValidationMessages.VIRTUAL_ATTRIBUTE_NO_ATTRIBUTE_TYPE_SPECIFIED,
						new String[] {this.getName()},
						this,
						this.getAttributeTypeTextRange()
					)
				);
				return;
			}
			if (MappingTools.typeIsBasic(this.getJavaProject(), this.getFullyQualifiedAttributeType())) {
				return;
			}
			IType jdtType = JDTTools.findType(this.getJavaProject(), this.getFullyQualifiedAttributeType());
			if (jdtType == null && this.getResolvedAttributeType() == null) {
				messages.add(
					DefaultEclipseLinkJpaValidationMessages.buildMessage(
						IMessage.HIGH_SEVERITY,
						EclipseLinkJpaValidationMessages.VIRTUAL_ATTRIBUTE_ATTRIBUTE_TYPE_DOES_NOT_EXIST,
						new String[] {this.getFullyQualifiedAttributeType()},
						this,
						this.getAttributeTypeTextRange()
					)
				);
				return;
			}
		}
		if (!ArrayTools.contains(SUPPORTED_TYPE_NAMES, this.getFullyQualifiedAttributeType())) {
			messages.add(
					DefaultEclipseLinkJpaValidationMessages.buildMessage(
							IMessage.HIGH_SEVERITY,
							EclipseLinkJpaValidationMessages.PERSISTENT_ATTRIBUTE_INVALID_VERSION_MAPPING_TYPE,
							new String[] {this.getName()},
							this,
							this.getAttributeTypeTextRange()
					)
			);
		}
	}

	protected boolean isVirtualAccess() {
		return getPersistentAttribute().getAccess() == EclipseLinkAccessType.VIRTUAL;
	}

	protected TextRange getAttributeTypeTextRange() {
		return this.getValidationTextRange(this.xmlAttributeMapping.getAttributeTypeTextRange());
	}

	// ********** completion proposals **********

	@Override
	public Iterable<String> getCompletionProposals(int pos) {
		Iterable<String> result = super.getCompletionProposals(pos);
		if (result != null) {
			return result;
		}
		result = this.converterContainer.getCompletionProposals(pos);
		if (result != null) {
			return result;
		}
		if (this.attributeTypeTouches(pos)) {
			return this.getCandidateAttributeTypeNames();
		}
		return null;
	}

	protected boolean attributeTypeTouches(int pos) {
		return this.xmlAttributeMapping.attributeTypeTouches(pos);
	}
	
	protected Iterable<String> getCandidateAttributeTypeNames() {
		List<String> names = new ArrayList<String>();
		names.add(int.class.getName());
		names.add(Integer.class.getSimpleName());
		names.add(short.class.getName());
		names.add(Short.class.getSimpleName());
		names.add(long.class.getName());
		names.add(Long.class.getSimpleName());
		names.add(Timestamp.class.getName());
		return names;
	}
}
