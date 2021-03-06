
/*
 * Copyright (C) 2015 Archie L. Cobbs. All rights reserved.
 */

package org.jsimpledb.jsck;

import org.jsimpledb.core.FieldType;
import org.jsimpledb.core.FieldTypeRegistry;
import org.jsimpledb.core.ObjId;
import org.jsimpledb.schema.SimpleSchemaField;
import org.jsimpledb.util.ByteReader;

abstract class SimpleIndex extends Index {

    protected final FieldType<?> type;

    SimpleIndex(JsckInfo info, int schemaVersion, SimpleSchemaField field) {
        super(info, field.getStorageId());
        assert field.isIndexed();
        this.type = this.info.findFieldType(schemaVersion, field).genericizeForIndex();
    }

    @Override
    public boolean isCompatible(Storage that) {
        if (!super.isCompatible(that))
            return false;
        if (!this.type.equals(((SimpleIndex)that).type))
            return false;
        return true;
    }

    @Override
    protected final void validateIndexEntryContent(JsckInfo info, ByteReader reader) {

        // Decode indexed value
        final byte[] value = this.validateEncodedBytes(reader, this.type);

        // Decode object ID
        final ObjId id = this.validateEncodedValue(reader, FieldTypeRegistry.OBJ_ID);

        // Validate object exists
        this.validateObjectExists(info, reader, id);

        // Proceed with subclass
        this.validateIndexEntrySuffix(info, reader, value, id);
    }

    /**
     * Validate the index entry content following the object ID (if any) and confirm that the entry agrees with
     * the content of the corresponding object. This assumes the object exists and has already been validated.
     *
     * @throws IllegalArgumentException if entry is invalid
     */
    protected abstract void validateIndexEntrySuffix(JsckInfo info, ByteReader reader, byte[] indexValue, ObjId id);
}

