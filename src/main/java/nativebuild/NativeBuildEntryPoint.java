package nativebuild;

import io.vlingo.xoom.codegen.content.Content;
import io.vlingo.xoom.codegen.content.ContentQuery;
import io.vlingo.xoom.codegen.template.TemplateCustomFunctions;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;

import java.util.ArrayList;

public final class NativeBuildEntryPoint {
  @CEntryPoint(name = "Java_io_vlingo_xoom_codegennative_Native_from")
  public static int from(@CEntryPoint.IsolateThreadContext long isolateId, CCharPointer message) {
    final String messageString = CTypeConversion.toJavaString(message);
    TemplateCustomFunctions fns = TemplateCustomFunctions.instance();

    ContentQuery.findFullyQualifiedClassNames(null, new ArrayList<Content>());
    return 0;
  }
}
