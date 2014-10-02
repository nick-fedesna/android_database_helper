package com.vokal.codegen.tools;

import javax.lang.model.element.TypeElement;

public class EnclosingClass {

    private final String      mClassPackage;
    private final String      mClassName;
    private final String      mTargetClass;
    private final String      mParentEnclosingClass;
    private final TypeElement mElement;

    public EnclosingClass(String aClassPackage, String aClassName, String aTargetClass,
                   String aParentEnclosingClass, TypeElement aElement) {
        mClassPackage = aClassPackage;
        mClassName = aClassName;
        mTargetClass = aTargetClass;
        mParentEnclosingClass = aParentEnclosingClass;
        mElement = aElement;
    }

    public String getClassPackage() {
        return mClassPackage;
    }

    public String getClassName() {
        return mClassName;
    }

    public String getTargetClass() {
        return mTargetClass;
    }

    public String getParentEnclosingClass() {
        return mParentEnclosingClass;
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
        if (!mClassPackage.equals(that.mClassPackage))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mClassPackage.hashCode();
        result = 31 * result + mClassName.hashCode();
        return result;
    }
}
