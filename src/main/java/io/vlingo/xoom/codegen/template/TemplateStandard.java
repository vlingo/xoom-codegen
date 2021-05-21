package io.vlingo.xoom.codegen.template;

public interface TemplateStandard {

  default String resolveClassname() {
    throw new UnsupportedOperationException("Unable to resolve classname");
  }

  default String resolveClassname(final String name) {
    throw new UnsupportedOperationException("Unable to resolve classname");
  }

  default String resolveClassname(final TemplateParameters parameters) {
    throw new UnsupportedOperationException("Unable to resolve classname");
  }

  default String resolveClassname(final String name, final TemplateParameters parameters) {
    throw new UnsupportedOperationException("Unable to resolve classname");
  }

  default String resolveFilename(final TemplateParameters parameters) {
    throw new UnsupportedOperationException("Unable to resolve filename");
  }

  default String resolveFilename(final String name, final TemplateParameters parameters) {
    throw new UnsupportedOperationException("Unable to resolve filename");
  }

  String retrieveTemplateFilename(final TemplateParameters parameters);

}
