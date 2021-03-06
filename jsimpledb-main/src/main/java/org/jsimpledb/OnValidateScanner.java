
/*
 * Copyright (C) 2015 Archie L. Cobbs. All rights reserved.
 */

package org.jsimpledb;

import java.lang.reflect.Method;

import org.jsimpledb.annotation.OnValidate;

/**
 * Scans for {@link OnValidate &#64;OnValidate} annotations.
 */
class OnValidateScanner<T> extends AnnotationScanner<T, OnValidate> {

    OnValidateScanner(JClass<T> jclass) {
        super(jclass, OnValidate.class);
    }

    @Override
    protected boolean includeMethod(Method method, OnValidate annotation) {
        this.checkNotStatic(method);
        this.checkReturnType(method, void.class);
        this.checkParameterTypes(method);
        return true;
    }
}

