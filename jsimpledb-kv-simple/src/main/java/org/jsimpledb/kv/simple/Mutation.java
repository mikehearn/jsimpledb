
/*
 * Copyright (C) 2015 Archie L. Cobbs. All rights reserved.
 */

package org.jsimpledb.kv.simple;

import com.google.common.base.Preconditions;

import java.util.Arrays;

import org.jsimpledb.kv.KVStore;
import org.jsimpledb.kv.KeyRange;
import org.jsimpledb.kv.util.KeyWatchTracker;

/**
 * Represents an outstanding {@link SimpleKVTransaction} mutation.
 */
abstract class Mutation extends KeyRange {

    protected Mutation(byte[] min, byte[] max) {
        super(min, max);
        Preconditions.checkArgument(max == null || !Arrays.equals(min, max), "empty range");
    }

    protected Mutation(byte[] value) {
        super(value);
    }

    public abstract void apply(KVStore kv);

    public boolean trigger(KeyWatchTracker keyWatchTracker) {
        return keyWatchTracker.trigger(this);
    }

    public static Mutation key(byte[] value) {
        return new Mutation(value) {
            @Override
            public void apply(KVStore kv) {
                throw new UnsupportedOperationException();
            }
        };
    }
}

