// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
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

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.vlingo.xoom.codegen.dialect.Dialect.KOTLIN;

public class KotlinSyntaxConverter {

  public static final ReservedWordsHandler handlerForImportStatements =
          ReservedWordsHandler.using((resolver, word) -> "`" + word + "`");

  public static final void convertFieldTypes(final CodeGenerationParameters params,
                                             final ParameterLabel parent,
                                             final ParameterLabel child) {
    params.convertValuesSyntax(KOTLIN, parent, child, value -> StringUtil.capitalize(value));
  }

  public static final void convertImports(final TemplateParameters params) {
    params.convertImportSyntax(KotlinSyntaxConverter::handleImportEntry);
  }

  protected static final String handleImportEntry(final String importEntry) {
    return Stream.of(importEntry.split("\\."))
            .map(importPart -> handlerForImportStatements.handle(KOTLIN, importPart))
            .collect(Collectors.joining("."));
  }

}
