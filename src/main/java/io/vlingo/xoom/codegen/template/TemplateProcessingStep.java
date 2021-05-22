// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.codegen.template;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.CodeGenerationStep;
import io.vlingo.xoom.codegen.dialect.Dialect;

import java.util.List;

public abstract class TemplateProcessingStep implements CodeGenerationStep {

  @Override
  public void process(final CodeGenerationContext context) {
    final Dialect dialect = resolveDialect(context);
    dialect.resolvePreParametersProcessing(context.parameters());
    buildTemplatesData(context).forEach(templateData -> {
      dialect.resolvePostParametersProcessing(templateData.parameters());
      final String code = TemplateProcessor.instance().process(dialect, templateData);
      context.registerTemplateProcessing(dialect, templateData, code);
    });
  }

  protected abstract List<TemplateData> buildTemplatesData(final CodeGenerationContext context);

  protected Dialect resolveDialect(final CodeGenerationContext context) {
    return Dialect.findDefault();
  }

}
