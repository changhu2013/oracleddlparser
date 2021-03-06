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
package org.eclipse.persistence.tools.oracleddl.metadata.visit;

//javase imports
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//DDL parser imports
import org.eclipse.persistence.tools.oracleddl.metadata.UnresolvedSizedType;
import org.eclipse.persistence.tools.oracleddl.metadata.UnresolvedType;

public class UnresolvedTypesVisitor extends BaseDatabaseTypeVisitor {

    protected List<UnresolvedType> unresolvedTypes = new ArrayList<UnresolvedType>();
    protected Map<String, List<UnresolvedType>> uniq = new HashMap<String, List<UnresolvedType>>();

    public List<UnresolvedType> getUnresolvedTypes() {
        return unresolvedTypes;
    }

    public void visit(UnresolvedType unresolvedType) {
        String typeName = unresolvedType.getTypeName();
        List<UnresolvedType> similarUnresolvedTypes = uniq.get(typeName);
        if (similarUnresolvedTypes == null) {
            similarUnresolvedTypes = new ArrayList<UnresolvedType>();
            uniq.put(typeName, similarUnresolvedTypes);
        }
        boolean addToUnresolvedTypes = false;
        for (UnresolvedType similarUnresolvedType : similarUnresolvedTypes) {
            if (unresolvedType.getOwningType() != similarUnresolvedType.getOwningType()) {
                addToUnresolvedTypes = true;
                break;
            }
        }
        if (addToUnresolvedTypes || similarUnresolvedTypes.isEmpty()) {
            similarUnresolvedTypes.add(unresolvedType);
            unresolvedTypes.add(unresolvedType);
        }
    }

    public void visit(UnresolvedSizedType unresolvedType) {
        visit((UnresolvedType)unresolvedType);
    }
}