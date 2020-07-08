package com.datastax.workshop;

import java.time.Instant;
import java.util.UUID;

public class Journey {

    private UUID    id;
    private String  summary;
    private Instant start;
    private Instant end;
    private String  spaceCraft;
    private boolean active;
    
    public Journey() {}
    
    /**
     * Getter accessor for attribute 'id'.
     *
     * @return
     *       current value of 'id'
     */
    public UUID getId() {
        return id;
    }

    /**
     * Setter accessor for attribute 'id'.
     * @param id
     * 		new value for 'id '
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Getter accessor for attribute 'summary'.
     *
     * @return
     *       current value of 'summary'
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Setter accessor for attribute 'summary'.
     * @param summary
     * 		new value for 'summary '
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Getter accessor for attribute 'start'.
     *
     * @return
     *       current value of 'start'
     */
    public Instant getStart() {
        return start;
    }

    /**
     * Setter accessor for attribute 'start'.
     * @param start
     * 		new value for 'start '
     */
    public void setStart(Instant start) {
        this.start = start;
    }

    /**
     * Getter accessor for attribute 'end'.
     *
     * @return
     *       current value of 'end'
     */
    public Instant getEnd() {
        return end;
    }

    /**
     * Setter accessor for attribute 'end'.
     * @param end
     * 		new value for 'end '
     */
    public void setEnd(Instant end) {
        this.end = end;
    }

    /**
     * Getter accessor for attribute 'spaceCraft'.
     *
     * @return
     *       current value of 'spaceCraft'
     */
    public String getSpaceCraft() {
        return spaceCraft;
    }

    /**
     * Setter accessor for attribute 'spaceCraft'.
     * @param spaceCraft
     * 		new value for 'spaceCraft '
     */
    public void setSpaceCraft(String spaceCraft) {
        this.spaceCraft = spaceCraft;
    }

    /**
     * Getter accessor for attribute 'active'.
     *
     * @return
     *       current value of 'active'
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Setter accessor for attribute 'active'.
     * @param active
     * 		new value for 'active '
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}
