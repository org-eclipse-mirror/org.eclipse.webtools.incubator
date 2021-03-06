/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.nosql.eclipselink.core.internal.context.java;

import org.eclipse.jpt.common.core.resource.java.JavaResourceType;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.utility.internal.StringTools;
import org.eclipse.jpt.common.utility.internal.iterable.TransformationIterable;
import org.eclipse.jpt.jpa.core.internal.context.java.AbstractJavaNamedColumn;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.java.AbstractJavaNamedDiscriminatorColumn;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkTenantDiscriminatorColumnAnnotation2_3;
import org.eclipse.jpt.nosql.eclipselink.core.context.ReadOnlyTenantDiscriminatorColumn2_3;
import org.eclipse.jpt.nosql.eclipselink.core.context.java.JavaTenantDiscriminatorColumn2_3;

/**
 * Java tenant discriminator column
 */
public class EclipseLinkJavaTenantDiscriminatorColumn2_3
	extends AbstractJavaNamedDiscriminatorColumn<EclipseLinkTenantDiscriminatorColumnAnnotation2_3, ReadOnlyTenantDiscriminatorColumn2_3.Owner>
	implements JavaTenantDiscriminatorColumn2_3
{
	/** @see AbstractJavaNamedColumn#AbstractJavaNamedColumn(org.eclipse.jpt.jpa.core.context.JpaContextNode, org.eclipse.jpt.jpa.core.context.ReadOnlyNamedColumn.Owner, org.eclipse.jpt.jpa.core.resource.java.NamedColumnAnnotation) */
	protected /* final */ EclipseLinkTenantDiscriminatorColumnAnnotation2_3 columnAnnotation;  // never null

	protected String specifiedTableName;
	protected String defaultTableName;

	protected String specifiedContextProperty;
	protected String defaultContextProperty;

	protected Boolean specifiedPrimaryKey;
	protected boolean defaultPrimaryKey = DEFAULT_PRIMARY_KEY;

	public EclipseLinkJavaTenantDiscriminatorColumn2_3(JavaEclipseLinkMultitenancyImpl2_3 parent, ReadOnlyTenantDiscriminatorColumn2_3.Owner owner, EclipseLinkTenantDiscriminatorColumnAnnotation2_3 columnAnnotation) {
		super(parent, owner, columnAnnotation);
		this.specifiedTableName = this.buildSpecifiedTableName();
		this.specifiedContextProperty = this.buildSpecifiedContextProperty();
		this.specifiedPrimaryKey = this.buildSpecifiedPrimaryKey();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.setSpecifiedTableName_(this.buildSpecifiedTableName());
		this.setSpecifiedContextProperty_(this.buildSpecifiedContextProperty());
		this.setSpecifiedPrimaryKey_(this.buildSpecifiedPrimaryKey());
	}

	@Override
	public void update() {
		super.update();
		this.setDefaultTableName(this.buildDefaultTableName());
		this.setDefaultContextProperty(this.buildDefaultContextProperty());
		this.setDefaultPrimaryKey(this.buildDefaultPrimaryKey());
	}

	// ********** column annotation **********

	@Override
	public EclipseLinkTenantDiscriminatorColumnAnnotation2_3 getColumnAnnotation() {
		return this.columnAnnotation;
	}

	@Override
	protected void setColumnAnnotation(EclipseLinkTenantDiscriminatorColumnAnnotation2_3 columnAnnotation) {
		this.columnAnnotation = columnAnnotation;
	}

	@Override
	protected void removeColumnAnnotation() {
		// we don't remove a tenant discriminator column annotation when it is empty
	}


	// ********** table name **********

	@Override
	public String getTableName() {
		return (this.specifiedTableName != null) ? this.specifiedTableName : this.defaultTableName;
	}

	public String getSpecifiedTableName() {
		return this.specifiedTableName;
	}

	public void setSpecifiedTableName(String tableName) {
		if (this.valuesAreDifferent(this.specifiedTableName, tableName)) {
			this.getColumnAnnotation().setTable(tableName);
			this.removeColumnAnnotationIfUnset();
			this.setSpecifiedTableName_(tableName);
		}
	}

	protected void setSpecifiedTableName_(String tableName) {
		String old = this.specifiedTableName;
		this.specifiedTableName = tableName;
		this.firePropertyChanged(SPECIFIED_TABLE_NAME_PROPERTY, old, tableName);
	}

	protected String buildSpecifiedTableName() {
		return this.getColumnAnnotation().getTable();
	}

	public String getDefaultTableName() {
		return this.defaultTableName;
	}

	protected void setDefaultTableName(String tableName) {
		String old = this.defaultTableName;
		this.defaultTableName = tableName;
		this.firePropertyChanged(DEFAULT_TABLE_NAME_PROPERTY, old, tableName);
	}

	protected String buildDefaultTableName() {
		return this.owner.getDefaultTableName();
	}

	public TextRange getTableNameTextRange() {
		return this.getValidationTextRange(this.getColumnAnnotation().getTableTextRange());
	}

	public TextRange getTableNameValidationTextRange() {
		return this.getValidationTextRange(this.getColumnAnnotation().getTableTextRange());
	}

	// ********** contextProperty **********

	public String getContextProperty() {
		return (this.specifiedContextProperty != null) ? this.specifiedContextProperty : this.defaultContextProperty;
	}

	public String getSpecifiedContextProperty() {
		return this.specifiedContextProperty;
	}

	public void setSpecifiedContextProperty(String contextProperty) {
		if (this.valuesAreDifferent(this.specifiedContextProperty, contextProperty)) {
			this.getColumnAnnotation().setContextProperty(contextProperty);
			this.removeColumnAnnotationIfUnset();
			this.setSpecifiedContextProperty_(contextProperty);
		}
	}

	protected void setSpecifiedContextProperty_(String contextProperty) {
		String old = this.specifiedContextProperty;
		this.specifiedContextProperty = contextProperty;
		this.firePropertyChanged(SPECIFIED_CONTEXT_PROPERTY_PROPERTY, old, contextProperty);
	}

	protected String buildSpecifiedContextProperty() {
		return this.getColumnAnnotation().getContextProperty();
	}

	public String getDefaultContextProperty() {
		return this.defaultContextProperty;
	}

	protected void setDefaultContextProperty(String contextProperty) {
		String old = this.defaultContextProperty;
		this.defaultContextProperty = contextProperty;
		this.firePropertyChanged(DEFAULT_CONTEXT_PROPERTY_PROPERTY, old, contextProperty);
	}

	protected String buildDefaultContextProperty() {
		return this.owner.getDefaultContextPropertyName();
	}

	public TextRange getContextPropertyTextRange() {
		return this.getValidationTextRange(this.getColumnAnnotation().getContextPropertyTextRange());
	}


	// ********** primaryKey **********

	public boolean isPrimaryKey() {
		return (this.specifiedPrimaryKey != null) ? this.specifiedPrimaryKey.booleanValue() : this.isDefaultPrimaryKey();
	}

	public Boolean getSpecifiedPrimaryKey() {
		return this.specifiedPrimaryKey;
	}

	public void setSpecifiedPrimaryKey(Boolean primaryKey) {
		if (this.valuesAreDifferent(this.specifiedPrimaryKey, primaryKey)) {
			this.getColumnAnnotation().setPrimaryKey(primaryKey);
			this.removeColumnAnnotationIfUnset();
			this.setSpecifiedPrimaryKey_(primaryKey);
		}
	}

	protected void setSpecifiedPrimaryKey_(Boolean primaryKey) {
		Boolean old = this.specifiedPrimaryKey;
		this.specifiedPrimaryKey = primaryKey;
		this.firePropertyChanged(SPECIFIED_PRIMARY_KEY_PROPERTY, old, primaryKey);
	}

	protected Boolean buildSpecifiedPrimaryKey() {
		return this.getColumnAnnotation().getPrimaryKey();
	}

	public boolean isDefaultPrimaryKey() {
		return this.defaultPrimaryKey;
	}

	protected void setDefaultPrimaryKey(boolean primaryKey) {
		boolean old = this.defaultPrimaryKey;
		this.defaultPrimaryKey = primaryKey;
		this.firePropertyChanged(DEFAULT_PRIMARY_KEY_PROPERTY, old, primaryKey);
	}

	protected boolean buildDefaultPrimaryKey() {
		return DEFAULT_PRIMARY_KEY;
	}


	// ********** misc **********

	@Override
	public JavaEclipseLinkMultitenancyImpl2_3 getParent() {
		return (JavaEclipseLinkMultitenancyImpl2_3) super.getParent();
	}

	protected JavaResourceType getJavaResourceType() {
		return this.getParent().getJavaResourceType();
	}


	// ********** Java completion proposals **********

	@Override
	public Iterable<String> getCompletionProposals(int pos) {
		Iterable<String> result = super.getCompletionProposals(pos);
		if (result != null) {
			return result;
		}
		if (this.tableTouches(pos)) {
			return this.getJavaCandidateTableNames();
		}
		return null;
	}

	protected boolean tableTouches(int pos) {
		return this.getColumnAnnotation().tableTouches(pos);
	}

	protected Iterable<String> getJavaCandidateTableNames() {
		return new TransformationIterable<String, String>(this.getCandidateTableNames(),
				StringTools.JAVA_STRING_LITERAL_CONTENT_TRANSFORMER);
	}

	public Iterable<String> getCandidateTableNames() {
		return this.owner.getCandidateTableNames();
	}


	// ********** validation **********

	public boolean tableNameIsInvalid() {
		return this.owner.tableNameIsInvalid(this.getTableName());
	}
}
