package com.datastax.workshop.test;

import com.datastax.driver.mapping.annotations.Frozen;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;
import java.util.Map;

@Table(value="refdata_mapping")
public class RefDataMapping {
    @PrimaryKeyColumn(value="source",ordinal=0,type= PrimaryKeyType.PARTITIONED)
    private String source;

    @PrimaryKeyColumn(value="destination",ordinal=1,type= PrimaryKeyType.CLUSTERED)
    private String destination;

    @Column(value="field_mapping")
    private Map<String,String> fieldMapping;

    @Frozen
    @Column(value="audit")
    private List<Map<String,String >> audit;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Map<String, String> getFieldMapping() {
        return fieldMapping;
    }

    public void setFieldMapping(Map<String, String> fieldMapping) {
        this.fieldMapping = fieldMapping;
    }

    public List<Map<String, String>> getAudit() {
        return audit;
    }

    public void setAudit(List<Map<String, String>> audit) {
        this.audit = audit;
    }
}
