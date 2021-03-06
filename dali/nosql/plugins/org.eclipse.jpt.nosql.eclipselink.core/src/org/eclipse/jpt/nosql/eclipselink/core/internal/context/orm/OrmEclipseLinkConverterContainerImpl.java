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
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.common.utility.iterable.ListIterable;
import org.eclipse.jpt.jpa.core.context.JpaContextNode;
import org.eclipse.jpt.jpa.core.context.TypeRefactoringParticipant;
import org.eclipse.jpt.jpa.core.internal.context.orm.AbstractOrmXmlContextNode;
import org.eclipse.jpt.jpa.core.resource.orm.v2_1.XmlConverter_2_1;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.EclipseLinkOrmFactory;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlConverter;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlConverterContainer;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlObjectTypeConverter;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlStructConverter;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlTypeConverter;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkConverter;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkCustomConverter;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkObjectTypeConverter;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkStructConverter;
import org.eclipse.jpt.nosql.eclipselink.core.context.EclipseLinkTypeConverter;
import org.eclipse.jpt.nosql.eclipselink.core.context.orm.OrmEclipseLinkConverterContainer;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class OrmEclipseLinkConverterContainerImpl
	extends AbstractOrmXmlContextNode
	implements OrmEclipseLinkConverterContainer
{

	private final Owner owner;
	private final XmlConverterContainer xmlConverterContainer;

	protected final ContextListContainer<OrmEclipseLinkCustomConverter, XmlConverter_2_1> customConverterContainer;
	protected final ContextListContainer<OrmEclipseLinkObjectTypeConverter, XmlObjectTypeConverter> objectTypeConverterContainer;
	protected final ContextListContainer<OrmEclipseLinkStructConverter, XmlStructConverter> structConverterContainer;
	protected final ContextListContainer<OrmEclipseLinkTypeConverter, XmlTypeConverter> typeConverterContainer;


	public OrmEclipseLinkConverterContainerImpl(JpaContextNode parent, Owner owner,  XmlConverterContainer xmlConverterContainer) {
		super(parent);
		this.owner = owner;
		this.xmlConverterContainer = xmlConverterContainer;

		this.customConverterContainer = this.buildCustomConverterContainer();
		this.objectTypeConverterContainer = this.buildObjectTypeConverterContainer();
		this.structConverterContainer = this.buildStructConverterContainer();
		this.typeConverterContainer = this.buildTypeConverterContainer();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.syncCustomConverters();
		this.syncObjectTypeConverters();
		this.syncStructConverters();
		this.syncTypeConverters();
	}

	@Override
	public void update() {
		super.update();
		this.updateNodes(this.getCustomConverters());
		this.updateNodes(this.getObjectTypeConverters());
		this.updateNodes(this.getStructConverters());
		this.updateNodes(this.getTypeConverters());
	}


	// ********** custom converters **********

	public ListIterable<OrmEclipseLinkCustomConverter> getCustomConverters() {
		return this.customConverterContainer.getContextElements();
	}

	public int getCustomConvertersSize() {
		return this.customConverterContainer.getContextElementsSize();
	}

	public OrmEclipseLinkCustomConverter addCustomConverter() {
		return this.addCustomConverter(this.getCustomConvertersSize());
	}

	public OrmEclipseLinkCustomConverter addCustomConverter(int index) {
		XmlConverter xmlConverter = this.buildXmlCustomConverter();
		OrmEclipseLinkCustomConverter converter = this.customConverterContainer.addContextElement(index, xmlConverter);
		this.xmlConverterContainer.getConverters().add(index, xmlConverter);
		return converter;
	}

	protected XmlConverter buildXmlCustomConverter() {
		return EclipseLinkOrmFactory.eINSTANCE.createXmlConverter();
	}

	public void removeCustomConverter(EclipseLinkCustomConverter converter) {
		this.removeCustomConverter(this.customConverterContainer.indexOfContextElement((OrmEclipseLinkCustomConverter) converter));
	}

	public void removeCustomConverter(int index) {
		this.customConverterContainer.removeContextElement(index);
		this.xmlConverterContainer.getConverters().remove(index);
	}

	public void moveCustomConverter(int targetIndex, int sourceIndex) {
		this.customConverterContainer.moveContextElement(targetIndex, sourceIndex);
		this.xmlConverterContainer.getConverters().move(targetIndex, sourceIndex);
	}

	protected OrmEclipseLinkCustomConverter buildCustomConverter(XmlConverter xmlConverter) {
		return new OrmEclipseLinkCustomConverter(this, xmlConverter);
	}

	protected void syncCustomConverters() {
		this.customConverterContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<XmlConverter_2_1> getXmlCustomConverters() {
		// clone to reduce chance of concurrency problems
		return IterableTools.cloneLive(this.xmlConverterContainer.getConverters());
	}

	protected ContextListContainer<OrmEclipseLinkCustomConverter, XmlConverter_2_1> buildCustomConverterContainer() {
		CustomConverterContainer container = new CustomConverterContainer();
		container.initialize();
		return container;
	}

	/**
	 * custom converter container
	 */
	protected class CustomConverterContainer
		extends ContextListContainer<OrmEclipseLinkCustomConverter, XmlConverter_2_1>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return CUSTOM_CONVERTERS_LIST;
		}
		@Override
		protected OrmEclipseLinkCustomConverter buildContextElement(XmlConverter_2_1 resourceElement) {
			return OrmEclipseLinkConverterContainerImpl.this.buildCustomConverter((XmlConverter) resourceElement);
		}
		@Override
		protected ListIterable<XmlConverter_2_1> getResourceElements() {
			return OrmEclipseLinkConverterContainerImpl.this.getXmlCustomConverters();
		}
		@Override
		protected XmlConverter_2_1 getResourceElement(OrmEclipseLinkCustomConverter contextElement) {
			return contextElement.getXmlConverter();
		}
	}


	// ********** object type converters **********

	public ListIterable<OrmEclipseLinkObjectTypeConverter> getObjectTypeConverters() {
		return this.objectTypeConverterContainer.getContextElements();
	}

	public int getObjectTypeConvertersSize() {
		return this.objectTypeConverterContainer.getContextElementsSize();
	}

	public OrmEclipseLinkObjectTypeConverter addObjectTypeConverter() {
		return this.addObjectTypeConverter(this.getObjectTypeConvertersSize());
	}

	public OrmEclipseLinkObjectTypeConverter addObjectTypeConverter(int index) {
		XmlObjectTypeConverter xmlConverter = this.buildXmlObjectTypeConverter();
		OrmEclipseLinkObjectTypeConverter converter = this.objectTypeConverterContainer.addContextElement(index, xmlConverter);
		this.xmlConverterContainer.getObjectTypeConverters().add(index, xmlConverter);
		return converter;
	}

	protected XmlObjectTypeConverter buildXmlObjectTypeConverter() {
		return EclipseLinkOrmFactory.eINSTANCE.createXmlObjectTypeConverter();
	}

	public void removeObjectTypeConverter(EclipseLinkObjectTypeConverter converter) {
		this.removeObjectTypeConverter(this.objectTypeConverterContainer.indexOfContextElement((OrmEclipseLinkObjectTypeConverter) converter));
	}

	public void removeObjectTypeConverter(int index) {
		this.objectTypeConverterContainer.removeContextElement(index);
		this.xmlConverterContainer.getObjectTypeConverters().remove(index);
	}

	public void moveObjectTypeConverter(int targetIndex, int sourceIndex) {
		this.objectTypeConverterContainer.moveContextElement(targetIndex, sourceIndex);
		this.xmlConverterContainer.getObjectTypeConverters().move(targetIndex, sourceIndex);
	}

	protected OrmEclipseLinkObjectTypeConverter buildObjectTypeConverter(XmlObjectTypeConverter xmlConverter) {
		return new OrmEclipseLinkObjectTypeConverter(this, xmlConverter);
	}

	protected void syncObjectTypeConverters() {
		this.objectTypeConverterContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<XmlObjectTypeConverter> getXmlObjectTypeConverters() {
		// clone to reduce chance of concurrency problems
		return IterableTools.cloneLive(this.xmlConverterContainer.getObjectTypeConverters());
	}

	protected ContextListContainer<OrmEclipseLinkObjectTypeConverter, XmlObjectTypeConverter> buildObjectTypeConverterContainer() {
		ObjectTypeConverterContainer container = new ObjectTypeConverterContainer();
		container.initialize();
		return container;
	}

	/**
	 * object type converter container
	 */
	protected class ObjectTypeConverterContainer
		extends ContextListContainer<OrmEclipseLinkObjectTypeConverter, XmlObjectTypeConverter>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return OBJECT_TYPE_CONVERTERS_LIST;
		}
		@Override
		protected OrmEclipseLinkObjectTypeConverter buildContextElement(XmlObjectTypeConverter resourceElement) {
			return OrmEclipseLinkConverterContainerImpl.this.buildObjectTypeConverter(resourceElement);
		}
		@Override
		protected ListIterable<XmlObjectTypeConverter> getResourceElements() {
			return OrmEclipseLinkConverterContainerImpl.this.getXmlObjectTypeConverters();
		}
		@Override
		protected XmlObjectTypeConverter getResourceElement(OrmEclipseLinkObjectTypeConverter contextElement) {
			return contextElement.getXmlConverter();
		}
	}

	// ********** struct converters **********

	public ListIterable<OrmEclipseLinkStructConverter> getStructConverters() {
		return this.structConverterContainer.getContextElements();
	}

	public int getStructConvertersSize() {
		return this.structConverterContainer.getContextElementsSize();
	}

	public OrmEclipseLinkStructConverter addStructConverter() {
		return this.addStructConverter(this.getStructConvertersSize());
	}

	public OrmEclipseLinkStructConverter addStructConverter(int index) {
		XmlStructConverter xmlConverter = this.buildXmlStructConverter();
		OrmEclipseLinkStructConverter converter = this.structConverterContainer.addContextElement(index, xmlConverter);
		this.xmlConverterContainer.getStructConverters().add(index, xmlConverter);
		return converter;
	}

	protected XmlStructConverter buildXmlStructConverter() {
		return EclipseLinkOrmFactory.eINSTANCE.createXmlStructConverter();
	}

	public void removeStructConverter(EclipseLinkStructConverter converter) {
		this.removeStructConverter(this.structConverterContainer.indexOfContextElement((OrmEclipseLinkStructConverter) converter));
	}

	public void removeStructConverter(int index) {
		this.structConverterContainer.removeContextElement(index);
		this.xmlConverterContainer.getStructConverters().remove(index);
	}

	public void moveStructConverter(int targetIndex, int sourceIndex) {
		this.structConverterContainer.moveContextElement(targetIndex, sourceIndex);
		this.xmlConverterContainer.getStructConverters().move(targetIndex, sourceIndex);
	}

	protected OrmEclipseLinkStructConverter buildStructConverter(XmlStructConverter xmlConverter) {
		return new OrmEclipseLinkStructConverter(this, xmlConverter);
	}

	protected void syncStructConverters() {
		this.structConverterContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<XmlStructConverter> getXmlStructConverters() {
		// clone to reduce chance of concurrency problems
		return IterableTools.cloneLive(this.xmlConverterContainer.getStructConverters());
	}

	protected ContextListContainer<OrmEclipseLinkStructConverter, XmlStructConverter> buildStructConverterContainer() {
		StructConverterContainer container = new StructConverterContainer();
		container.initialize();
		return container;
	}

	/**
	 * struct converter container
	 */
	protected class StructConverterContainer
		extends ContextListContainer<OrmEclipseLinkStructConverter, XmlStructConverter>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return STRUCT_CONVERTERS_LIST;
		}
		@Override
		protected OrmEclipseLinkStructConverter buildContextElement(XmlStructConverter resourceElement) {
			return OrmEclipseLinkConverterContainerImpl.this.buildStructConverter(resourceElement);
		}
		@Override
		protected ListIterable<XmlStructConverter> getResourceElements() {
			return OrmEclipseLinkConverterContainerImpl.this.getXmlStructConverters();
		}
		@Override
		protected XmlStructConverter getResourceElement(OrmEclipseLinkStructConverter contextElement) {
			return contextElement.getXmlConverter();
		}
	}


	// ********** type converters **********

	public ListIterable<OrmEclipseLinkTypeConverter> getTypeConverters() {
		return this.typeConverterContainer.getContextElements();
	}

	public int getTypeConvertersSize() {
		return this.typeConverterContainer.getContextElementsSize();
	}

	public OrmEclipseLinkTypeConverter addTypeConverter() {
		return this.addTypeConverter(this.getTypeConvertersSize());
	}

	public OrmEclipseLinkTypeConverter addTypeConverter(int index) {
		XmlTypeConverter xmlConverter = this.buildXmlTypeConverter();
		OrmEclipseLinkTypeConverter converter = this.typeConverterContainer.addContextElement(index, xmlConverter);
		this.xmlConverterContainer.getTypeConverters().add(index, xmlConverter);
		return converter;
	}

	protected XmlTypeConverter buildXmlTypeConverter() {
		return EclipseLinkOrmFactory.eINSTANCE.createXmlTypeConverter();
	}

	public void removeTypeConverter(EclipseLinkTypeConverter converter) {
		this.removeTypeConverter(this.typeConverterContainer.indexOfContextElement((OrmEclipseLinkTypeConverter) converter));
	}

	public void removeTypeConverter(int index) {
		this.typeConverterContainer.removeContextElement(index);
		this.xmlConverterContainer.getTypeConverters().remove(index);
	}

	public void moveTypeConverter(int targetIndex, int sourceIndex) {
		this.typeConverterContainer.moveContextElement(targetIndex, sourceIndex);
		this.xmlConverterContainer.getTypeConverters().move(targetIndex, sourceIndex);
	}

	protected OrmEclipseLinkTypeConverter buildTypeConverter(XmlTypeConverter xmlConverter) {
		return new OrmEclipseLinkTypeConverter(this, xmlConverter);
	}

	protected void syncTypeConverters() {
		this.typeConverterContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<XmlTypeConverter> getXmlTypeConverters() {
		// clone to reduce chance of concurrency problems
		return IterableTools.cloneLive(this.xmlConverterContainer.getTypeConverters());
	}

	protected ContextListContainer<OrmEclipseLinkTypeConverter, XmlTypeConverter> buildTypeConverterContainer() {
		TypeConverterContainer container = new TypeConverterContainer();
		container.initialize();
		return container;
	}

	/**
	 * type converter container
	 */
	protected class TypeConverterContainer
		extends ContextListContainer<OrmEclipseLinkTypeConverter, XmlTypeConverter>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return TYPE_CONVERTERS_LIST;
		}
		@Override
		protected OrmEclipseLinkTypeConverter buildContextElement(XmlTypeConverter resourceElement) {
			return OrmEclipseLinkConverterContainerImpl.this.buildTypeConverter(resourceElement);
		}
		@Override
		protected ListIterable<XmlTypeConverter> getResourceElements() {
			return OrmEclipseLinkConverterContainerImpl.this.getXmlTypeConverters();
		}
		@Override
		protected XmlTypeConverter getResourceElement(OrmEclipseLinkTypeConverter contextElement) {
			return contextElement.getXmlConverter();
		}
	}


	// ********** refactoring **********

	@SuppressWarnings("unchecked")
	public Iterable<ReplaceEdit> createRenameTypeEdits(IType originalType, String newName) {
		return IterableTools.concatenate(
			this.createRenameObjectTypeConverterEdits(originalType, newName),
			this.createRenameTypeConverterEdits(originalType, newName),
			this.createRenameStructConverterEdits(originalType, newName),
			this.createRenameCustomConverterEdits(originalType, newName));
	}

	protected Iterable<ReplaceEdit> createRenameObjectTypeConverterEdits(IType originalType, String newName) {
		return IterableTools.children(getObjectTypeConverters(), new TypeRefactoringParticipant.RenameTypeEditsTransformer(originalType, newName));
	}

	protected Iterable<ReplaceEdit> createRenameTypeConverterEdits(IType originalType, String newName) {
		return IterableTools.children(getTypeConverters(), new TypeRefactoringParticipant.RenameTypeEditsTransformer(originalType, newName));
	}

	protected Iterable<ReplaceEdit> createRenameStructConverterEdits(IType originalType, String newName) {
		return IterableTools.children(getStructConverters(), new TypeRefactoringParticipant.RenameTypeEditsTransformer(originalType, newName));
	}

	protected Iterable<ReplaceEdit> createRenameCustomConverterEdits(IType originalType, String newName) {
		return IterableTools.children(getCustomConverters(), new TypeRefactoringParticipant.RenameTypeEditsTransformer(originalType, newName));
	}

	@SuppressWarnings("unchecked")
	public Iterable<ReplaceEdit> createMoveTypeEdits(IType originalType, IPackageFragment newPackage) {
		return IterableTools.concatenate(
			this.createMoveObjectTypeConverterEdits(originalType, newPackage),
			this.createMoveTypeConverterEdits(originalType, newPackage),
			this.createMoveStructConverterEdits(originalType, newPackage),
			this.createMoveCustomConverterEdits(originalType, newPackage));
	}

	protected Iterable<ReplaceEdit> createMoveObjectTypeConverterEdits(IType originalType, IPackageFragment newPackage) {
		return IterableTools.children(getObjectTypeConverters(), new TypeRefactoringParticipant.MoveTypeEditsTransformer(originalType, newPackage));
	}

	protected Iterable<ReplaceEdit> createMoveTypeConverterEdits(IType originalType, IPackageFragment newPackage) {
		return IterableTools.children(getTypeConverters(), new TypeRefactoringParticipant.MoveTypeEditsTransformer(originalType, newPackage));
	}

	protected Iterable<ReplaceEdit> createMoveStructConverterEdits(IType originalType, IPackageFragment newPackage) {
		return IterableTools.children(getStructConverters(), new TypeRefactoringParticipant.MoveTypeEditsTransformer(originalType, newPackage));
	}

	protected Iterable<ReplaceEdit> createMoveCustomConverterEdits(IType originalType, IPackageFragment newPackage) {
		return IterableTools.children(getCustomConverters(), new TypeRefactoringParticipant.MoveTypeEditsTransformer(originalType, newPackage));
	}


	@SuppressWarnings("unchecked")
	public Iterable<ReplaceEdit> createRenamePackageEdits(IPackageFragment originalPackage, String newName) {
		return IterableTools.concatenate(
			this.createObjectTypeConverterRenamePackageEdits(originalPackage, newName),
			this.createTypeConverterRenamePackageEdits(originalPackage, newName),
			this.createStructConverterRenamePackageEdits(originalPackage, newName),
			this.createCustomConverterRenamePackageEdits(originalPackage, newName));
	}

	protected Iterable<ReplaceEdit> createObjectTypeConverterRenamePackageEdits(IPackageFragment originalPackage, String newName) {
		return IterableTools.children(getObjectTypeConverters(), new TypeRefactoringParticipant.RenamePackageEditsTransformer(originalPackage, newName));
	}

	protected Iterable<ReplaceEdit> createTypeConverterRenamePackageEdits(IPackageFragment originalPackage, String newName) {
		return IterableTools.children(getTypeConverters(), new TypeRefactoringParticipant.RenamePackageEditsTransformer(originalPackage, newName));
	}

	protected Iterable<ReplaceEdit> createStructConverterRenamePackageEdits(IPackageFragment originalPackage, String newName) {
		return IterableTools.children(getStructConverters(), new TypeRefactoringParticipant.RenamePackageEditsTransformer(originalPackage, newName));
	}

	protected Iterable<ReplaceEdit> createCustomConverterRenamePackageEdits(IPackageFragment originalPackage, String newName) {
		return IterableTools.children(getCustomConverters(), new TypeRefactoringParticipant.RenamePackageEditsTransformer(originalPackage, newName));
	}


	// ********** validation **********

	/**
	 * The converters are validated in the persistence unit.
	 * @see org.eclipse.jpt.nosql.eclipselink.core.context.persistence.EclipseLinkPersistenceUnit#validateConverters(List, IReporter)
	 */
	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		// converters are validated in the persistence unit
	}
	
	public TextRange getValidationTextRange() {
		TextRange textRange = this.xmlConverterContainer.getValidationTextRange();
		return (textRange != null) ? textRange : this.getParent().getValidationTextRange();
	}


	// ********** misc **********

	@SuppressWarnings("unchecked")
	public Iterable<EclipseLinkConverter> getConverters() {
		return IterableTools.<EclipseLinkConverter>concatenate(
					this.getCustomConverters(),
					this.getObjectTypeConverters(),
					this.getStructConverters(),
					this.getTypeConverters()
				);
	}

	public int getConvertersSize() {
		return this.getCustomConvertersSize()
			+ this.getObjectTypeConvertersSize()
			+ this.getStructConvertersSize()
			+ this.getTypeConvertersSize();
	}

	public int getNumberSupportedConverters() {
		return this.owner.getNumberSupportedConverters();
	}

	// ********** completion proposals **********

	@Override
	public Iterable<String> getCompletionProposals(int pos) {
		Iterable<String> result = super.getCompletionProposals(pos);
		if (result != null) {
			return result;
		}
		for (OrmEclipseLinkCustomConverter converter : this.customConverterContainer.getContextElements()) {
			result = converter.getCompletionProposals(pos);
			if (result != null) {
				return result;
			}
		}
		for (OrmEclipseLinkStructConverter converter : this.structConverterContainer.getContextElements()) {
			result = converter.getCompletionProposals(pos);
			if (result != null) {
				return result;
			}
		}
		for (OrmEclipseLinkObjectTypeConverter converter : this.objectTypeConverterContainer.getContextElements()) {
			result = converter.getCompletionProposals(pos);
			if (result != null) {
				return result;
			}
		}
		for (OrmEclipseLinkTypeConverter converter : this.typeConverterContainer.getContextElements()) {
			result = converter.getCompletionProposals(pos);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
}
