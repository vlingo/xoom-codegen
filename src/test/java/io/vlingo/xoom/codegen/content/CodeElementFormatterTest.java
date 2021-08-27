// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.xoom.codegen.content;

import io.vlingo.xoom.codegen.dialect.Dialect;
import io.vlingo.xoom.codegen.dialect.ReservedWordsHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CodeElementFormatterTest {

  private CodeElementFormatter javaFormatter;

  @Test
  public void testThatJavaCodeElementsAreFormatted() {
    Assert.assertEquals("XoomApp", javaFormatter.simpleNameOf("io.vlingo.xoom.XoomApp"));
    Assert.assertEquals("io.vlingo.xoom", javaFormatter.packageOf("io.vlingo.xoom.XoomApp"));
    Assert.assertEquals("io.vlingo.xoom.XoomApp", javaFormatter.qualifiedNameOf("io.vlingo.xoom", "XoomApp"));
    Assert.assertEquals("xoomApp", javaFormatter.simpleNameToAttribute("XoomApp"));
    Assert.assertEquals("public_", javaFormatter.simpleNameToAttribute("Public"));
    Assert.assertEquals("xoomApp", javaFormatter.qualifiedNameToAttribute("io.vlingo.xoom.XoomApp"));
    Assert.assertEquals("public_", javaFormatter.qualifiedNameToAttribute("io.vlingo.xoom.Public"));
    Assert.assertEquals("io.vlingo.xoom.*", javaFormatter.importAllFrom("io.vlingo.xoom"));
    Assert.assertEquals("static io.vlingo.xoom.*", javaFormatter.staticallyImportAllFrom("io.vlingo.xoom"));
    Assert.assertEquals("VLINGO_XOOM_APPLICATION", javaFormatter.staticConstant("vlingoXoomApplication"));
    Assert.assertEquals("xoomApp", javaFormatter.rectifySyntax("xoomApp"));
    Assert.assertEquals("private_", javaFormatter.rectifySyntax("private"));
  }

  @Before
  public void setUp() {
    this.javaFormatter = CodeElementFormatter.with(Dialect.JAVA, ReservedWordsHandler.usingSuffix("_"));
  }

}
