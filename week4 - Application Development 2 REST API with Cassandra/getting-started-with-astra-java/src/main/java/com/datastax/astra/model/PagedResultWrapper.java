package com.datastax.astra.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import com.datastax.oss.driver.api.core.MappedAsyncPagingIterable;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.protocol.internal.util.Bytes;

/**
 * Ease usage of the paginState.
 *
 * @author DataStax Developer Advocates team.
 */
public class PagedResultWrapper < ENTITY > {
	
	/** Custom management of paging state. */
	private Optional< String > pageState = Optional.empty();
	
	/** Custom management of paging state. */
    private Integer pageSize = 1;

    /** Results map as entities. */
    private List<ENTITY> data = new ArrayList<>();
    
    /**
	 * Default Constructor.
	 */
	public PagedResultWrapper() {}
	
	/**
     * Constructor from a RESULT.
     * 
     * @param rs
     *      result set
     * @param mapper
     *      mapper
     */
    public PagedResultWrapper(PagingIterable<ENTITY> rs, int pageSize) {
        if (null != rs) {
            Iterator<ENTITY> iterResults = rs.iterator();
            IntStream.range(0, rs.getAvailableWithoutFetching())
                     .forEach(item -> data.add(iterResults.next()));
            if (null != rs.getExecutionInfo().getPagingState()) {
                ByteBuffer pagingState = rs.getExecutionInfo().getPagingState();
                if (pagingState != null && pagingState.hasArray()) {
                    pageState = Optional.ofNullable(Bytes.toHexString(pagingState));
                }
            }
            this.pageSize = pageSize;
        }
    }
    
    public PagedResultWrapper(MappedAsyncPagingIterable<ENTITY> rs, int pageSize) {
        if (null != rs) {
           rs.currentPage().forEach(data::add);
           ByteBuffer pagingState = rs.getExecutionInfo().getPagingState();
           if (pagingState != null && pagingState.hasArray()) {
               pageState = Optional.ofNullable(Bytes.toHexString(pagingState));
           }
           this.pageSize = pageSize;
        }
    }

    /**
     * Getter accessor for attribute 'pageState'.
     *
     * @return
     *       current value of 'pageState'
     */
    public Optional<String> getPageState() {
        return pageState;
    }

    /**
     * Getter accessor for attribute 'pageSize'.
     *
     * @return
     *       current value of 'pageSize'
     */
    public Integer getPageSize() {
        return pageSize;
    }

    /**
     * Getter accessor for attribute 'data'.
     *
     * @return
     *       current value of 'data'
     */
    public List<ENTITY> getData() {
        return data;
    }

}
