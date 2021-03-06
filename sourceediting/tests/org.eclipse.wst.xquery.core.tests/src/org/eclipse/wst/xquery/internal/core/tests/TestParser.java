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
package org.eclipse.wst.xquery.internal.core.tests;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.RecognitionException;
import org.eclipse.wst.xquery.core.IXQDTLanguageConstants;
import org.eclipse.wst.xquery.internal.core.parser.antlr.NewLazyTokenStream;
import org.eclipse.wst.xquery.internal.core.parser.antlr.XQDTCommonTreeAdaptor;
import org.eclipse.wst.xquery.internal.core.parser.antlr.XQueryLexer;
import org.eclipse.wst.xquery.internal.core.parser.antlr.XQueryParser;
import org.junit.Test;

@SuppressWarnings("restriction")
public class TestParser implements IXQDTLanguageConstants {

    // *****************************************************
    // ****************** TESTS ****************************
    // *****************************************************

    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=297366
    @Test
    public void testBug297366_1() {
        testQuery("declare private variable $bar := ''; ()", false, LANGUAGE_XQUERY);
    }

    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=297366
    @Test
    public void testBug297366_2() {
        testQuery("declare private variable $bar := ''; ()", true, LANGUAGE_XQUERY_MARK_LOGIC);
    }

    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=297366
    @Test
    public void testBug297366_3() {
        testQuery("declare private function foo() as xs:string { 'bar' }; ()", false, LANGUAGE_XQUERY);
    }

    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=297366
    @Test
    public void testBug297366_4() {
        testQuery("declare private function foo() as xs:string { 'bar' }; ()", true, LANGUAGE_XQUERY_MARK_LOGIC);
    }

    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=292340
    @Test
    public void testBug292340_1() {
        testQuery("binary { 'deadbeef' }", false, LANGUAGE_XQUERY);
    }

    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=292340
    @Test
    public void testBug292340_2() {
        testQuery("binary { 'deadbeef' }", true, LANGUAGE_XQUERY_MARK_LOGIC);
    }

    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=296671 
    @Test
    public void testBug296671() {
        testQuery("\"<\"", true, LANGUAGE_XQUERY);
    }

    // https://bugs.eclipse.org/bugs/show_bug.cgi?id=295437
    @Test
    public void testBug295437() {
        testQuery("<e>{.}--</e>", true, LANGUAGE_XQUERY);
    }

    // *****************************************************
    // *****************************************************

    private void testQuery(String query, boolean valid, int languageLevel) {
        XQueryParser parser = prepareParser("parser_unit_tests.xq", query.toCharArray());
        parser.setLanguageLevel(languageLevel);
        try {
            parser.p_Module();
            assert valid == (parser.getNumberOfSyntaxErrors() == 0);
        } catch (RecognitionException e) {
            // test for RecognitionException 
            assert false;
        }
    }

    private XQueryParser prepareParser(String fileName, char[] source) {
        ANTLRStringStream inputStream = new ANTLRStringStream(source, source.length);
        XQueryLexer lexer = new XQueryLexer(inputStream);
        NewLazyTokenStream tokenStream = new NewLazyTokenStream(lexer);
        tokenStream.jumpToFirstValidToken();
        XQueryParser parser = new XQueryParser(tokenStream);
        parser.setCharSource(inputStream);
        parser.setTreeAdaptor(new XQDTCommonTreeAdaptor(null));
        parser.setFileName(fileName);
        return parser;
    }

}
