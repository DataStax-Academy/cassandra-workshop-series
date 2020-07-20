package com.killrvideo.grpc.utils;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

import com.google.protobuf.Timestamp;

import killrvideo.common.CommonTypes.TimeUuid;
import killrvideo.common.CommonTypes.Uuid;

/**
 * Convert Grpc Attribute to common types.
 *
 * @author DataStax evangelist team.
 */
public class GrpcMapper {

    /**
     * Conversion.
     */
    public static Timestamp instantToTimeStamp(Instant instant) {
        return Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).setNanos(instant.getNano()).build();
    }

    /**
     * Conversion.
     */
    public static Timestamp epochTimeToTimeStamp(long epoch) {
        return Timestamp.newBuilder().setSeconds(epoch).build();
    }

    /**
     * Conversion.
     */
    public static Timestamp dateToTimestamp(Date date) {
        return instantToTimeStamp(date.toInstant());
    }

    /**
     * Conversion.
     */
    public static Date dateFromTimestamp(Timestamp timestamp) {
        return Date.from(Instant.ofEpochSecond(timestamp.getSeconds()));
    }

    /**
     * Conversion.
     */
    public static TimeUuid uuidToTimeUuid(UUID uuid) {
        return TimeUuid.newBuilder().setValue(uuid.toString()).build();
    }

    /**
     * Conversion.
     */
    public static Uuid uuidToUuid(UUID uuid) {
        return Uuid.newBuilder().setValue(uuid.toString()).build();
    }
}
