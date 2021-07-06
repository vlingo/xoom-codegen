// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package nativebuild;

import java.util.ArrayList;

import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.template.TemplateCustomFunctions;

public final class NativeBuildEntryPoint {
  @SuppressWarnings("unused")
  @CEntryPoint(name = "Java_io_vlingo_xoom_codegennative_Native_from")
  public static int from(@CEntryPoint.IsolateThreadContext long isolateId, CCharPointer message) {
    final String messageString = CTypeConversion.toJavaString(message);
    TemplateCustomFunctions fns = TemplateCustomFunctions.instance();

    ContentQuery.findFullyQualifiedClassNames(null, new ArrayList<Content>());
    return 0;
  }
}
