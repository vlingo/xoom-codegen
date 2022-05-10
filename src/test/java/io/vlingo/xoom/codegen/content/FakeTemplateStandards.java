// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.codegen.content;

import io.vlingo.xoom.codegen.template.TemplateParameters;
import io.vlingo.xoom.codegen.template.TemplateStandard;

public enum FakeTemplateStandards implements TemplateStandard {

  AGGREGATE, AGGREGATE_STATE, AGGREGATE_PROTOCOL;

  @Override
  public String resolveClassname() {
    return null;
  }

  @Override
  public String resolveClassname(String name) {
    return null;
  }

  @Override
  public String resolveClassname(TemplateParameters parameters) {
    return null;
  }

  @Override
  public String resolveClassname(String name, TemplateParameters parameters) {
    return null;
  }

  @Override
  public String resolveFilename(TemplateParameters parameters) {
    return null;
  }

  @Override
  public String resolveFilename(String name, TemplateParameters parameters) {
    return null;
  }

  @Override
  public String retrieveTemplateFilename(TemplateParameters parameters) {
    return null;
  }
}
