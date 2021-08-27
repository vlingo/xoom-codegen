// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.codegen.content;

import com.google.common.base.CaseFormat;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;

import java.beans.Introspector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeElementFormatter {

  private final Dialect dialect;
  private final ReservedWordsHandler reservedWordsHandler;

  public static CodeElementFormatter newInstance() {
    return with(Dialect.findDefault(), ReservedWordsHandler.noOp());
  }

  public static CodeElementFormatter with(final Dialect dialect,
                                          final ReservedWordsHandler reservedWordsHandler) {
    return new CodeElementFormatter(dialect, reservedWordsHandler);
  }

  public CodeElementFormatter(final Dialect dialect,
                              final ReservedWordsHandler reservedWordsHandler) {
    this.dialect = dialect;
    this.reservedWordsHandler = reservedWordsHandler;
  }

  public String simpleNameOf(final String qualifiedName) {
    return qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
  }

  public String packageOf(final String qualifiedName) {
    return qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
  }

  public String qualifiedNameOf(final String packageName,
                                final String className) {
    return packageName + "." + className;
  }

  public String simpleNameToAttribute(final String simpleName) {
    return rectifySyntax(Introspector.decapitalize(simpleName));
  }

  public String qualifiedNameToAttribute(final String qualifiedName) {
    return rectifySyntax(simpleNameToAttribute(simpleNameOf(qualifiedName)));
  }

  public String importAllFrom(final String packageName) {
    return packageName + ".*";
  }

  public String staticallyImportAllFrom(final String qualifiedName) {
    return "static " + importAllFrom(qualifiedName);
  }

  public String staticConstant(final String constantName) {
    return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, constantName);
  }

  public String rectifySyntax(final String elementName) {
    if(elementName.contains(dialect.packageSeparator())) {
      return rectifyPackageSyntax(elementName);
    }
    return reservedWordsHandler.handle(dialect, elementName);
  }

  public String rectifyPackageSyntax(final String packageName) {
    return Stream.of(packageName.split(dialect.packageSeparator()))
            .map(packageElement -> reservedWordsHandler.handle(dialect, packageElement))
            .collect(Collectors.joining("."));
  }
}
