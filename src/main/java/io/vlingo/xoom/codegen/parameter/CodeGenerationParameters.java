// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.codegen.parameter;

import io.vlingo.xoom.codegen.dialect.Dialect;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CodeGenerationParameters {

  private final Map<ParameterLabel, Dialect> conversionEntries = new HashMap<>();
  private final List<CodeGenerationParameter> parameters = new ArrayList<>();

  public static CodeGenerationParameters from(final ParameterLabel label, final Object value) {
    return from(label, value.toString());
  }

  public static CodeGenerationParameters from(final ParameterLabel label, final String value) {
    return from(CodeGenerationParameter.of(label, value));
  }

  public static CodeGenerationParameters from(final CodeGenerationParameter... codeGenerationParameters) {
    return new CodeGenerationParameters(Arrays.asList(codeGenerationParameters));
  }

  public static CodeGenerationParameters empty() {
    return new CodeGenerationParameters(new ArrayList<>());
  }

  private CodeGenerationParameters(final List<CodeGenerationParameter> parameters) {
    this.parameters.addAll(parameters);
  }

  public CodeGenerationParameters add(final ParameterLabel label, final String value) {
    return add(CodeGenerationParameter.of(label, value));
  }

  public CodeGenerationParameters add(final CodeGenerationParameter parameter) {
    this.parameters.add(parameter);
    return this;
  }

  public CodeGenerationParameters add(final ParameterLabel label, final Object object) {
    return add(CodeGenerationParameter.ofObject(label, object));
  }

  public void addAll(final Map<ParameterLabel, String> parameterEntries) {
    final Function<Entry<ParameterLabel, String>, CodeGenerationParameter> mapper =
            entry -> CodeGenerationParameter.of(entry.getKey(), entry.getValue());

    addAll(parameterEntries.entrySet().stream().map(mapper).collect(Collectors.toList()));
  }

  public void addAll(final CodeGenerationParameters parameters) {
    addAll(parameters.list());
  }

  public CodeGenerationParameters addAll(final List<CodeGenerationParameter> parameters) {
    this.parameters.addAll(parameters);
    return this;
  }

  public String retrieveValue(final ParameterLabel label) {
    return retrieveOne(label).value;
  }

  public <T> T retrieveValue(final ParameterLabel label, final Function<String, T> mapper) {
    return mapper.apply(retrieveValue(label));
  }

  public <T> T retrieveObject(final ParameterLabel label) {
    return retrieveOne(label).object();
  }

  public CodeGenerationParameter retrieveOne(final ParameterLabel label) {
    return parameters.stream()
            .filter(param -> param.isLabeled(label)).findFirst()
            .orElse(CodeGenerationParameter.of(label, ""));
  }

  protected List<CodeGenerationParameter> list() {
    return Collections.unmodifiableList(parameters);
  }

  public Stream<CodeGenerationParameter> retrieveAll(final ParameterLabel label) {
    return retrieveAll(label, RetrievalLevel.SUPERFICIAL);
  }

  public Stream<CodeGenerationParameter> retrieveAll(final ParameterLabel label, final RetrievalLevel retrievalLevel) {
    if (RetrievalLevel.EXTENSIVE.equals(retrievalLevel)) {
      return performBulkRetrieval(label);
    }
    return parameters.stream().filter(param -> param.isLabeled(label));
  }

  public boolean isEmpty() {
    return parameters.isEmpty();
  }

  public void convertValuesSyntax(final Dialect dialect,
                                  final ParameterLabel parentLabel,
                                  final ParameterLabel relatedLabel,
                                  final Function<String, String> converter) {
    if (!isAlreadyConverted(dialect, relatedLabel)) {
      conversionEntries.remove(relatedLabel);

      performBulkRetrieval(parentLabel).forEach(parent -> parent.convertValuesSyntax(relatedLabel, converter));

      conversionEntries.put(relatedLabel, dialect);
    }
  }

  protected void applySyntaxConverter(final ParameterLabel label, final Function<String, String> converter) {
    final List<CodeGenerationParameter> affectedParameters =
            parameters.stream().filter(param -> param.isLabeled(label))
                    .map(param -> param.formatValue(converter))
                    .collect(Collectors.toList());

    final List<CodeGenerationParameter> nonAffectedParameters =
            parameters.stream().filter(param -> !param.isLabeled(label)).collect(Collectors.toList());

    parameters.clear();
    parameters.addAll(affectedParameters);
    parameters.addAll(nonAffectedParameters);
  }

  private boolean isAlreadyConverted(final Dialect dialect, final ParameterLabel label) {
    if (conversionEntries.containsKey(label)) {
      return conversionEntries.get(label).equals(dialect);
    }
    return false;
  }

  private Stream<CodeGenerationParameter> performBulkRetrieval(final ParameterLabel label) {
    final List<CodeGenerationParameter> collected = new ArrayList<>();
    performBulkRetrieval(label, parameters.stream(), collected);
    return collected.stream();
  }

  private void performBulkRetrieval(final ParameterLabel label, final Stream<CodeGenerationParameter> source, final List<CodeGenerationParameter> collected) {
    source.forEach(parameter -> {
      if (parameter.isLabeled(label)) {
        collected.add(parameter);
      } else {
        performBulkRetrieval(label, parameter.relatedParametersAsStream(), collected);
      }
    });
  }

  public enum RetrievalLevel {
    SUPERFICIAL, EXTENSIVE
  }
}
