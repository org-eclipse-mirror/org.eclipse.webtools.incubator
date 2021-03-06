package org.eclipse.wst.rng.contentassist.internal.resolver.test;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.eclipse.wst.rng.contentassist.ICompletionProposalCalculator;
import org.eclipse.wst.rng.contentassist.IQNameCompletionProposal;
import org.eclipse.wst.rng.contentassist.internal.DefaultQNameCompletionProposal;
import org.eclipse.wst.rng.contentassist.internal.function.ProposedName;
import org.eclipse.wst.rng.contentassist.internal.resolver.test.AbstractInferenceTest.INodeTest;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;



public class AttributeNamesAmongCompletionsTest implements INodeTest {
	private ICompletionProposalCalculator proposalCalc;
	
	public AttributeNamesAmongCompletionsTest(
			ICompletionProposalCalculator proposalCalc) {
		super();
		this.proposalCalc = proposalCalc;
	}

	// @Override
	public boolean runOnNode(Node node) {
		if (node instanceof Element) {
			Element element = (Element)node;
			return element.getAttributes().getLength() > 0;
		}
		return false;
	}
	
	// @Override
	public void execute(Node node) {
		Element element = (Element)node;
		NamedNodeMap attributes = element.getAttributes();
		for (int i = 0; i < attributes.getLength(); i++) {
			Attr attribute = (Attr)attributes.item(i);
			if (!"http://www.w3.org/2000/xmlns/".equals(attribute.getNamespaceURI())) {
				element.removeAttributeNode(attribute);
				Set<IQNameCompletionProposal> attributeNameProposals = proposalCalc.getAttributeNameProposals(element);
				assertTrue("Attribute " + attribute + " not found among completion proposals.", attributeNameProposals.contains(qName(attribute)));
				element.setAttributeNode(attribute);
			}
		}		
	}
	
	private IQNameCompletionProposal qName(Node node) {
		return new DefaultQNameCompletionProposal(new ProposedName(node.getNamespaceURI(), node.getLocalName(), null, null), null, null);
	}
}
