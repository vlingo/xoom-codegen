// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.codegen.content;

import java.util.stream.Stream;

public interface ModuleRetriever {

  String find(final String text);

  boolean support(final String text);

  static String retrieve(final String text) {
    return Stream.of(new JvmModuleRetriever(), new DotNetModuleRetriever(), new AgnosticModuleRetriever())
        .filter(retriever -> retriever.support(text))
        .map(retriever -> retriever.find(text)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Unable to find package"));
  }

  class JvmModuleRetriever implements ModuleRetriever {
    private final String MODULE_KEYWORD = "package";

    @Override
    public String find(final String text) {
      final int packageStartIndex = text.indexOf(MODULE_KEYWORD);
      final int packageEndIndex = text.indexOf(";");
      return text.substring(packageStartIndex + 8, packageEndIndex)
          .replaceAll(";", "").trim();
    }

    @Override
    public boolean support(final String text) {
      return text.contains(MODULE_KEYWORD);
    }
  }

  class AgnosticModuleRetriever implements ModuleRetriever {

    @Override
    public String find(final String text) {
      return Stream.of(text.split("\\r?\\n")).filter(line -> line.startsWith("package"))
          .map(line -> line.replaceAll("package", "").replaceAll(";", "").trim())
          .findFirst().orElse("");
    }

    @Override
    public boolean support(final String text) {
      return text.split("\\r?\\n").length > 0;
    }
  }

  class DotNetModuleRetriever implements ModuleRetriever {
    private final String MODULE_KEYWORD = "namespace";
    @Override
    public String find(final String text) {
      final int packageStartIndex = text.indexOf(MODULE_KEYWORD);
      final int packageEndIndex = text.substring(packageStartIndex).indexOf(";");
      return text.substring(packageStartIndex + MODULE_KEYWORD.length() + 1, packageEndIndex + packageStartIndex);
    }

    @Override
    public boolean support(final String text) {
      return text.contains(MODULE_KEYWORD);
    }
  }
}
