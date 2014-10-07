
/*
 * Copyright (C) 2014 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package org.jsimpledb.core;

import java.util.Comparator;

/**
 * Class that holds information about the {@link SchemaItem}s associated with a storage ID,
 * independent of any specific schema version. This is exactly the information that must
 * be consistent across schema versions.
 *
 * @see #verifySharedStorageId verifySharedStorageId()
 */
abstract class StorageInfo {

    public static final Comparator<StorageInfo> SORT_BY_STORAGE_ID = new Comparator<StorageInfo>() {
        @Override
        public int compare(StorageInfo info1, StorageInfo info2) {
            return Integer.compare(info1.getStorageId(), info2.getStorageId());
        }
    };

    final int storageId;

    StorageInfo(int storageId) {
        if (storageId <= 0)
            throw new IllegalArgumentException("invalid storageId " + storageId);
        this.storageId = storageId;
    }

    /**
     * Get the storage ID associated with this instance.
     *
     * @return storage ID, always greater than zero
     */
    public int getStorageId() {
        return this.storageId;
    }

    /**
     * Compare for compatability across schema versions.
     *
     * <p>
     * Subclasses must override as required.
     * </p>
     *
     * @param that other instance
     * @throws IllegalArgumentException if this instance and {@code that} cannot use the same storage ID
     */
    public void verifySharedStorageId(StorageInfo that) {
        if (this.getClass() != that.getClass()) {
            throw new IllegalArgumentException("types " + this.getClass().getName()
              + " and " + that.getClass().getName() + " are not compatible");
        }
        if (this.storageId != that.storageId)
            throw new IllegalArgumentException("storage ID " + this.storageId + " != " + that.storageId);
    }

    @Override
    public abstract String toString();
}

