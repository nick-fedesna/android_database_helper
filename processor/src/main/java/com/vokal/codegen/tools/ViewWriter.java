package com.vokal.codegen.tools;

import javax.tools.JavaFileObject;

class ViewWriter extends AbsWriter {

  private static final String SUPER_SUFFIX = "\"$$SUPER$$\"";
  private static final String SUPER_KEY = BASE_KEY + " + " + SUPER_SUFFIX;

  public ViewWriter(JavaFileObject jfo, String suffix, EnclosingClass enclosingClass) {
    super(jfo, suffix, enclosingClass);
  }

  @Override protected String getWriterType() {
    return "Parcelable";
  }
}
