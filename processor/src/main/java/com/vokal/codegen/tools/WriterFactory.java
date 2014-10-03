package com.vokal.codegen.tools;

import java.io.IOException;
import javax.annotation.processing.Filer;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;

public class WriterFactory {
  private final Filer filer;
  private final String suffix;

  public WriterFactory(Filer filer, String suffix) {
    this.filer = filer;
    this.suffix = suffix;
  }

  public CodeGenWriter writeClass(EnclosingClass enclosingClass) throws IOException {
    TypeElement classType = enclosingClass.getElement();
    String fqcn = enclosingClass.getClassPackage() + "." + enclosingClass.getClassName() + suffix;
    JavaFileObject jfo = filer.createSourceFile(fqcn, classType);
    return new CodeGenWriter(jfo, suffix, enclosingClass);
  }
}
