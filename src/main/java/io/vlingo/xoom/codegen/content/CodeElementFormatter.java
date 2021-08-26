// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.codegen.content;

import com.google.common.base.CaseFormat;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;

import java.beans.Introspector;

public class CodeElementFormatter {

  private final ReservedWordsHandler reservedWordsHandler;

  public static CodeElementFormatter newInstance() {
    return new CodeElementFormatter(ReservedWordsHandler.noOp());
  }

  public static CodeElementFormatter with(final ReservedWordsHandler reservedWordsHandler) {
    return new CodeElementFormatter(reservedWordsHandler);
  }

  public CodeElementFormatter(final ReservedWordsHandler reservedWordsHandler) {
    this.reservedWordsHandler = reservedWordsHandler;
  }

  public String qualifiedNameOf(final String packageName,
                                       final String className) {
    return packageName + "." + className;
  }

  public String simpleNameToAttribute(final String simpleName) {
    return Introspector.decapitalize(simpleName);
  }

  public String qualifiedNameToAttribute(final String qualifiedName) {
    return simpleNameToAttribute(simpleNameOf(qualifiedName));
  }

  public String simpleNameOf(final String qualifiedName) {
    return qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
  }

  public String packageOf(final String qualifiedName) {
    return qualifiedName.substring(0, qualifiedName.lastIndexOf("."));
  }

  public String importAllFrom(final String packageName) {
    return packageName + ".*";
  }

  public String staticallyImportAllFrom(final String projectionSourceTypesQualifiedName) {
    return "static " + importAllFrom(projectionSourceTypesQualifiedName);
  }

  public String staticConstant(final String constantName) {
    return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, constantName);
  }
}
