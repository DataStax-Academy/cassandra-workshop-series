package com.killrvideo.dse.utils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.extras.codecs.MappingCodec;

/**
 * Column expect a blob, attribute is a String, we need a codec here for conversion.
 * 
 * In CQL you would be able to use textAsBlob().
 * 
 * @author DataStax evangelist team.
 */
public class BlobToStringCodec extends MappingCodec<String, ByteBuffer> {
    
    /** Working woth Charset UTF */
    private Charset plarformCharset;
    
    /**
     * Default charset will be UTF8.
     */
    public BlobToStringCodec() {
        this(StandardCharsets.UTF_8);
    }
    
    /**
     * Default construcot.
     */
    public BlobToStringCodec(Charset charset) { 
        super(TypeCodec.blob(), String.class);
        this.plarformCharset = charset;
    }

    /** {@inheritDoc} */
    @Override
    protected ByteBuffer serialize(String str) { 
        return ByteBuffer.wrap(str.getBytes(plarformCharset)); 
    }

    /** {@inheritDoc} */
    @Override
    protected String deserialize(ByteBuffer byteBuffer) { 
        return plarformCharset.decode(byteBuffer).toString();
    }
}