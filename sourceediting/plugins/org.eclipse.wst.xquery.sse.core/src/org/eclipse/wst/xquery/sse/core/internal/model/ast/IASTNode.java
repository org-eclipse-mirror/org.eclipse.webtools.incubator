/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xquery.sse.core.internal.model.ast;

import java.util.List;

/**
 * Represent an XQuery AST node.
 * 
 * @author <a href="villard@us.ibm.com">Lionel Villard</a>
 */
public interface IASTNode {

	// AST node types

	public static final int MODULE = 1;
	public static final int OPERATOR = 2;
	public static final int LITERAL = 3;
	public static final int VARREF = 4;
	public static final int FLWOR = 5;
	public static final int SEQUENCETYPE = 6;
	public static final int FUNCTIONCALL = 7;
	public static final int FUNCTIONDECL = 8;
	public static final int DIRELEMENT = 9;
	public static final int DIRATTRIBUTE = 10;
	public static final int VARDECL = 11;
	public static final int IF = 12;
	public static final int TYPESWITCH = 13;
	public static final int QUANTIFIED = 14;

	/** Gets the AST node type */
	public int getType();

	/**
	 * Gets parent AST node
	 * 
	 * @return the parent AST node or null if this is the root node.
	 */
	public IASTNode getASTParent();

	/**
	 * @param index
	 * @param newChild
	 */
	public void setChildASTNodeAt(int index, IASTNode newChild);

	/**
	 * @param i
	 * @return
	 */
	public IASTNode getChildASTNodeAt(int i);

	/**
	 * @param index
	 */
	public void removeChildASTNodesAfter(int index);

	/** Gets number of child nodes */
	public int getChildASTNodesCount();

	/**
	 * Set the parent node
	 */
	public void setASTParent(IASTNode parent);
}
