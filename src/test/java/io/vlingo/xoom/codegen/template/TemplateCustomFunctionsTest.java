// Copyright © 2012-2023 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.codegen.template;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class TemplateCustomFunctionsTest {

  private TemplateCustomFunctions fns = TemplateCustomFunctions.instance();

  @Test
  public void capitalizeTest() {
    assertEquals("Customer", fns.capitalize("customer"));
    assertEquals("Customer", fns.capitalize("Customer"));
  }

  @Test
  public void decapitalizeTest() {
    assertEquals("customer", fns.decapitalize("customer"));
    assertEquals("customer", fns.decapitalize("Customer"));
  }

  @Test
  public void capitalizeMultiWordTest() {
    assertEquals("Xoom Petclinic", fns.capitalizeMultiWord("xoom petclinic"));
  }

  @Test
  public void makePluralTest() {
    assertEquals("Pets", fns.makePlural("Pet"));
    assertEquals("Houses", fns.makePlural("House"));
    assertEquals("Buses", fns.makePlural("Bus"));
    assertEquals("Lunches", fns.makePlural("Lunch"));
    assertEquals("Cities", fns.makePlural("City"));
    assertEquals("Puppies", fns.makePlural("Puppy"));
    assertEquals("Boys", fns.makePlural("Boy"));
  }
}
