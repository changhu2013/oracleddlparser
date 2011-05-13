/* Generated By:JJTree&JavaCC: Do not edit this line. PLSQLParserConstants.java */
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
 *     Mike Norman - add PLSQL package spec parsing to DBWSBuilder
 ******************************************************************************/
package org.eclipse.persistence.tools.dbws.plsqlparser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface PLSQLParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int COMMENT_LINE = 6;
  /** RegularExpression Id. */
  int COMMENT_BLOCK = 7;
  /** RegularExpression Id. */
  int K_AS = 8;
  /** RegularExpression Id. */
  int K_AUTHID = 9;
  /** RegularExpression Id. */
  int K_AUTONOMOUS_TRANSACTION = 10;
  /** RegularExpression Id. */
  int K_CREATE = 11;
  /** RegularExpression Id. */
  int K_CURRENT_USER = 12;
  /** RegularExpression Id. */
  int K_CURSOR = 13;
  /** RegularExpression Id. */
  int K_DEFAULT = 14;
  /** RegularExpression Id. */
  int K_DEFINER = 15;
  /** RegularExpression Id. */
  int K_DETERMINISTIC = 16;
  /** RegularExpression Id. */
  int K_END = 17;
  /** RegularExpression Id. */
  int K_EXCEPTION_INIT = 18;
  /** RegularExpression Id. */
  int K_FUNCTION = 19;
  /** RegularExpression Id. */
  int K_INLINE = 20;
  /** RegularExpression Id. */
  int K_IS = 21;
  /** RegularExpression Id. */
  int K_OR = 22;
  /** RegularExpression Id. */
  int K_NO = 23;
  /** RegularExpression Id. */
  int K_PACKAGE = 24;
  /** RegularExpression Id. */
  int K_PARALLEL_ENABLE = 25;
  /** RegularExpression Id. */
  int K_PIPELINED = 26;
  /** RegularExpression Id. */
  int K_PRAGMA = 27;
  /** RegularExpression Id. */
  int K_PROCEDURE = 28;
  /** RegularExpression Id. */
  int K_REPLACE = 29;
  /** RegularExpression Id. */
  int K_RESTRICT_REFERENCES = 30;
  /** RegularExpression Id. */
  int K_RESULT_CACHE = 31;
  /** RegularExpression Id. */
  int K_RETURN = 32;
  /** RegularExpression Id. */
  int K_RNDS = 33;
  /** RegularExpression Id. */
  int K_RNPS = 34;
  /** RegularExpression Id. */
  int K_SERIALLY_REUSABLE = 35;
  /** RegularExpression Id. */
  int K_TRUST = 36;
  /** RegularExpression Id. */
  int K_TYPE = 37;
  /** RegularExpression Id. */
  int K_WNDS = 38;
  /** RegularExpression Id. */
  int K_WNPS = 39;
  /** RegularExpression Id. */
  int K_YES = 40;
  /** RegularExpression Id. */
  int ASSIGN = 41;
  /** RegularExpression Id. */
  int ASTERISK = 42;
  /** RegularExpression Id. */
  int CLOSEPAREN = 43;
  /** RegularExpression Id. */
  int CONCAT = 44;
  /** RegularExpression Id. */
  int COLON = 45;
  /** RegularExpression Id. */
  int DOT = 46;
  /** RegularExpression Id. */
  int EQUAL = 47;
  /** RegularExpression Id. */
  int GREATER = 48;
  /** RegularExpression Id. */
  int GREATEREQUAL = 49;
  /** RegularExpression Id. */
  int JOINPLUS = 50;
  /** RegularExpression Id. */
  int LESS = 51;
  /** RegularExpression Id. */
  int LESSEQUAL = 52;
  /** RegularExpression Id. */
  int MINUS = 53;
  /** RegularExpression Id. */
  int NOTEQUAL2 = 54;
  /** RegularExpression Id. */
  int NOTEQUAL = 55;
  /** RegularExpression Id. */
  int OPENPAREN = 56;
  /** RegularExpression Id. */
  int PLUS = 57;
  /** RegularExpression Id. */
  int QUESTIONMARK = 58;
  /** RegularExpression Id. */
  int ROWTYPE = 59;
  /** RegularExpression Id. */
  int SEMICOLON = 60;
  /** RegularExpression Id. */
  int SLASH = 61;
  /** RegularExpression Id. */
  int TILDE = 62;
  /** RegularExpression Id. */
  int TYPE = 63;
  /** RegularExpression Id. */
  int S_NUMBER = 64;
  /** RegularExpression Id. */
  int FLOAT = 65;
  /** RegularExpression Id. */
  int INTEGER = 66;
  /** RegularExpression Id. */
  int DIGIT = 67;
  /** RegularExpression Id. */
  int S_IDENTIFIER = 68;
  /** RegularExpression Id. */
  int LETTER = 69;
  /** RegularExpression Id. */
  int SPECIAL_CHARS = 70;
  /** RegularExpression Id. */
  int S_BIND = 71;
  /** RegularExpression Id. */
  int S_CHAR_LITERAL = 72;
  /** RegularExpression Id. */
  int S_QUOTED_IDENTIFIER = 73;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"\\f\"",
    "<COMMENT_LINE>",
    "<COMMENT_BLOCK>",
    "\"AS\"",
    "\"AUTHID\"",
    "\"AUTONOMOUS_TRANSACTION\"",
    "\"CREATE\"",
    "\"CURRENT_USER\"",
    "\"CURSOR\"",
    "\"DEFAULT\"",
    "\"DEFINER\"",
    "\"DETERMINISTIC\"",
    "\"END\"",
    "\"EXCEPTION_INIT\"",
    "\"FUNCTION\"",
    "\"INLINE\"",
    "\"IS\"",
    "\"OR\"",
    "\"\\\'NO\\\'\"",
    "\"PACKAGE\"",
    "\"PARALLEL_ENABLE\"",
    "\"PIPELINED\"",
    "\"PRAGMA\"",
    "\"PROCEDURE\"",
    "\"REPLACE\"",
    "\"RESTRICT_REFERENCES\"",
    "\"RESULT_CACHE\"",
    "\"RETURN\"",
    "\"RNDS\"",
    "\"RNPS\"",
    "\"SERIALLY_REUSABLE\"",
    "\"TRUST\"",
    "\"TYPE\"",
    "\"WNDS\"",
    "\"WNPS\"",
    "\"\\\'YES\\\'\"",
    "\":=\"",
    "\"*\"",
    "\")\"",
    "\"||\"",
    "\":\"",
    "\".\"",
    "\"=\"",
    "\">\"",
    "\">=\"",
    "\"(+)\"",
    "\"<\"",
    "\"<=\"",
    "\"-\"",
    "\"<>\"",
    "\"!=\"",
    "\"(\"",
    "\"+\"",
    "\"?\"",
    "\"%ROWTYPE\"",
    "\";\"",
    "\"/\"",
    "\"~\"",
    "\"%TYPE\"",
    "<S_NUMBER>",
    "<FLOAT>",
    "<INTEGER>",
    "<DIGIT>",
    "<S_IDENTIFIER>",
    "<LETTER>",
    "<SPECIAL_CHARS>",
    "<S_BIND>",
    "<S_CHAR_LITERAL>",
    "<S_QUOTED_IDENTIFIER>",
    "\",\"",
    "<token of kind 75>",
    "<token of kind 76>",
    "<token of kind 77>",
  };

}
