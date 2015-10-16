package org.pentaho.reporting.engine.classic.extensions.datasources.kettle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FormulaArgumentTest {

  private FormulaArgument formulaArgument;

  @Before
  public void before() {
    // formulaArgument = new
  }

  @Test
  public void create() {
    FormulaArgument argument = FormulaArgument.create( "TEST_FIELD" );
    String actualFormulaString = argument.getFormula();
    Assert.assertEquals( "=[TEST_FIELD]", actualFormulaString );
  }

}
