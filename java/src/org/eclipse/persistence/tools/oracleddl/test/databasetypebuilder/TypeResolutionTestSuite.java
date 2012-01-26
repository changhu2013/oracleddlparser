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
 *     Mike Norman - June 10 2011, created DDL parser package
 *     David McCann - July 2011, visit tests
 ******************************************************************************/
package org.eclipse.persistence.tools.oracleddl.test.databasetypebuilder;

//javase imports
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

//JUnit4 imports
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

//DDL imports
import org.eclipse.persistence.tools.oracleddl.metadata.ArgumentType;
import org.eclipse.persistence.tools.oracleddl.metadata.DatabaseType;
import org.eclipse.persistence.tools.oracleddl.metadata.FunctionType;
import org.eclipse.persistence.tools.oracleddl.metadata.PLSQLPackageType;
import org.eclipse.persistence.tools.oracleddl.metadata.PLSQLRecordType;
import org.eclipse.persistence.tools.oracleddl.metadata.ProcedureType;
import org.eclipse.persistence.tools.oracleddl.metadata.visit.UnresolvedTypesVisitor;
import org.eclipse.persistence.tools.oracleddl.parser.ParseException;
import org.eclipse.persistence.tools.oracleddl.test.AllTests;
import org.eclipse.persistence.tools.oracleddl.util.DatabaseTypeBuilder;

//testing imports
import static org.eclipse.persistence.tools.oracleddl.test.TestHelper.DATABASE_DDL_KEY;
import static org.eclipse.persistence.tools.oracleddl.test.TestHelper.DEFAULT_DATABASE_DDL;
import static org.eclipse.persistence.tools.oracleddl.test.TestHelper.buildConnection;
import static org.eclipse.persistence.tools.oracleddl.test.TestHelper.createDbArtifact;

public class TypeResolutionTestSuite {

