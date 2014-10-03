package com.vokal.codegen.tools;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public class AnnotatedField {

    private final String      mName;
    private final TypeMirror  mType;
    private final TypeElement mEnclosingClassType;

    AnnotatedField(Element aElement) {
        this(aElement.getSimpleName().toString(), aElement.asType(), (TypeElement) aElement.getEnclosingElement());
    }

    AnnotatedField(String aName, TypeMirror aType, TypeElement aEnclosingClassType) {
        mName = aName;
        mType = aType;
        mEnclosingClassType = aEnclosingClassType;
    }

    public String getName() {
        return mName;
    }

    public String getSimpleType() {
        return mType.toString().substring(mType.toString().lastIndexOf('.')+1);
    }

    public TypeElement getEnclosingClassType() {
        return mEnclosingClassType;
    }
}
