/**********************************************************************
 * Copyright (c) 2009 Martin Schmied and others. All rights reserved.   This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Martin Schmied - Initial API and implementation
 **********************************************************************/
package org.eclipse.wst.rng.contentassist;

import java.io.IOException;

import org.w3c.dom.Document;

public interface IRngSchemaBinder {
	void bind(Document document, IRngSchema schema) throws IOException, InvalidRelaxNgSchemaException;
	
	void unBind(Document document);
	
	boolean hasBoundSchema(Document document);
}