    static final String CREATE_TESTMAN_TYPE1 =
        "CREATE OR REPLACE TYPE TESMAN_TYPE1 AS OBJECT (" +
        	"\n\tACCT\tNUMBER," +
            "\n\tCOUNTRY\tVARCHAR2(30)," +
            "\n\tADDR_DIVISION\tVARCHAR2(30)," +
            "\n\tSTATE\tVARCHAR2(30)" +
        ")";
    static final String CREATE_TESTMAN_TYPE2 =
        "CREATE OR REPLACE TYPE TESMAN_TYPE2 AS OBJECT (" +
            "\n\tPNR\tNUMBER," +
            "\n\tCOMPANY\tVARCHAR2(30)," +
            "\n\tSE\tVARCHAR2(30)," +
            "\n\tSCRIP\tVARCHAR2(30)," +
            "\n\tTT\tTESMAN_TYPE1" +
        ")";
    static final String CREATE_TESTMAN_TYPE3 =
        "CREATE OR REPLACE TYPE TESMAN_TYPE3 AS VARRAY(2) OF TESMAN_TYPE2";
    static final String CREATE_TESTMAN_TABLE1 =
        "CREATE OR REPLACE TABLE TESMAN_TABLE1 (" +
            "\n\tIDE\tNUMBER," +
            "\n\tIDTT\tTESMAN_TYPE1" +
        ")";
    static final String CREATE_TESTMAN_TABLE2 =
        "CREATE OR REPLACE TABLE TESMAN_TABLE2 (" +
            "\n\tSRNO\tNUMBER," +
            "\n\tDETAIL\tTESMAN_TYPE2" +
        ")";
    static final String CREATE_TESTMAN_TABLE3 =
        "CREATE OR REPLACE TABLE TESMAN_TABLE3 (" +
            "\n\tID\tINTEGER," +
            "\n\tTT3\tTESMAN_TYPE3" +
            ")";
    static final String TESMANPACK_PACKAGE = "TESMANPACK";
    static final String CREATE_TESTMAN_PACKAGE =
        "CREATE OR REPLACE PACKAGE " + TESMANPACK_PACKAGE + " AS" +
            "\n\tTYPE EMPREC IS RECORD ( " +
                "\n\tEMPNO EMP.EMPNO%TYPE," +
                "\n\tENAME EMP.ENAME%TYPE," +
                "\n\tJOB EMP.JOB%TYPE," +
                "\n\tMGR EMP.MGR%TYPE," +
                "\n\tHIREDATE EMP.HIREDATE%TYPE," +
                "\n\tSAL EMP.SAL%TYPE," +
                "\n\tCOMM EMP.COMM%TYPE," +
                "\n\tDEPTNO EMP.DEPTNO%TYPE" +
            "\n\t);" +
            "\n\tFUNCTION TESMANFUNC17(PARAM1 IN INTEGER) RETURN TESMAN_TABLE2%ROWTYPE;" +
            "\n\tPROCEDURE TESMANPROC17(PARAM1 IN INTEGER, REC OUT TESMAN_TABLE2%ROWTYPE);" +
            "\n\tPROCEDURE TESMANPROC17b(OLDREC IN TESMAN_TABLE3%ROWTYPE, NEWREC OUT TESMAN_TABLE3%ROWTYPE);" +
            "\n\tPROCEDURE EMP_TEST(E1 IN EMPREC, NAME IN VARCHAR2);" +
            "\n\tPROCEDURE EMP_TEST2(NAME IN EMP.ENAME%TYPE);" +
        "END " + TESMANPACK_PACKAGE + ";";
    static final String CREATE_TESTMAN_PACKAGE_BODY =
        "CREATE OR REPLACE PACKAGE BODY " + TESMANPACK_PACKAGE + " AS" +
            "\n\tFUNCTION TESMANFUNC17(PARAM1 IN INTEGER) RETURN TESMAN_TABLE2%ROWTYPE AS" +
                "\n\tL_DATA1 TESMAN_TABLE2%ROWTYPE;" +
                "\n\tCURSOR C_EMP(PARAMTEMP IN INTEGER) IS SELECT * FROM TESMAN_TABLE2 TE WHERE TE.SRNO=PARAMTEMP;" +
            "\n\tBEGIN" +
                "\n\tOPEN C_EMP(PARAM1);" +
                "\n\tLOOP" +
                    "\n\tFETCH C_EMP INTO L_DATA1;" +
                    "\n\tEXIT WHEN C_EMP%NOTFOUND;" +
                "\n\tEND LOOP;" +
                "\n\tRETURN L_DATA1;" +
            "\n\tEND;" +
            "\n\tPROCEDURE TESMANPROC17( PARAM1 IN INTEGER, REC OUT TESMAN_TABLE2%ROWTYPE) AS" +
            "\n\tBEGIN" +
                "\n\tREC := TESMANFUNC17(PARAM1);" +
            "\n\tEND;" +
            "\n\tPROCEDURE TESMANPROC17b(OLDREC IN TESMAN_TABLE3%ROWTYPE, NEWREC OUT TESMAN_TABLE3%ROWTYPE) AS" +
            "\n\tBEGIN" +
                "\n\tNEWREC := OLDREC;" +
            "\n\tEND;" +
            "\n\tPROCEDURE EMP_TEST(E1 IN EMPREC, NAME IN VARCHAR2) AS" +
            "\n\tBEGIN" +
                "\n\tnull;" +
            "\n\tEND EMP_TEST;" +
            "\n\tPROCEDURE EMP_TEST2(NAME IN EMP.ENAME%TYPE) AS" +
            "\n\tBEGIN" +
                "\n\tnull;" +
            "\n\tEND EMP_TEST2;" +
        "END " + TESMANPACK_PACKAGE + ";";

    //JUnit fixture(s)
    static DatabaseTypeBuilder dtBuilder = DatabaseTypeBuilderTestSuite.dtBuilder;
    static Connection conn = AllTests.conn;
    static PLSQLPackageType tesmanPackage = null;

