package org.kohsuke.rngom.binary;

import org.kohsuke.rngom.binary.visitor.PatternFunction;
import org.kohsuke.rngom.binary.visitor.PatternVisitor;
import org.kohsuke.rngom.nc.NameClass;
import org.kohsuke.rngom.nc.SimpleNameClass;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public final class AttributePattern extends Pattern {
  private NameClass nameClass;
  private Pattern p;
  private Locator loc;

  AttributePattern(NameClass nameClass, Pattern value, Locator loc) {
    super(false,
	  EMPTY_CONTENT_TYPE,
	  combineHashCode(ATTRIBUTE_HASH_CODE,
			  nameClass.hashCode(),
			  value.hashCode()));
    this.nameClass = nameClass;
    this.p = value;
    this.loc = loc;
  }

  Pattern expand(SchemaPatternBuilder b) {
    Pattern ep = p.expand(b);
    if (ep != p)
      return b.makeAttribute(nameClass, ep, loc);
    else
      return this;
  }

  void checkRestrictions(int context, DuplicateAttributeDetector dad, Alphabet alpha)
    throws RestrictionViolationException {
    switch (context) {
    case START_CONTEXT:
      throw new RestrictionViolationException("start_contains_attribute");
    case ELEMENT_CONTEXT:
      if (nameClass.isOpen())
	throw new RestrictionViolationException("open_name_class_not_repeated");
      break;
    case ELEMENT_REPEAT_GROUP_CONTEXT:
      throw new RestrictionViolationException("one_or_more_contains_group_contains_attribute");
    case ELEMENT_REPEAT_INTERLEAVE_CONTEXT:
      throw new RestrictionViolationException("one_or_more_contains_interleave_contains_attribute");
    case LIST_CONTEXT:
      throw new RestrictionViolationException("list_contains_attribute");
    case ATTRIBUTE_CONTEXT:
      throw new RestrictionViolationException("attribute_contains_attribute");
    case DATA_EXCEPT_CONTEXT:
      throw new RestrictionViolationException("data_except_contains_attribute");
    }
    if (!dad.addAttribute(nameClass)) {
      if (nameClass instanceof SimpleNameClass)
        throw new RestrictionViolationException("duplicate_attribute_detail", ((SimpleNameClass)nameClass).name);
      else
        throw new RestrictionViolationException("duplicate_attribute");
    }
    try {
      p.checkRestrictions(ATTRIBUTE_CONTEXT, null, null);
    }
    catch (RestrictionViolationException e) {
      e.maybeSetLocator(loc);
      throw e;
    }
  }

  boolean samePattern(Pattern other) {
    if (!(other instanceof AttributePattern))
      return false;
    AttributePattern ap = (AttributePattern)other;
    return nameClass.equals(ap.nameClass)&& p == ap.p;
  }

  void checkRecursion(int depth) throws SAXException {
    p.checkRecursion(depth);
  }

  public void accept(PatternVisitor visitor) {
    visitor.visitAttribute(nameClass, p);
  }

  public Object apply(PatternFunction f) {
    return f.caseAttribute(this);
  }

  public Pattern getContent() {
    return p;
  }

  public NameClass getNameClass() {
    return nameClass;
  }

  public Locator getLocator() {
    return loc;
  }
}
