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

public class KettleTransFromRepositoryReadHandlerTest {

  private KettleTransFromRepositoryReadHandler kettleTransFromRepositoryReadHandler;

  private Attributes testAttributes;

  @Before
  public void before() throws SAXException {
    ClassicEngineBoot.getInstance().start();
    kettleTransFromRepositoryReadHandler = new KettleTransFromRepositoryReadHandler();
    kettleTransFromRepositoryReadHandler.init( mock( RootXmlReadHandler.class ), "TEST_URI", "TEST_TAG" );

    testAttributes = mock( Attributes.class );
    when( testAttributes.getValue( anyString(), eq( "name" ) ) ).thenReturn( "TEST_NAME" );
    when( testAttributes.getValue( anyString(), eq( "repository" ) ) ).thenReturn( "TEST_REPOSITORY" );
    when( testAttributes.getValue( anyString(), eq( "username" ) ) ).thenReturn( "TEST_USERNAME" );
    when( testAttributes.getValue( anyString(), eq( "password" ) ) ).thenReturn( "TEST_PASSWORD" );
    when( testAttributes.getValue( anyString(), eq( "step" ) ) ).thenReturn( "TEST_STEP" );
  }

  @Test( expected = SAXException.class )
  public void startParsing_exception_on_missed_transformation() throws SAXException {
    kettleTransFromRepositoryReadHandler.startParsing( testAttributes );
  }

  @Test( expected = SAXException.class )
  public void startParsing_exception_on_missed_directory() throws SAXException {
    when( testAttributes.getValue( anyString(), eq( "transformation" ) ) ).thenReturn( "TEST_TRANSFORMATION" );
    kettleTransFromRepositoryReadHandler.startParsing( testAttributes );
  }

  @Test
  public void getObject() throws SAXException {
    when( testAttributes.getValue( anyString(), eq( "transformation" ) ) ).thenReturn( "TEST_TRANSFORMATION" );
    when( testAttributes.getValue( anyString(), eq( "directory" ) ) ).thenReturn( "TEST_DIRECTORY" );
    kettleTransFromRepositoryReadHandler.startParsing( testAttributes );
    kettleTransFromRepositoryReadHandler.doneParsing();
    kettleTransFromRepositoryReadHandler.getObject();
  }

}
