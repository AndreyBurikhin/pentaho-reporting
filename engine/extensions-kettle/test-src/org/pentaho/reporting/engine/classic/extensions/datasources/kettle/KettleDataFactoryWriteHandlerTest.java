/*
 * This program is free software; you can redistribute it and/or modify it under the
 *  terms of the GNU Lesser General Public License, version 2.1 as published by the Free Software
 *  Foundation.
 *
 *  You should have received a copy of the GNU Lesser General Public License along with this
 *  program; if not, you can obtain a copy at http://www.gnu.org/licenses/old-licenses/lgpl-2.1.html
 *  or from the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *  without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU Lesser General Public License for more details.
 *
 *  Copyright (c) 2006 - 2015 Pentaho Corporation..  All rights reserved.
 */

package org.pentaho.reporting.engine.classic.extensions.datasources.kettle;


import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static junit.framework.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.pentaho.reporting.engine.classic.core.modules.parser.extwriter.ReportWriterContext;
import org.pentaho.reporting.engine.classic.extensions.datasources.kettle.writer.KettleDataFactoryWriteHandler;
import org.pentaho.reporting.libraries.xmlns.writer.XmlWriter;
import org.w3c.dom.*;


public class KettleDataFactoryWriteHandlerTest
{
  private static final String FILE_NAME = "test-file.xml";
  private static final String STEP_NAME = "step";
  private static final String QUERY_NAME = "default";
  private static final String REPO_NAME = "repo";
  private static final String TRANS_NAME = "trans";
  private static final String USER_NAME = "user";
  private static final String PASS = "pass";
  private static final String DIRECTORY_NAME = "dir";

  public void testAbstractKettleTransformationProducerWritesCorrect(AbstractKettleTransformationProducer producer,
                                                                    Map<String, String> params,
                                                                    String tagName) throws Exception
  {
    KettleDataFactoryWriteHandler writeHandler = new KettleDataFactoryWriteHandler();
    ReportWriterContext reportWriterContext = Mockito.mock(ReportWriterContext.class);

    KettleDataFactory factory = new KettleDataFactory();
    factory.setQuery(QUERY_NAME, producer);

    Writer writer = new FileWriter(new File(FILE_NAME));
    try
    {
      final XmlWriter xmlWriter = new XmlWriter(writer);
      writeHandler.write(reportWriterContext, xmlWriter, factory);
    }
    finally
    {
      writer.close();
    }

    checkParsing(params, tagName);

  }

  @Test
  public void testKettleTransFromRepositoryProducerWritesCorrect() throws Exception
  {
    AbstractKettleTransformationProducer producer = new KettleTransFromRepositoryProducer(REPO_NAME, DIRECTORY_NAME, TRANS_NAME, STEP_NAME,
        USER_NAME, PASS, new FormulaArgument[0], new FormulaParameter[0]);

    Map<String, String> params = new HashMap<String, String>();
    params.put("name", QUERY_NAME);
    params.put("repository", REPO_NAME);
    params.put("directory", DIRECTORY_NAME);
    params.put("transformation", TRANS_NAME);
    params.put("step", STEP_NAME);
    params.put("username", USER_NAME);
    params.put("password", PASS);

    testAbstractKettleTransformationProducerWritesCorrect(producer, params, "data:query-repository");
  }

  @Test
  public void testKettleDataFactoryWriteHandlerWritesCorrect() throws Exception
  {
    final AbstractKettleTransformationProducer producer =
        new KettleTransFromFileProducer(FILE_NAME, STEP_NAME);


    Map<String, String> params = new HashMap<String, String>();
    params.put("name", QUERY_NAME);
    params.put("filename", FILE_NAME);
    params.put("step", STEP_NAME);

    testAbstractKettleTransformationProducerWritesCorrect(producer, params, "data:query-file");
  }

  private void checkParsing(Map<String, String> params, String tagName) throws Exception
  {
    File fXmlFile = new File(FILE_NAME);
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(fXmlFile);
    doc.getDocumentElement().normalize();

    NodeList nList = doc.getElementsByTagName(tagName);

    Node nNode = nList.item(0);

    if (nNode.getNodeType() == Node.ELEMENT_NODE)
    {
      Element element = (Element) nNode;

      for (Map.Entry<String, String> pair : params.entrySet())
      {
        assertEquals(pair.getValue(), element.getAttribute(pair.getKey()));
      }
    }
  }


}
