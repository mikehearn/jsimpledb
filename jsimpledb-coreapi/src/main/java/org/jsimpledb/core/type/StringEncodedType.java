
/*
 * Copyright (C) 2015 Archie L. Cobbs. All rights reserved.
 */

package org.jsimpledb.core.type;

import com.google.common.base.Converter;
import com.google.common.reflect.TypeToken;

/**
 * A {@link org.jsimpledb.core.FieldType} implementation for any Java type that can be encoded uniquely as a {@link String}.
 * A {@link Converter} is used to convert between native and {@link String} forms.
 *
 * <p>
 * This class provides a convenient way to implement custom {@link org.jsimpledb.core.FieldType}s.
 * Null values are supported and null is the default value. This type will sort instances according to
 * the lexicographical sort order of their {@link String} encodings; null will sort last.
 *
 * <p>
 * The supplied {@link Converter} must be {@link java.io.Serializable} in order for an instance of this
 * class to also be {@link java.io.Serializable}.
 *
 * @param <T> The associated Java type
 */
public class StringEncodedType<T> extends NullSafeType<T> {

    private static final long serialVersionUID = 6224434959455483181L;

    /**
     * Convenience constructor. Uses the name of the {@code type} as this {@link org.jsimpledb.core.FieldType}'s type name.
     *
     * @param type represented Java type
     * @param signature binary encoding signature (in this case, {@link String} encoding signature)
     * @param converter converts between native form and {@link String} form; should be {@link java.io.Serializable}
     * @throws IllegalArgumentException if {@code converter} does not convert null to null
     * @throws IllegalArgumentException if any parameter is null
     */
    public StringEncodedType(Class<T> type, long signature, Converter<T, String> converter) {
        this(type.getName(), type, signature, converter);
    }

    /**
     * Primary constructor.
     *
     * @param name the name for this {@link org.jsimpledb.core.FieldType}
     * @param type represented Java type
     * @param signature binary encoding signature (in this case, {@link String} encoding signature)
     * @param converter converts between native form and {@link String} form; should be {@link java.io.Serializable}
     * @throws IllegalArgumentException if {@code converter} does not convert null to null
     * @throws IllegalArgumentException if any parameter is null
     */
    public StringEncodedType(String name, Class<T> type, long signature, Converter<T, String> converter) {
        super(new StringConvertedType<T>(name, TypeToken.of(type), signature, converter));
    }
}

