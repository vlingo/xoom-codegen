// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.codegen;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import io.vlingo.xoom.codegen.dialect.Dialect;

public class TextExpectation {

  private final Dialect dialect;

  public static TextExpectation onJava() {
    return new TextExpectation(Dialect.JAVA);
  }

  public static TextExpectation onCSharp() {
    return new TextExpectation(Dialect.C_SHARP);
  }

  public static TextExpectation onReactJs() {
     return new TextExpectation(Dialect.REACTJS);
  }

  private TextExpectation(final Dialect dialect) {
    this.dialect = dialect;
  }

  public String read(final String textFileName) {
    final String path =
            String.format("/text-expectations/%s/%s.text",
                    dialect.name().toLowerCase(), textFileName);

    try {
      final InputStream stream = TextExpectation.class.getResourceAsStream(path);
      return IOUtils.toString(stream, StandardCharsets.UTF_8.name());
    } catch (Exception cause) {
      throw new RuntimeException(String.format("Failed to load text expectations from `%s`.", path), cause);
    }
  }
}
