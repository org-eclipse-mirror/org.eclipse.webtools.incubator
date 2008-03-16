package org.eclipse.jst.jsf.facelet.core.internal.cm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.jst.jsf.common.internal.managedobject.IManagedObject;
import org.eclipse.jst.jsf.common.runtime.internal.view.model.common.ITagElement;
import org.eclipse.jst.jsf.common.runtime.internal.view.model.common.Namespace;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.CMDocumentFactoryTLD;
import org.eclipse.jst.jsp.core.internal.contentmodel.tld.provisional.TLDDocument;
import org.eclipse.jst.jsp.core.taglib.ITaglibRecord;
import org.eclipse.jst.jsp.core.taglib.TaglibIndex;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNamedNodeMap;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNamespace;
import org.eclipse.wst.xml.core.internal.contentmodel.CMNode;

/*package*/class NamespaceCMAdapter implements CMNamedNodeMap, CMDocument, IManagedObject
{
    private final Namespace                     _ns;
    private final Map<String, ElementCMAdapter> _elements;
    private final IProject                      _project;
    private ExternalTagInfo                     _tldTagInfo;

    public NamespaceCMAdapter(final Namespace ns, final IProject project)
    {
        _ns = ns;
        _elements = new HashMap<String, ElementCMAdapter>();
        _project = project;
    }

    public int getLength()
    {
        if (_ns.hasViewElements())
        {
            return _ns.getViewElements().size();
        }
        return 0;
    }

    public CMNode getNamedItem(final String name)
    {
        String localname = name;

        if (name != null && name.indexOf(':') > -1)
        {
            String[]  splitName = name.split(":");
            
            if (splitName.length == 2)
            {
                localname = splitName[1];
            }
        }
        ElementCMAdapter element = _elements.get(localname);

        if (element == null)
        {
            final ITagElement tagElement = _ns.getViewElement(localname);
            if (tagElement != null)
            {
                ExternalTagInfo tagInfo = getOrCreateExtraTagInfo();
                element = new ElementCMAdapter(tagElement, tagInfo);
                _elements.put(localname, element);
            }
        }
        return element; 
    }

    private ExternalTagInfo getOrCreateExtraTagInfo()
    {
        if (_tldTagInfo == null)
        {
            _tldTagInfo = createExternalTagInfo();
        }
        return _tldTagInfo;
    }

    /**
     * @return a new external tag info for this namespace
     */
    protected ExternalTagInfo createExternalTagInfo()
    {
        ExternalTagInfo tldTagInfo = new MetadataTagInfo(_ns.getNSUri());
        final ITaglibRecord[] tldrecs = TaglibIndex
                .getAvailableTaglibRecords(_project.getFullPath());
        FIND_TLDRECORD: for (final ITaglibRecord rec : tldrecs)
        {
            final String matchUri = rec.getDescriptor().getURI();
            if (_ns.getNSUri().equals(matchUri))
            {
                final CMDocumentFactoryTLD factory = new CMDocumentFactoryTLD();
                tldTagInfo  = new MetadataTagInfo((TLDDocument) factory.createCMDocument(rec));
                break FIND_TLDRECORD;
            }
        }
        return tldTagInfo;
    }

    // TODO: optimize
    public CMNode item(int index)
    {
        if (_ns.hasViewElements() && index >= 0
                && index < _ns.getViewElements().size())
        {
            final Iterator<?> it = iterator();
            for (int i = 0; it.hasNext(); i++)
            {
                final ITagElement tagElement = (ITagElement) it.next(); 
                if (i == index)
                {
                    ElementCMAdapter element = _elements.get(tagElement.getName());
                    
                    if (element == null)
                    {
                        element = new ElementCMAdapter(tagElement, getOrCreateExtraTagInfo());
                        _elements.put(tagElement.getName(), element);
                        return element;
                    }
                }
            }
        }
        return null;
    }

    public Iterator<?> iterator()
    {
        return new WrappingIterator(_ns.getViewElements());
    }

    private class WrappingIterator implements Iterator<CMNode>
    {
        @SuppressWarnings("unchecked")
        final Iterator  _viewElementIterator;
        @SuppressWarnings("unchecked")
        public WrappingIterator(final Collection viewElements)
        {
            _viewElementIterator = viewElements.iterator();
        }
        
        public boolean hasNext()
        {
            return _viewElementIterator.hasNext();
        }

        public CMNode next()
        {
            ITagElement nextElement = (ITagElement) _viewElementIterator.next();
            CMNode node = getNamedItem(nextElement.getName());
            return node;
        }

        public void remove()
        {
            throw new UnsupportedOperationException("");
        }
        
    }
    public CMNamedNodeMap getElements()
    {
        return this;
    }

    public CMNamedNodeMap getEntities()
    {
        // no entities
        return null;
    }

    public CMNamespace getNamespace()
    {
        return new CMNamespaceImpl(_ns.getNSUri());
    }

    public String getNodeName()
    {
        return getNamespace().getURI();
    }

    public int getNodeType()
    {
        return CMNode.DOCUMENT;
    }

    public Object getProperty(String propertyName)
    {
        // none supported for now
        return null;
    }

    public boolean supports(String propertyName)
    {
        // support none for now
        return false;
    }

    private static class CMNamespaceImpl implements CMNamespace
    {
        private final String _uri;

        CMNamespaceImpl(final String uri)
        {
            _uri = uri;
        }

        public String getPrefix()
        {
            return null;
        }

        public String getURI()
        {
            return _uri;
        }

        public String getNodeName()
        {
            return getURI();
        }

        public int getNodeType()
        {
            return CMNode.NAME_SPACE;
        }

        public Object getProperty(String propertyName)
        {
            // TODO Auto-generated method stub
            return null;
        }

        public boolean supports(String propertyName)
        {
            // TODO Auto-generated method stub
            return false;
        }
    }
    public void dispose()
    {
        _elements.clear();
    }

}
