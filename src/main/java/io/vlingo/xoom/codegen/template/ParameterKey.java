// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.codegen.template;

public interface ParameterKey {

  String value();

  enum Defaults implements ParameterKey {
    IMPORTS("imports"),
    PACKAGE_NAME("packageName");

    private final String value;

    Defaults(String value) {
      this.value = value;
    }

    @Override
    public String value() {
      return null;
    }
  }
}
