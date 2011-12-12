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

//DDL parser imports
import org.eclipse.persistence.tools.oracleddl.metadata.visit.DatabaseTypeVisitable;
import org.eclipse.persistence.tools.oracleddl.metadata.visit.DatabaseTypeVisitor;

public class TYPEType implements CompositeDatabaseType, DatabaseTypeVisitable {

    protected String typeName;
    protected DatabaseType enclosedType;

    public TYPEType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
		return typeName;
    }

	public void addCompositeType(DatabaseType enclosedType) {
		this.enclosedType = enclosedType;
	}
    public DatabaseType getEnclosedType() {
        return enclosedType;
    }

    public boolean isComposite() {
        return true;
    }

    public boolean isResolved() {
        // if enclosedType is unresolved, then this argument is unresolved
        if (enclosedType == null) {
            return false;
        }
        return enclosedType.isResolved();
    }

    public String shortName() {
        StringBuilder sb = new StringBuilder(typeName);
        if (!enclosedType.isResolved()) {
            sb.append("[u]");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (typeName != null) {
            sb.append(typeName);
        }
        if (enclosedType == null) {
            sb.append("<null/>");
        }
        else if (!enclosedType.isResolved()) {
            sb.append("[u]");
        }
        return sb.toString();
    }

	public void accept(DatabaseTypeVisitor visitor) {
		visitor.visit(this);
	}
}