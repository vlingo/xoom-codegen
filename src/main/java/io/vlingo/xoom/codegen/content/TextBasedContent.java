// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.codegen.content;

import io.vlingo.xoom.codegen.template.OutputFile;
import io.vlingo.xoom.codegen.template.TemplateStandard;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.WRITE;

public class TextBasedContent extends Content {

  public final File file;
  public final String text;
  private final Filer filer;
  private final Element source;
  private final String offset;
  private final boolean placeholder;

  public TextBasedContent(final TemplateStandard standard,
                          final OutputFile outputFile,
                          final Element source,
                          final Filer filer,
                          final String text) {
    super(standard);
    this.filer = filer;
    this.source = source;
    this.file = outputFile.toFile();
    this.offset = outputFile.offset();
    this.placeholder = outputFile.isPlaceholder();
    this.text = text;
  }

  @Override
  public void create() {
    try {
      if (filer == null) {
        handleDefaultCreation();
      } else {
        handleCreationFromSourceElement();
      }
    } catch (final IOException e) {
      System.out.println(e.getMessage());
    }
  }

  private void handleDefaultCreation() throws IOException {
    if (Files.isRegularFile(file.toPath())) {
      if (hasOffset()) {
        final StringBuilder fileContent =
                new StringBuilder(new String(Files.readAllBytes(file.toPath())));

        final int offsetPosition =
                fileContent.indexOf(offset) + offset.length();

        final String updateContent =
                fileContent.insert(offsetPosition, text).toString();

        Files.write(file.toPath(), updateContent.getBytes(), WRITE);
      } else {
        Files.write(file.toPath(), text.getBytes(), APPEND);
      }
    } else {
      file.getParentFile().mkdirs();
      file.createNewFile();
      Files.write(file.toPath(), text.getBytes());
    }
  }

  private void handleCreationFromSourceElement() throws IOException {
    final Writer writer =
            filer.createSourceFile(retrieveQualifiedName(), source).openWriter();
    writer.write(text);
    writer.close();
  }

  @Override
  public String retrieveName() {
    return FilenameUtils.removeExtension(file.getName());
  }

  @Override
  public String retrievePackage() {
    return ModuleRetriever.retrieve(text);
  }

  @Override
  public String retrieveQualifiedName() {
    return CodeElementFormatter.newInstance().qualifiedNameOf(retrievePackage(), retrieveName());
  }

  @Override
  public boolean contains(final String term) {
    return text.contains(term);
  }

  @Override
  public boolean canWrite() {
    return !placeholder;
  }

  private boolean hasOffset() {
    return offset != null && !offset.isEmpty();
  }

}
