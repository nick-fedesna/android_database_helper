package com.vokal.codegen.tools;

import javax.lang.model.element.TypeElement;

public class EnclosingClass {

    private final String      mClassPackage;
    private final String      mClassName;
    private final TypeElement mElement;

    public EnclosingClass(String aClassPackage, String aClassName, TypeElement aElement) {
        mClassPackage = aClassPackage;
        mClassName = aClassName;
        mElement = aElement;
    }

    public String getClassPackage() {
        return mClassPackage;
    }

    public String getClassName() {
        return mClassName;
    }

    public TypeElement getElement() {
        return mElement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        EnclosingClass that = (EnclosingClass) o;

        if (!mClassName.equals(that.mClassName))
            return false;
        return mClassPackage.equals(that.mClassPackage);

    }

    @Override
    public int hashCode() {
        int result = mClassPackage.hashCode();
        result = 31 * result + mClassName.hashCode();
        return result;
    }
}
