// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.codegen.template;

import io.vlingo.xoom.codegen.CodeGenerationContext;
import io.vlingo.xoom.codegen.dialect.Dialect;

public interface OutputFileInstantiator {

  OutputFile instantiate(final CodeGenerationContext context,
                         final TemplateData templateData,
                         final Dialect dialect);

  static OutputFileInstantiator defaultInstantiation() {
    return (context, data, dialect) -> {
      final String absolutePath = context.fileLocationResolver().resolve(context, dialect, data);
      final String filename = dialect.formatFilename(data.filename());
      return new OutputFile(absolutePath, filename, "", data.isPlaceholder());
    };
  }
}
