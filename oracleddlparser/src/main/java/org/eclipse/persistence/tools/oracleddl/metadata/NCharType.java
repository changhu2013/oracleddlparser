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
 ******************************************************************************/
package org.eclipse.persistence.tools.oracleddl.metadata;

import org.eclipse.persistence.tools.oracleddl.metadata.visit.DatabaseTypeVisitor;

public class NCharType extends CharType {

	public static final String TYPENAME = "NCHAR";

    public NCharType() {
        super(TYPENAME, DEFAULT_SIZE);
    }
    public NCharType(long size) {
        super(TYPENAME, size);
    }

	@Override
    public boolean isNCharType() {
        return true;
    }

    @Override
	public long getDefaultSize() {
		return DEFAULT_SIZE;
	}

	public void accept(DatabaseTypeVisitor visitor) {
		visitor.visit(this);
	}
}
