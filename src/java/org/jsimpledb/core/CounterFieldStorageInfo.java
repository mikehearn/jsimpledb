
/*
 * Copyright (C) 2015 Archie L. Cobbs. All rights reserved.
 */

package org.jsimpledb.core;

class CounterFieldStorageInfo extends FieldStorageInfo {

    CounterFieldStorageInfo(CounterField field) {
        super(field);
    }

    @Override
    public String toString() {
        return "counter field";
    }
}

