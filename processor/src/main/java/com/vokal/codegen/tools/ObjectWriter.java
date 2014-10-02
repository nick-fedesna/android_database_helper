package com.vokal.codegen.tools;

import javax.tools.JavaFileObject;

class ObjectWriter extends AbsWriter {

  ObjectWriter(JavaFileObject jfo, String suffix, EnclosingClass enclosingClass) {
    super(jfo, suffix, enclosingClass);
  }

  @Override protected String getWriterType() {
    return "Bundle";
  }
}
