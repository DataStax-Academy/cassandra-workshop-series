package com.killrvideo.graphql.domain;

import java.util.ArrayList;
import java.util.List;

public class ResultPageCommentGQL {

    private List < CommentGQL > listOfResults = new ArrayList<>();
    
    private String nextPage;

    /**
     * Getter accessor for attribute 'listOfResults'.
     *
     * @return
     *       current value of 'listOfResults'
     */
    public List<CommentGQL> getListOfResults() {
        return listOfResults;
    }

    /**
     * Setter accessor for attribute 'listOfResults'.
     * @param listOfResults
     * 		new value for 'listOfResults '
     */
    public void setListOfResults(List<CommentGQL> listOfResults) {
        this.listOfResults = listOfResults;
    }

    /**
     * Getter accessor for attribute 'nextPage'.
     *
     * @return
     *       current value of 'nextPage'
     */
    public String getNextPage() {
        return nextPage;
    }

    /**
     * Setter accessor for attribute 'nextPage'.
     * @param nextPage
     * 		new value for 'nextPage '
     */
    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
}
