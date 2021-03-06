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
/**
 * 
 */
package org.eclipse.wst.xquery.core.codeassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.wst.xquery.core.IXQDTLanguageConstants;
import org.eclipse.wst.xquery.core.utils.LanguageUtil;

public class XQDTKeywords {

    /**
     * Keywords present in the XQuery 1.1 specification
     */
    public static String[] KEYWORDS_XQUERY_11 = { "ancestor", "ancestor-or-self", "and", "as", "ascending", "at",
            "attribute", "base-uri", "boundary-space", "by", "case", "cast", "castable", "child", "collation",
            "comment", "construction", "copy-namespaces", "declare", "default", "descendant", "descendant-or-self",
            "descending", "div", "document", "document-node", "element", "else", "empty", "empty-sequence", "encoding",
            "eq", "every", "except", "external", "following", "following-sibling", "for", "function", "ge", "greatest",
            "gt", "idiv", "if", "import", "in", "inherit", "instance", "intersect", "is", "item", "lax", "le", "least",
            "let", "lt", "mod", "module", "namespace", "ne", "node", "no-inherit", "no-preserve", "of", "option", "or",
            "order", "ordered", "ordering", "parent", "preceding", "preceding-sibling", "preserve",
            "processing-instruction", "return", "satisfies", "schema", "schema-attribute", "schema-element", "self",
            "some", "stable", "strict", "strip", "text", "then", "to", "treat", "typeswitch", "union", "unordered",
            "validate", "variable", "version",
            "where",
            "xquery",
            // XQuery 1.1 keywords
            "catch", "context", "count", "decimal-format", "decimal-separator", "digit", "end", "group",
            "grouping-separator", "infinity", "minus-sign", "namespace-node", "NaN", "next", "only", "outer",
            "pattern-separator", "percent", "per-mille", "previous", "sliding", "start", "try", "tumbling", "when",
            "window", "zero-digit" };

    /**
     * Keywords added by the XQuery Update 1.0 specification (not including the already existing
     * ones in XQuery 1.0)
     */
    public static String[] KEYWORDS_XQUERY_UPDATE = { "after", "before", "copy", "delete", "first", "insert", "into",
            "last", "modify", "nodes", "rename", "replace", "revalidation", "update", "updating", "value", "with" };

    /**
     * Keywords added by the XQuery Full Text 1.0 specification (not including the already existing
     * ones in XQuery 1.0)
     */
    public static String[] FULLTEXT_KEYWORDS = { "all", "any", "content", "diacritics", "different", "distance", "end",
            "entire", "exactly", "from", "ft-option", "ftand", "ftcontains", "ftnot", "ftor", "insensitive",
            "language", "least", "levels", "lowercase", "most", "not", "occurs", "paragraph", "paragraphs", "phrase",
            "relationship", "same", "score", "sensitive", "sentence", "sentences", "start", "stemming", "stop",
            "thesaurus", "times", "uppercase", "weight", "wildcards", "window", "with", "without", "word", "words" };

    /**
     * Keywords added by the XQuery Scripting 1.0 specification (not including the already existing
     * ones in XQuery 1.0 or XQuery Update 1.0)
     */
    public static String[] KEYWORDS_XQUERY_SCRIPTING = { "block", "constant", "exit", "returning", "sequential", "set",
            "simple", "while" };

    /**
     * Keywords added by the Xorba XQuery DDL extensions (not including the already existing ones in
     * XQuery 1.0, XQuery Update 1.0, XQuery Scripting 1.0)
     */
    public static String[] KEYWORDS_ZORBA = { "append_only", "automatically", "check", "collection", "constraint",
            "const", "equality", "eval", "foreach", "foreign", "from", "index", "integrity", "key", "maintained",
            "manually", "mutable", "non", "on", "queue", "range", "read-only", "unique", "using" };

    /**
     * Keywords added by the MarkLogic XQuery extensions
     */
    public static String[] KEYWORDS_MARKLOGIC = { "binary", "private" };

    /**
     * XQuery 1.0 item types
     */
    public static final String[] XQUERY_KEYWORDS_ITEM_TYPES = { "attribute", "comment", "document-node", "element",
            "empty-sequence", "item", "node", "processing-instruction", "schema-attribute", "schema-element", "text" };

    /**
     * Reserved Function Names http://www.w3.org/TR/xquery/#id-reserved-fn-names
     */
    public static final String[] XQUERY_KEYWORDS_RESERVED_FN_NAMES = { "attribute", "comment", "document-node",
            "element", "empty-sequence", "if", "item", "namespace-node", "node", "processing-instruction",
            "schema-attribute", "schema-element", "text", "typeswitch" };

    /**
     * Entity reference names
     */
    public static String[] EMTITY_REFERECE_NAMES = { "amp", "apos", "lt", "gt", "quot" };

    /**
     * Entity reference character
     */
    public static String[] EMTITY_REFERECE_CHAR = { "&", "'", "<", ">", "\"" };

    public static String[] findByPrefix(String prefix, int languageLevel) {
        List<String> result = new ArrayList<String>();
        if (LanguageUtil.isLanguage(languageLevel, IXQDTLanguageConstants.LANGUAGE_XQUERY)) {
            for (int i = 0; i < KEYWORDS_XQUERY_11.length; i++) {
                if (KEYWORDS_XQUERY_11[i].startsWith(prefix)) {
                    result.add(KEYWORDS_XQUERY_11[i]);
                }
            }
            if (LanguageUtil.isLanguage(languageLevel, IXQDTLanguageConstants.LANGUAGE_XQUERY_UPDATE)) {
                for (int i = 0; i < KEYWORDS_XQUERY_UPDATE.length; i++) {
                    if (KEYWORDS_XQUERY_UPDATE[i].startsWith(prefix)) {
                        result.add(KEYWORDS_XQUERY_UPDATE[i]);
                    }
                }
                if (LanguageUtil.isLanguage(languageLevel, IXQDTLanguageConstants.LANGUAGE_XQUERY_SCRIPTING)) {
                    for (int i = 0; i < KEYWORDS_XQUERY_SCRIPTING.length; i++) {
                        if (KEYWORDS_XQUERY_SCRIPTING[i].startsWith(prefix)) {
                            result.add(KEYWORDS_XQUERY_SCRIPTING[i]);
                        }
                    }
                }
            }
        }
        if (LanguageUtil.isLanguage(languageLevel, IXQDTLanguageConstants.LANGUAGE_XQUERY_ZORBA)) {
            for (int i = 0; i < KEYWORDS_ZORBA.length; i++) {
                if (KEYWORDS_ZORBA[i].startsWith(prefix)) {
                    result.add(KEYWORDS_ZORBA[i]);
                }
            }
        }

        return result.toArray(new String[result.size()]);
    }
}
