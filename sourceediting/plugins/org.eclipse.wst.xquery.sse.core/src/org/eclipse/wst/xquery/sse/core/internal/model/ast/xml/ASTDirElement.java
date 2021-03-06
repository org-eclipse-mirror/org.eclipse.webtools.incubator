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
package org.eclipse.wst.xquery.sse.core.internal.model.ast.xml;

import org.eclipse.wst.xml.core.internal.document.ElementImpl;
import org.eclipse.wst.xml.core.internal.document.TextImpl;
import org.eclipse.wst.xquery.sse.core.internal.model.ast.IASTNode;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Direct element constructor
 * 
 * 
 * @author <a href="villard@us.ibm.com">Lionel Villard</a>
 */
public class ASTDirElement extends ElementImpl implements IASTNode {

	// State

	/** AST parent node (which could be a non-xml parent) */
	protected IASTNode parentAST;

	// Constructors

	public ASTDirElement() {
	}

	/**
	 * @param astDirDocument
	 * @param tagName
	 */
	public ASTDirElement(ASTDirDocument owner, String tagName) {
		setOwnerDocument(owner);
		setTagName(tagName);
	}

	// Overrides
	

	@Override
	public Attr setAttributeNode(Attr newAttr) throws DOMException { 
		if (newAttr instanceof ASTDirAttribute) 
			((ASTDirAttribute) newAttr).setASTParent(this); 
		
		return super.setAttributeNode(newAttr);
	}

	@Override
	public void setTagName(String tagName) {
		super.setTagName(tagName);
	}

	// Implements IASTNode


	public IASTNode getChildASTNodeAt(int i) {
		if (i < getChildNodes().getLength()) {
			final Node node = getChildNodes().item(i);
			if (node instanceof IASTNode)
				return (IASTNode) node;
			
			// Wrapper
			return ((ASTNodeText) node).node;
		}
		return null;
	}

	public int getChildASTNodesCount() {
		return getChildNodes().getLength();
	}

	public IASTNode getASTParent() {
		return parentAST;
	}

	public void setASTParent(IASTNode parent) {
		parentAST = parent;
		if (parent instanceof Node)
			setParentNode((Node) parent);
	}

	public int getType() {
		return DIRELEMENT;
	}

	public void removeChildASTNodesAfter(int index) {
		NodeList children = getChildNodes();
		if (getChildNodes().getLength() > index) {
			while (getChildNodes().getLength() != index)
				removeChild(getLastChild());
		}
	}

	public void setChildASTNodeAt(int index, IASTNode newChild) {
		Node node;
		if (!(newChild instanceof Node)) {
			// This is an XQuery expression=> wrap around a text node
			node = new ASTNodeText(newChild);
		} else
			node = (Node) newChild;

		if (getChildNodes().getLength() >= index)
			appendChild(node);
		else

		{
			Node oldChild = getChildNodes().item(index);
			replaceChild(node, oldChild);
		}
		
		newChild.setASTParent(this);
	}

	// Inner class

	class ASTNodeText extends TextImpl {
		// State

		/** Wrapped AST node */
		protected IASTNode node;

		// Constructor

		protected ASTNodeText(IASTNode node) {
			this.node = node;
		}
	}

}
