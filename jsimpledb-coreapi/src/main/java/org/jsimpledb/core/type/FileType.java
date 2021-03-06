
/*
 * Copyright (C) 2015 Archie L. Cobbs. All rights reserved.
 */

package org.jsimpledb.core.type;

import com.google.common.base.Converter;

import java.io.File;
import java.io.Serializable;

/**
 * {@link File} type. Null values are supported by this class.
 */
public class FileType extends StringEncodedType<File> {

    private static final long serialVersionUID = -8784371602920299513L;

    public FileType() {
        super(File.class, 0, new FileConverter());
    }

// FileConverter

    private static class FileConverter extends Converter<File, String> implements Serializable {

        private static final long serialVersionUID = 6790052264207886810L;

        @Override
        protected String doForward(File file) {
            if (file == null)
                return null;
            return file.toString();
        }

        @Override
        protected File doBackward(String string) {
            if (string == null)
                return null;
            return new File(string);
        }
    }
}
