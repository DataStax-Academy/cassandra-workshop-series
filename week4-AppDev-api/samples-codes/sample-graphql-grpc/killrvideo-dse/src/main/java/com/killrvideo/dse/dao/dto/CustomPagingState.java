package com.killrvideo.dse.dao.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

/**
 * Entity handling pagination.
 *
 * @author DataStax evangelist team.
 */
public class CustomPagingState implements Serializable {

    /** Serial. */
    private static final long serialVersionUID = 8160171855827276965L;
    
    /**  Constants. */
    public static final Pattern PARSE_LATEST_PAGING_STATE = Pattern.compile("((?:[0-9]{8}_){7}[0-9]{8}),([0-9]),(.*)");
   
    /** List of Buckets. */
    private List<String> listOfBuckets = new ArrayList<>();
    
    /** Current Bucket. */
    private int currentBucket = 0;
    
    /** Paging. */
    private String cassandraPagingState;
    
    /** Default constructor. */
    public CustomPagingState() {}
    
    /**
     * Full fledge constructor.
     * 
     * @param currentBucket
     *      current offset
     * @param pagingState
     *      state
     * @param buckets
     *      set of buckets
     */
    public CustomPagingState(int currentBucket, String pagingState, String ...buckets) {
        this.cassandraPagingState = pagingState;
        this.currentBucket = currentBucket;
        listOfBuckets.addAll(Arrays.asList(buckets));
    }
    
    /**
     * Map Paging State.
     *
     * @param customPagingStateString
     *      current paging state.
     * @return
     *      current pageing state
     */
    public static Optional<CustomPagingState> parse(Optional<String> customPagingStateString) {
        CustomPagingState pagingState = null;
        if (customPagingStateString.isPresent()) {
            Matcher matcher = PARSE_LATEST_PAGING_STATE.matcher(customPagingStateString.get());
            if (matcher.matches()) {
                pagingState = new CustomPagingState()
                        .cassandraPagingState( matcher.group(3))
                        .currentBucket(Integer.parseInt(matcher.group(2)))
                        .listOfBuckets(Lists.newArrayList(matcher.group(1).split("_")));
            }
        }
        return Optional.ofNullable(pagingState);
    }
    
    /** {@inheritDoc} */
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"currentBucket\":").append(currentBucket).append(",");
        sb.append("\"cassandraPagingState\":").append("\"" + cassandraPagingState).append("\",");
        sb.append("\"listOfBuckets\":[");
        boolean first = true;
        for (String bucket : listOfBuckets) {
            if (!first) {
                sb.append(",");
            }
            sb.append("\"" + bucket + "\"");
            first = false;
        }
        return sb.append("}").toString();
    }
   
    /**
     * Syntaxic sugar.
     *
     * @param bucketName
     *          current bucketName
     */
    public void addBucket(String bucketName) {
        listOfBuckets.add(bucketName);
    }
    
    /**
     * Getter for attribute 'currentBucket'.
     *
     * @return
     *       current value of 'currentBucket'
     */
    public int getCurrentBucket() {
        return currentBucket;
    }

    /**
     * Setter for attribute 'currentBucket'.
     * @param currentBucket
     * 		new value for 'currentBucket '
     */
    public void setCurrentBucket(int currentBucket) {
        this.currentBucket = currentBucket;
    }

    /**
     * Getter for attribute 'cassandraPagingState'.
     *
     * @return
     *       current value of 'cassandraPagingState'
     */
    public String getCassandraPagingState() {
        return cassandraPagingState;
    }

    /**
     * Setter for attribute 'cassandraPagingState'.
     * @param cassandraPagingState
     * 		new value for 'cassandraPagingState '
     */
    public void setCassandraPagingState(String cassandraPagingState) {
        this.cassandraPagingState = cassandraPagingState;
    }

    /**
     * Getter for attribute 'listOfBuckets'.
     *
     * @return
     *       current value of 'listOfBuckets'
     */
    public List<String> getListOfBuckets() {
        return listOfBuckets;
    }
    
    /**
     * Get size of bucket list.
     */
    public int getListOfBucketsSize() {
        return getListOfBuckets().size();
    }

    /**
     * Setter for attribute 'listOfBuckets'.
     * @param listOfBuckets
     * 		new value for 'listOfBuckets '
     */
    public void setListOfBuckets(List<String> listOfBuckets) {
        this.listOfBuckets = listOfBuckets;
    }
    
    /**
     * Builder pattern.
     * 
     * @param cassandraPagingState
     *      last state
     * @return
     *      current object reference
     */
    public CustomPagingState cassandraPagingState(String cassandraPagingState) {
        setCassandraPagingState(cassandraPagingState);
        return this;
    }
    
    /**
     * Builder pattern.
     * 
     * @param cassandraPagingState
     *      last state
     * @return
     *      current object reference
     */
    public CustomPagingState listOfBuckets(List<String> listOfBuckets) {
        setListOfBuckets(listOfBuckets);
        return this;
    }
    
    /**
     * Builder pattern.
     * 
     * @param cassandraPagingState
     *      last state
     * @return
     *      current object reference
     */
    public CustomPagingState currentBucket(int currentBucket) {
        setCurrentBucket(currentBucket);
        this.currentBucket = currentBucket;
        return this;
    }
    
    /**
     * Increment index.
     */
    public void incCurrentBucketIndex() {
        currentBucket++;
    }
    
    /**
     * Current bucket value.
     */
    public String getCurrentBucketValue() {
         return getListOfBuckets().get(getCurrentBucket());
    }
    
}