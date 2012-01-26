/*******************************************************************************
 * Copyright (c) 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     David McCann - Aug.08, 2011 - 2.4 - Initial implementation
 ******************************************************************************/
package org.eclipse.persistence.tools.oracleddl.test.metadata;

import static org.eclipse.persistence.tools.dbws.DBWSBuilder.NO_SESSIONS_FILENAME;
import static org.eclipse.persistence.tools.dbws.DBWSBuilder.SESSIONS_FILENAME_KEY;
import static org.eclipse.persistence.tools.dbws.XRPackager.__nullStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;

import javax.wsdl.WSDLException;

import org.eclipse.persistence.dbws.DBWSModel;
import org.eclipse.persistence.dbws.DBWSModelProject;
import org.eclipse.persistence.internal.databaseaccess.Platform;
import org.eclipse.persistence.internal.helper.ConversionManager;
import org.eclipse.persistence.internal.xr.ProjectHelper;
import org.eclipse.persistence.internal.xr.XRDynamicClassLoader;
import org.eclipse.persistence.internal.xr.XRServiceAdapter;
import org.eclipse.persistence.internal.xr.XRServiceFactory;
import org.eclipse.persistence.internal.xr.XRServiceModel;
import org.eclipse.persistence.oxm.XMLContext;
import org.eclipse.persistence.oxm.XMLUnmarshaller;
import org.eclipse.persistence.platform.xml.XMLComparer;
import org.eclipse.persistence.platform.xml.XMLParser;
import org.eclipse.persistence.platform.xml.XMLPlatform;
import org.eclipse.persistence.platform.xml.XMLPlatformFactory;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.DatasourceLogin;
import org.eclipse.persistence.sessions.Project;
import org.eclipse.persistence.sessions.factories.XMLProjectReader;
import org.eclipse.persistence.tools.dbws.DBWSBuilder;
import org.eclipse.persistence.tools.dbws.DBWSBuilderModel;
import org.eclipse.persistence.tools.dbws.DBWSBuilderModelProject;
import org.eclipse.persistence.tools.dbws.XRPackager;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TestHelper {
    // TODO:  defaults should be Oracle    
    public static final String DATABASE_USERNAME_KEY = "db.user";
    public static final String DATABASE_PASSWORD_KEY = "db.pwd";
    public static final String DATABASE_URL_KEY = "db.url";
    public static final String DATABASE_DRIVER_KEY = "db.driver";
    public static final String DATABASE_PLATFORM_KEY = "db.platform";
    public static final String DEFAULT_DATABASE_DRIVER = "oracle.jdbc.OracleDriver";
    public static final String DEFAULT_DATABASE_PLATFORM = "org.eclipse.persistence.platform.database.OraclePlatform";
    public static final String DEFAULT_DATABASE_USERNAME = "user";
    public static final String DEFAULT_DATABASE_PASSWORD = "password";
    public static final String DEFAULT_DATABASE_URL = "jdbc:oracle:thin:@ottvm028.ca.oracle.com:1521:TOPLINK";

    public static String DBWS_BUILDER_XML_USERNAME;
    public static String DBWS_BUILDER_XML_PASSWORD;
    public static String DBWS_BUILDER_XML_URL;
    public static String DBWS_BUILDER_XML_DRIVER;
    public static String DBWS_BUILDER_XML_PLATFORM;
    public static String DBWS_BUILDER_XML_MAIN;
    
    public static ByteArrayOutputStream DBWS_SERVICE_STREAM = new ByteArrayOutputStream();
    public static ByteArrayOutputStream DBWS_SCHEMA_STREAM = new ByteArrayOutputStream();
    public static ByteArrayOutputStream DBWS_SESSION_STREAM = new ByteArrayOutputStream();
    public static ByteArrayOutputStream DBWS_OR_STREAM = new ByteArrayOutputStream();
    public static ByteArrayOutputStream DBWS_OX_STREAM = new ByteArrayOutputStream();
    public static ByteArrayOutputStream DBWS_WSDL_STREAM = new ByteArrayOutputStream();

    public static XMLComparer comparer = new XMLComparer();
    public static XMLPlatform xmlPlatform = XMLPlatformFactory.getInstance().getXMLPlatform();
    public static XMLParser xmlParser = xmlPlatform.newXMLParser();
    public static DBWSBuilder builder = new DBWSBuilder();
    public static XRServiceAdapter xrService = null;

    public static void setUp() throws WSDLException {
        setUp(null);
    }

    public static void setUp(String stageDir) throws WSDLException {
        final String username = System.getProperty(DATABASE_USERNAME_KEY, DEFAULT_DATABASE_USERNAME);
        final String password = System.getProperty(DATABASE_PASSWORD_KEY, DEFAULT_DATABASE_PASSWORD);
        final String url = System.getProperty(DATABASE_URL_KEY, DEFAULT_DATABASE_URL);
        final String driver = System.getProperty(DATABASE_DRIVER_KEY, DEFAULT_DATABASE_DRIVER);
        final String platform = System.getProperty(DATABASE_PLATFORM_KEY, DEFAULT_DATABASE_PLATFORM);
        String builderString = DBWS_BUILDER_XML_USERNAME + username + DBWS_BUILDER_XML_PASSWORD + password + DBWS_BUILDER_XML_URL + url + DBWS_BUILDER_XML_DRIVER + driver + DBWS_BUILDER_XML_PLATFORM + platform + DBWS_BUILDER_XML_MAIN;
        XMLContext context = new XMLContext(new DBWSBuilderModelProject());
        XMLUnmarshaller unmarshaller = context.createUnmarshaller();
        DBWSBuilderModel builderModel = (DBWSBuilderModel) unmarshaller.unmarshal(new StringReader(builderString));
        builder.quiet = true;
        builder.setPlatformClassname(platform);
        builder.properties = builderModel.properties;
        builder.operations = builderModel.operations;
        XRPackager xrPackager = new XRPackager() {
            @Override
            public void start() {// do nothing
            }
        };
        xrPackager.setSessionsFileName(builder.getSessionsFileName());
        xrPackager.setDBWSBuilder(builder);
        builder.setPackager(xrPackager);
        if (stageDir == null) {
            builder.getProperties().put(SESSIONS_FILENAME_KEY, NO_SESSIONS_FILENAME);
            builder.build(DBWS_SCHEMA_STREAM, __nullStream, DBWS_SERVICE_STREAM, DBWS_OR_STREAM, DBWS_OX_STREAM, __nullStream, __nullStream, __nullStream, __nullStream, __nullStream, __nullStream, __nullStream, null);
        } else {
            xrPackager.setStageDir(new File(stageDir));
            builder.build(DBWS_SCHEMA_STREAM, DBWS_SESSION_STREAM, DBWS_SERVICE_STREAM, DBWS_OR_STREAM, DBWS_OX_STREAM, __nullStream, __nullStream, DBWS_WSDL_STREAM, __nullStream, __nullStream,  __nullStream, __nullStream, null);
        }
        XRServiceFactory factory = new XRServiceFactory() {
            @Override
            public XRServiceAdapter buildService(XRServiceModel xrServiceModel) {
                parentClassLoader = this.getClass().getClassLoader();
                xrSchemaStream = new ByteArrayInputStream(DBWS_SCHEMA_STREAM.toByteArray());
                return super.buildService(xrServiceModel);
            }

            @Override
            public void buildSessions() {
                XRDynamicClassLoader xrdecl = new XRDynamicClassLoader(parentClassLoader);
                Project orProject = null;
                if (DBWS_OR_STREAM.size() != 0) {
                    orProject = XMLProjectReader.read(new StringReader(DBWS_OR_STREAM.toString()), xrdecl);
                } else {
                    orProject = new Project();
                    orProject.setName(builder.getProjectName() + "-dbws-or");
                }
                Project oxProject = null;
                if (DBWS_OX_STREAM.size() != 0) {
                    oxProject = XMLProjectReader.read(new StringReader(DBWS_OX_STREAM.toString()), xrdecl);
                } else {
                    oxProject = new Project();
                    oxProject.setName(builder.getProjectName() + "-dbws-ox");
                }
                DatasourceLogin login = new DatabaseLogin();
                login.setUserName(username);
                login.setPassword(password);
                ((DatabaseLogin) login).setConnectionString(url);
                ((DatabaseLogin) login).setDriverClassName(driver);
                Platform platform = builder.getDatabasePlatform();
                ;
                ConversionManager conversionManager = platform.getConversionManager();
                if (conversionManager != null) {
                    conversionManager.setLoader(xrdecl);
                }
                login.setDatasourcePlatform(platform);
                ((DatabaseLogin) login).bindAllParameters();
                orProject.setDatasourceLogin(login);
                login = (DatasourceLogin) oxProject.getDatasourceLogin();
                if (login != null) {
                    platform = login.getDatasourcePlatform();
                    if (platform != null) {
                        conversionManager = platform.getConversionManager();
                        if (conversionManager != null) {
                            conversionManager.setLoader(xrdecl);
                        }
                    }
                }
                ProjectHelper.fixOROXAccessors(orProject, oxProject);
                xrService.setORSession(orProject.createDatabaseSession());
                xrService.getORSession().dontLogMessages();
                xrService.setXMLContext(new XMLContext(oxProject));
                xrService.setOXSession(xrService.getXMLContext().getSession(0));
            }
        };
        context = new XMLContext(new DBWSModelProject());
        unmarshaller = context.createUnmarshaller();
        DBWSModel model = (DBWSModel) unmarshaller.unmarshal(new StringReader(DBWS_SERVICE_STREAM.toString()));
        xrService = factory.buildService(model);
    }
    
    /**
     * Helper method that removes empty text nodes from a Document.
     * This is typically called prior to comparing two documents
     * for equality.
     * 
     */
    public static void removeEmptyTextNodes(Node node) {
        NodeList nodeList = node.getChildNodes();
        Node childNode;
        for (int x = nodeList.getLength() - 1; x >= 0; x--) {
            childNode = nodeList.item(x);
            if (childNode.getNodeType() == Node.TEXT_NODE) {
                if (childNode.getNodeValue().trim().equals("")) {
                    node.removeChild(childNode);
                }
            } else if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                removeEmptyTextNodes(childNode);
            }
        }
    }
}
