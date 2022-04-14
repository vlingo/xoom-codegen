// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.codegen.template;

import static io.vlingo.xoom.codegen.template.ParameterKey.Defaults.PACKAGE_NAME;

public class BasicTemplateData extends TemplateData {

  private final TemplateParameters parameters;
  private final TemplateStandard standard;

  public static TemplateData of(final TemplateStandard standard) {
    return of(standard, TemplateParameters.empty());
  }

  public static TemplateData of(final TemplateStandard standard, final String packageName) {
    return of(standard, TemplateParameters.with(PACKAGE_NAME, packageName));
  }

  public static TemplateData of(final TemplateStandard standard, final TemplateParameters templateParameters) {
    return new BasicTemplateData(standard, templateParameters);
  }

  private BasicTemplateData(final TemplateStandard standard, final TemplateParameters parameters) {
    this.parameters = parameters;
    this.standard = standard;
  }

  @Override
  public TemplateParameters parameters() {
    return parameters;
  }

  @Override
  public TemplateStandard standard() {
    return standard;
  }

}
