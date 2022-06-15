/*
 * Copyright (c) 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.graalvm.bisect.core.optimization;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.graalvm.bisect.util.Writer;

public class OptimizationImpl implements Optimization {
    private final int bci;
    private final String optimizationName;
    private final String eventName;
    private final Map<String, Object> properties;

    public OptimizationImpl(String optimizationName, String eventName, int bci, Map<String, Object> properties) {
        this.optimizationName = optimizationName;
        this.eventName = eventName;
        this.bci = bci;
        this.properties = properties;
    }

    @Override
    public String getOptimizationName() {
        return optimizationName;
    }

    /**
     * Gets the name of the event that occurred. Compared to {@link #getOptimizationName()}, it
     * should return a more specific description of the optimization.
     * 
     * @return the name of the event that occurred
     */
    @Override
    public String getName() {
        return eventName;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public int getBCI() {
        return bci;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getOptimizationName()).append(" ").append(getName()).append(" at bci ").append(getBCI());
        if (properties.isEmpty()) {
            return sb.toString();
        }
        sb.append(" {");
        boolean first = true;
        for (Map.Entry<String, Object> entry : properties.entrySet()) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(entry.getKey()).append(": ").append(entry.getValue());
        }
        sb.append('}');
        return sb.toString();
    }

    @Override
    public void writeHead(Writer writer) {
        writer.writeln(toString());
    }

    @Override
    public int hashCode() {
        int result = bci;
        result = 31 * result + optimizationName.hashCode();
        result = 31 * result + eventName.hashCode();
        result = 31 * result + (properties != null ? properties.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof OptimizationImpl)) {
            return false;
        }
        OptimizationImpl other = (OptimizationImpl) object;
        return bci == other.bci && optimizationName.equals(other.optimizationName) &&
                eventName.equals(other.eventName) && Objects.equals(properties, other.properties);
    }

    @Override
    public List<OptimizationTreeNode> getChildren() {
        return List.of();
    }
}
