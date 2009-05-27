/*******************************************************************************
 * Copyright (c) 2009 Shane Clarke.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Shane Clarke - initial API and implementation
 *******************************************************************************/
package org.eclipse.jst.ws.jaxws.core.annotation.validation.tests;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jst.ws.annotations.core.AnnotationsCore;
import org.eclipse.jst.ws.annotations.core.utils.AnnotationUtils;

/**
 * 
 * @author sclarke
 *
 */
public abstract class AbstractOnewayValidationTest extends AbstractAnnotationValidationTest {

    @Override
    public Annotation getAnnotation() {
        return AnnotationsCore.createAnnotation(ast, javax.jws.Oneway.class, 
              javax.jws.Oneway.class.getSimpleName(), null);
    }

    @Override
    public String getClassName() {
        return "MyClass.java";
    }

    @Override
    public String getPackageName() {
        return "com.example";
    }
    
    public abstract String getErrorMessage();
    
    public abstract IMethod getMethodToTest();
    
    public void testOnewayRule() {
        try {
            assertNotNull(annotation);
            assertEquals("Oneway", AnnotationUtils.getAnnotationName(annotation));

            IMethod method = getMethodToTest();
            assertNotNull(method);

            AnnotationUtils.getImportChange(compilationUnit, javax.jws.Oneway.class, textFileChange, true);

            AnnotationUtils.createMethodAnnotationChange(source, compilationUnit, rewriter, method,
                    annotation, textFileChange);

            assertTrue(executeChange(new NullProgressMonitor(), textFileChange));

            assertTrue(AnnotationUtils.isAnnotationPresent(method, AnnotationUtils
                    .getAnnotationName(annotation)));
            
            Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, null);

            IMarker[] allmarkers = source.getResource().findMarkers(IMarker.PROBLEM, true,
                    IResource.DEPTH_INFINITE);

            assertEquals(1, allmarkers.length);

            IMarker annotationProblemMarker = allmarkers[0];

            assertEquals(source.getResource(), annotationProblemMarker.getResource());
            assertEquals(getErrorMessage(), annotationProblemMarker.getAttribute(IMarker.MESSAGE));
        } catch (CoreException ce) {
            fail(ce.getLocalizedMessage());
        } catch (OperationCanceledException oce) {
            fail(oce.getLocalizedMessage());
        } catch (InterruptedException ie) {
            fail(ie.getLocalizedMessage());
        }
    }

}