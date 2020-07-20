package com.killrvideo.dse.dao.dto;

import java.io.Serializable;
import java.util.Optional;

/**
 * Proposition of super class for search queries. Embedded search definition in an object
 * is a good practice for future evolutions. (adding fields etc.)
 * 
 * @author DataStax evangelist team.
 */
public abstract class QueryDefinition implements Serializable {
    
    /** Serial. */
    private static final long serialVersionUID = 5286278417340641649L;

    /** Constants. */
    private static final int DEFAULT_PAGE_SIZE = 10;
    
    /** Useful for pageabl aueries. */ 
    private int pageSize = DEFAULT_PAGE_SIZE;
    
    /** Optional pageState. */
    private Optional < String > pageState = Optional.empty();

    /**
     * Keep default constructor.
     */
    public QueryDefinition() {}
    
    /**
     * Initialization for pageable queries.
     *
     * @param pageSize
     *      page size
     * @param pageState
     *      page state
     */
    public QueryDefinition(int pageSize, String pageState) {
        this.pageSize  = pageSize;
        this.pageState = Optional.ofNullable(pageState);
    }
    
    /**
     * Getter for attribute 'pageSize'.
     *
     * @return
     *       current value of 'pageSize'
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * Setter for attribute 'pageSize'.
     * @param pageSize
     * 		new value for 'pageSize '
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Getter for attribute 'pageState'.
     *
     * @return
     *       current value of 'pageState'
     */
    public Optional<String> getPageState() {
        return pageState;
    }

    /**
     * Setter for attribute 'pageState'.
     * @param pageState
     * 		new value for 'pageState '
     */
    public void setPageState(Optional<String> pageState) {
        this.pageState = pageState;
    }
    

}
