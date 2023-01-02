// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.codegen.template;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;
import io.vlingo.xoom.codegen.CodeGenerationException;

import java.util.Locale;

import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;

public class TemplateProcessorConfiguration {

  public final Configuration configuration;

  private static TemplateProcessorConfiguration instance;

  public static TemplateProcessorConfiguration instance() {
    if (instance == null) {
      instance = new TemplateProcessorConfiguration();
    }
    return instance;
  }

  private TemplateProcessorConfiguration() {
    try {
      this.configuration = new Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
      configuration.setClassForTemplateLoading(TemplateProcessor.class, "/");
      configuration.setDefaultEncoding("UTF-8");
      configuration.setLocale(Locale.US);
      configuration.setSharedVariable("fns", TemplateCustomFunctions.instance());
      configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
      configuration.setObjectWrapper(objectWrapper());
    } catch (final TemplateModelException e) {
      e.printStackTrace();
      throw new CodeGenerationException(e);
    }
  }

  private DefaultObjectWrapper objectWrapper() {
    final DefaultObjectWrapper objectWrapper = new DefaultObjectWrapper(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    objectWrapper.setExposeFields(true);
    return objectWrapper;
  }

}
