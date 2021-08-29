// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.codegen.dialect;

import java.util.*;
import java.util.function.BiFunction;

public class ReservedWordsHandler {

  public static final Map<Dialect, List<String>> indexedReservedWords = new HashMap<>();

  static {
    indexedReservedWords.put(Dialect.JAVA,
            Collections.unmodifiableList(
                    Arrays.asList("abstract", "assert", "break", "case",
                    "catch", "class", "const", "continue", "default", "do", "else", "enum",
                    "extends", "false", "final", "finally", "for", "goto", "if", "implements", "import",
                    "instanceof", "interface", "native", "new", "null", "package", "private", "protected",
                    "public", "return", "static", "strictfp", "super", "switch", "synchronized", "this",
                    "throw", "throws", "transient", "true", "try", "void", "volatile", "while")
            ));

    indexedReservedWords.put(Dialect.KOTLIN,
            Collections.unmodifiableList(
                    Arrays.asList("object", "public", "get", "set")
            ));
  }

  public static final BiFunction<ReservedWordsHandler, String, String> SUFFIX_INSERTION_POLICY =
          (resolver, word) -> word + resolver.suffix;

  private final String suffix;
  private final BiFunction<ReservedWordsHandler, String, String> resolutionPolicy;

  public static ReservedWordsHandler noOp() {
    return using((handler, word) -> word);
  }

  public static ReservedWordsHandler using(final BiFunction<ReservedWordsHandler, String, String> resolutionPolicy) {
    return new ReservedWordsHandler("", resolutionPolicy);
  }

  public static ReservedWordsHandler usingSuffix(final String suffix) {
    return usingSuffix(suffix, SUFFIX_INSERTION_POLICY);
  }

  public static ReservedWordsHandler usingSuffix(final String suffix,
                                                 final BiFunction<ReservedWordsHandler, String, String> resolutionPolicy) {
    return new ReservedWordsHandler(suffix, resolutionPolicy);
  }

  private ReservedWordsHandler(final String suffix,
                               final BiFunction<ReservedWordsHandler, String, String> resolutionPolicy) {
    this.suffix = suffix;
    this.resolutionPolicy = resolutionPolicy;
  }

  public String handle(final Dialect dialect, final String word) {
    if(indexedReservedWords.containsKey(dialect) && indexedReservedWords.get(dialect).contains(word)) {
      return resolutionPolicy.apply(this, word);
    }
    return word;
  }

}
