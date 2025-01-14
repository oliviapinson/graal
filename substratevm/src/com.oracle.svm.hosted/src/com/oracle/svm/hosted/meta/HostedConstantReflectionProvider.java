/*
 * Copyright (c) 2014, 2017, Oracle and/or its affiliates. All rights reserved.
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
package com.oracle.svm.hosted.meta;

import java.util.function.ObjIntConsumer;

import org.graalvm.nativeimage.Platform;
import org.graalvm.nativeimage.Platforms;

import com.oracle.svm.core.graal.meta.SharedConstantReflectionProvider;
import com.oracle.svm.core.meta.SubstrateObjectConstant;
import com.oracle.svm.hosted.SVMHost;

import jdk.vm.ci.meta.Constant;
import jdk.vm.ci.meta.JavaConstant;
import jdk.vm.ci.meta.MemoryAccessProvider;
import jdk.vm.ci.meta.ResolvedJavaField;
import jdk.vm.ci.meta.ResolvedJavaType;

@Platforms(Platform.HOSTED_ONLY.class)
public class HostedConstantReflectionProvider extends SharedConstantReflectionProvider {
    private final SVMHost hostVM;
    private final HostedUniverse universe;
    private final HostedMemoryAccessProvider memoryAccess;

    public HostedConstantReflectionProvider(SVMHost hostVM, HostedUniverse universe, HostedMemoryAccessProvider memoryAccess) {
        this.hostVM = hostVM;
        this.universe = universe;
        this.memoryAccess = memoryAccess;
    }

    @Override
    public Boolean constantEquals(Constant x, Constant y) {
        /* Delegate to the AnalysisConstantReflectionProvider. */
        return universe.getConstantReflectionProvider().constantEquals(x, y);
    }

    @Override
    public MemoryAccessProvider getMemoryAccessProvider() {
        return memoryAccess;
    }

    @Override
    public JavaConstant boxPrimitive(JavaConstant source) {
        /* Delegate to the AnalysisConstantReflectionProvider. */
        return universe.getConstantReflectionProvider().boxPrimitive(source);
    }

    @Override
    public JavaConstant unboxPrimitive(JavaConstant source) {
        /* Delegate to the AnalysisConstantReflectionProvider. */
        return universe.getConstantReflectionProvider().unboxPrimitive(source);
    }

    @Override
    public ResolvedJavaType asJavaType(Constant constant) {
        /* Delegate to the AnalysisConstantReflectionProvider. */
        return universe.lookup(universe.getConstantReflectionProvider().asJavaType(constant));
    }

    @Override
    public JavaConstant asJavaClass(ResolvedJavaType type) {
        return SubstrateObjectConstant.forObject(hostVM.dynamicHub(((HostedType) type).wrapped));
    }

    @Override
    public Integer readArrayLength(JavaConstant array) {
        /* Delegate to the AnalysisConstantReflectionProvider. */
        return universe.getConstantReflectionProvider().readArrayLength(array);
    }

    @Override
    public JavaConstant readArrayElement(JavaConstant array, int index) {
        /* Delegate to the AnalysisConstantReflectionProvider. */
        return universe.getConstantReflectionProvider().readArrayElement(array, index);
    }

    @Override
    public void forEachArrayElement(JavaConstant array, ObjIntConsumer<JavaConstant> consumer) {
        /* Delegate to the AnalysisConstantReflectionProvider. */
        universe.getConstantReflectionProvider().forEachArrayElement(array, consumer);
    }

    @Override
    public JavaConstant readFieldValue(ResolvedJavaField field, JavaConstant receiver) {
        return ((HostedField) field).readValue(receiver);
    }
}