	@BeforeClass
	static public void setUp() {
        try {
            conn = buildConnection();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        String ddl = System.getProperty(DATABASE_DDL_KEY, DEFAULT_DATABASE_DDL);
        if ("true".equalsIgnoreCase(ddl)) {
            try {
                createDbArtifact(conn, CREATE_TESTMAN_TYPE1);
            }
            catch (SQLException e) {
                //ignore
            }
            try {
                createDbArtifact(conn, CREATE_TESTMAN_TYPE2);
            }
            catch (SQLException e) {
                //ignore
            }
            try {
                createDbArtifact(conn, CREATE_TESTMAN_TYPE3);
            }
            catch (SQLException e) {
                //ignore
            }
            try {
                createDbArtifact(conn, CREATE_TESTMAN_TABLE1);
            }
            catch (SQLException e) {
                //ignore
            }
            try {
                createDbArtifact(conn, CREATE_TESTMAN_TABLE2);
            }
            catch (SQLException e) {
                //ignore
            }
            try {
                createDbArtifact(conn, CREATE_TESTMAN_TABLE3);
            }
            catch (SQLException e) {
                //ignore
            }
            try {
                createDbArtifact(conn, CREATE_TESTMAN_PACKAGE);
            }
            catch (SQLException e) {
                //ignore
            }
            try {
                createDbArtifact(conn, CREATE_TESTMAN_PACKAGE_BODY);
            }
            catch (SQLException e) {
                //ignore
            }
        }
        dtBuilder = new DatabaseTypeBuilder();
        boolean worked = true;
        String msg = null;
        try {
            tesmanPackage = dtBuilder.buildPackages(conn, null, TESMANPACK_PACKAGE).get(0);
        }
        catch (Exception e) {
            worked = false;
            msg = e.getMessage();
        }
        if (!worked) {
            fail(msg);
        }
	}

    @Test
    public void testUnresolvedTypeResolution() throws ParseException {
        assertEquals("incorrect procedure name", TESMANPACK_PACKAGE , tesmanPackage.getPackageName());
        UnresolvedTypesVisitor visitor = new UnresolvedTypesVisitor();
        visitor.visit(tesmanPackage);
        assertEquals(TESMANPACK_PACKAGE + " should not have any unresolved types",
            0, visitor.getUnresolvedTypes().size());

        FunctionType func1 = (FunctionType)tesmanPackage.getProcedures().get(0);
        DatabaseType tesmanfunc17ReturnType = func1.getReturnArgument().getDataType();
        ProcedureType proc2 = tesmanPackage.getProcedures().get(1);
        DatabaseType tesmanproc17OutArgType = proc2.getArguments().get(1).getDataType();
        assertSame(tesmanfunc17ReturnType, tesmanproc17OutArgType);

        ProcedureType proc3 = tesmanPackage.getProcedures().get(2);
        List<ArgumentType> proc3Args = proc3.getArguments();
        DatabaseType oldrecDatabaseType = proc3Args.get(0).getDataType();
        DatabaseType newrecDatabaseType = proc3Args.get(1).getDataType();
        assertSame(oldrecDatabaseType, newrecDatabaseType);

        PLSQLRecordType empRecType = (PLSQLRecordType)tesmanPackage.getTypes().get(0);
        PLSQLRecordType empRecType2 = (PLSQLRecordType)tesmanPackage.getProcedures().get(3).
            getArguments().get(0).getDataType();
        assertSame(empRecType, empRecType2);

        DatabaseType empDotEnamePcentTYPE1 = empRecType.getFields().get(1).getDataType();
        ArgumentType nameArg = tesmanPackage.getProcedures().get(4).getArguments().get(0);
        DatabaseType empDotEnamePcentTYPE2 = nameArg.getDataType();
        assertSame(empDotEnamePcentTYPE1, empDotEnamePcentTYPE2);
    }
}