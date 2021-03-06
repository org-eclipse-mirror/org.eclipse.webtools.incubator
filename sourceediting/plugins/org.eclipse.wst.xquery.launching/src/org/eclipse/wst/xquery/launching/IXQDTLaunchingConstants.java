/*******************************************************************************
 * Copyright (c) 2008, 2009 28msec Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Gabriel Petrovay (28msec) - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xquery.launching;

public interface IXQDTLaunchingConstants {

    public static final String BUILTIN_MODULES_EXTENSION_ID = "builtinModules";
    public static final String BUILTIN_MODULES_SEARCH_PATH_ELEMENT = "searchPath";
    public static final String BUILTIN_MODULES_INTERPRETER_INSTALL_TYPE_ID_ATTRIBUTE = "installType";
    public static final String BUILTIN_MODULES_RELATIVE_ATTRIBUTE = "relative";
    public static final String BUILTIN_MODULES_PATH_ATTRIBUTE = "path";
    public static final String BUILTIN_MODULES_DOC_PROVIDER_ATTRIBUTE = "docProvider";

    public static final String IMPLICIT_IMPORTS_EXTENSION_ID = "implicitImports";
    public static final String IMPLICIT_IMPORTS_PREFIXES_ELEMENT = "prefixes";
    public static final String IMPLICIT_IMPORTS_PREFIX_ELEMENT = "prefix";
    public static final String IMPLICIT_IMPORTS_ACTIVATOR_ATTRIBUTE = "activator";
    public static final String IMPLICIT_IMPORTS_NAME_ATTRIBUTE = "name";

}
