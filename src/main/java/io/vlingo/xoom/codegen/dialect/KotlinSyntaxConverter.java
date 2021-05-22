// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.codegen.dialect;

import freemarker.template.utility.StringUtil;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.parameter.ParameterLabel;
import io.vlingo.xoom.codegen.template.TemplateParameters;

import static io.vlingo.xoom.codegen.dialect.Dialect.KOTLIN;

public class KotlinSyntaxConverter {

  public static final void convertFieldTypes(final CodeGenerationParameters params,
                                             final ParameterLabel parent,
                                             final ParameterLabel child) {
    params.convertValuesSyntax(KOTLIN, parent, child, value -> StringUtil.capitalize(value));
  }

  public static final void convertImports(final TemplateParameters params) {
    params.convertImportSyntax(KotlinSyntaxConverter::handleImportEntry);
  }

  protected static final String handleImportEntry(final String importEntry) {
    String resolvedImport = importEntry;
    for (final String reservedWord : DialectSetup.KOTLIN_RESERVED_WORDS) {
      resolvedImport = handleReservedWord(reservedWord, resolvedImport);
    }
    return resolvedImport;
  }

  private static final String handleReservedWord(final String reservedWord, final String importEntry) {
    final String resolvedImport =
            !importEntry.startsWith(reservedWord + ".") ? importEntry :
                    importEntry.replaceFirst(reservedWord + "\\.", "`" + reservedWord + "`.");
    return resolvedImport.replaceAll("\\." + reservedWord + "\\.", ".`" + reservedWord + "`.");
  }
}
