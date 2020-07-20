package com.killrvideo.dse.utils;

import java.util.Date;

import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.extras.codecs.MappingCodec;

/**
 * Column expect a blob, attribute is a String, we need a codec here for conversion.
 * 
 * In CQL you would be able to use textAsBlob().
 * 
 * @author DataStax evangelist team.
 */
public class LongToTimeStampCodec extends MappingCodec<Long, Date> {
    
    /**
     * Default charset will be UTF8.
     */
    public LongToTimeStampCodec() {
        super(TypeCodec.timestamp(), Long.class);
    }

    /** {@inheritDoc} */
    @Override
    protected Long deserialize(Date value) {
        return value.getTime();
    }

    /** {@inheritDoc} */
    @Override
    protected Date serialize(Long value) {
        return new Date(value);
    }
    
}