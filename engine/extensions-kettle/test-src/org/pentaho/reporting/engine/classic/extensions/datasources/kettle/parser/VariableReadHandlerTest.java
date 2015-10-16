package org.pentaho.reporting.engine.classic.extensions.datasources.kettle.parser;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.libraries.xmlns.parser.RootXmlReadHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class VariableReadHandlerTest {

  private VariableReadHandler variableReadHandler;

  private Attributes testAttributes;

  @Before
  public void before() throws SAXException {
    ClassicEngineBoot.getInstance().start();
    variableReadHandler = new VariableReadHandler();
    variableReadHandler.init( mock( RootXmlReadHandler.class ), "TEST_URI", "TEST_TAG" );

    testAttributes = mock( Attributes.class );
    when( testAttributes.getValue( anyString(), eq( "name" ) ) ).thenReturn( "TEST_NAME" );
    when( testAttributes.getValue( anyString(), eq( "repository" ) ) ).thenReturn( "TEST_REPOSITORY" );
    when( testAttributes.getValue( anyString(), eq( "username" ) ) ).thenReturn( "TEST_USERNAME" );
    when( testAttributes.getValue( anyString(), eq( "password" ) ) ).thenReturn( "TEST_PASSWORD" );
    when( testAttributes.getValue( anyString(), eq( "step" ) ) ).thenReturn( "TEST_STEP" );
  }

  @Test( expected = SAXException.class )
  public void startParsing_exception_on_missed_formula() throws SAXException {
    variableReadHandler.startParsing( testAttributes );
  }

  @Test
  public void startParsing() throws SAXException {
    when( testAttributes.getValue( anyString(), eq( "datarow-name" ) ) ).thenReturn( "TEST_DATAROW_NAME" );
    variableReadHandler.startParsing( testAttributes );
  }

  @Test( expected = SAXException.class )
  public void startParsing_exception_on_missed_variable_name() throws SAXException {
    when( testAttributes.getValue( anyString(), eq( "formula" ) ) ).thenReturn( "TEST_FORMULA" );
    variableReadHandler.startParsing( testAttributes );
  }

}
