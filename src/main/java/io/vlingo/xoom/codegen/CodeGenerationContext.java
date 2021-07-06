// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentLoader;
import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameter;
import io.vlingo.xoom.codegen.parameter.CodeGenerationParameters;
import io.vlingo.xoom.codegen.parameter.ParameterLabel;
import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.codegen.template.OutputFileInstantiator;
import io.vlingo.xoom.codegen.template.TemplateData;
import io.vlingo.xoom.codegen.template.TemplateStandard;

public class CodeGenerationContext {

  private Filer filer;
  private Element source;
  private final CodeGenerationParameters parameters;
  private final List<Content> contents = new ArrayList<>();
  private final List<TemplateData> templatesData = new ArrayList<>();
  private FileLocationResolver fileLocationResolver = (context, dialect, data) -> "";
  private OutputFileInstantiator outputFileInstantiator = OutputFileInstantiator.defaultInstantiation();

  public static CodeGenerationContext empty() {
    return new CodeGenerationContext();
  }

  public static CodeGenerationContext using(final Filer filer, final Element source) {
    return new CodeGenerationContext(filer, source);
  }

  public static CodeGenerationContext with(final Map<ParameterLabel, String> parameters) {
    return new CodeGenerationContext().on(parameters);
  }

  public static CodeGenerationContext with(final CodeGenerationParameters parameters) {
    return new CodeGenerationContext().on(parameters);
  }

  private CodeGenerationContext() {
    this.parameters = CodeGenerationParameters.empty();
  }

  private CodeGenerationContext(final Filer filer, final Element source) {
    this();
    this.filer = filer;
    this.source = source;
  }

  public CodeGenerationContext contents(final List<ContentLoader> loaders) {
    loaders.stream().filter(ContentLoader::shouldLoad).forEach(loader -> loader.load(this));
    return this;
  }

  public CodeGenerationContext contents(final Content... contents) {
    this.contents.addAll(Arrays.asList(contents));
    return this;
  }

  public CodeGenerationContext with(final ParameterLabel label, final String value) {
    on(CodeGenerationParameters.from(label, value));
    return this;
  }

  public CodeGenerationContext on(final Map<ParameterLabel, String> parameters) {
    this.parameters.addAll(parameters);
    return this;
  }

  public CodeGenerationContext on(final CodeGenerationParameters parameters) {
    this.parameters.addAll(parameters);
    return this;
  }

  @SuppressWarnings("unchecked")
  public <T> T parameterOf(final ParameterLabel label) {
    return (T) parameterOf(label, value -> value);
  }

  public <T> T parameterOf(final ParameterLabel label, final Function<String, T> mapper) {
    final String value = parameters.retrieveValue(label);
    return mapper.apply(value);
  }

  public <T> T parameterObjectOf(final ParameterLabel label) {
    return parameters.retrieveObject(label);
  }

  public List<TemplateData> templateParametersOf(final TemplateStandard standard) {
    return templatesData.stream().filter(templateData -> templateData.hasStandard(standard))
            .collect(Collectors.toList());
  }

  public void registerTemplateProcessing(final Dialect dialect, final TemplateData templateData, final String text) {
    final OutputFile outputFile = outputFileInstantiator.instantiate(this, templateData, dialect);
    this.addContent(templateData.standard(), outputFile, text);
    this.templatesData.add(templateData);
  }

  //TODO: Make it private
  public CodeGenerationContext addContent(final TemplateStandard standard,
                                          final OutputFile file,
                                          final String text) {
    this.contents.add(Content.with(standard, file, filer, source, text));
    return this;
  }

  public CodeGenerationContext addContent(final TemplateStandard standard,
                                          final TypeElement type) {
    this.contents.add(Content.with(standard, type));
    return this;
  }

  public CodeGenerationContext addContent(final TemplateStandard standard,
                                          final TypeElement protocolType,
                                          final TypeElement actorType) {
    this.contents.add(Content.with(standard, protocolType, actorType));
    return this;
  }

  public Stream<CodeGenerationParameter> parametersOf(final ParameterLabel label) {
    return parameters.retrieveAll(label);
  }

  public boolean hasParameter(final ParameterLabel label) {
    return this.parameterOf(label) != null &&
            !this.<String>parameterOf(label).trim().isEmpty();
  }

  public CodeGenerationContext fileLocationResolver(final FileLocationResolver fileLocationResolver) {
    this.fileLocationResolver = fileLocationResolver;
    return this;
  }

  public Content findContent(final TemplateStandard standard) {
    return contents.stream().filter(content -> content.has(standard)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unable to find content " + standard));
  }


  public Content findContent(final TemplateStandard standard, final String contentName) {
    return contents.stream().filter(content -> content.has(standard) && content.isNamed(contentName)).findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Unable to find content " + standard + " - " + contentName));
  }

  public List<Content> contents() {
    return Collections.unmodifiableList(contents);
  }

  public CodeGenerationParameters parameters() {
    return parameters;
  }

  public FileLocationResolver fileLocationResolver() {
    return this.fileLocationResolver;
  }

  public CodeGenerationContext outputFileInstantiator(final OutputFileInstantiator outputFileInstantiator) {
    this.outputFileInstantiator = outputFileInstantiator;
    return this;
  }

}
