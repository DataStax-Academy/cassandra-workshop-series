package service;


import model.RefDataMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.mapping.BasicMapId;
import org.springframework.data.cassandra.core.mapping.MapId;
import repository.RefDataMappingRepository;

import java.util.Optional;

public class RefDataServiceImpl implements RefDataService {

    @Autowired
    private RefDataMappingRepository refDataMappingRepository;

    public RefDataMapping getRefDataMapping(String source,String destination){
        MapId mapId= BasicMapId.id("source",source).with("destination",destination);
        //getting exception at below line as codec not found for requested operation:[map<varchar,varchar> <-> java.util.Map]
        Optional<RefDataMapping> refDataMappingOptional=refDataMappingRepository.findById(mapId);
        RefDataMapping refDataMapping=refDataMappingOptional.isPresent()?refDataMappingOptional.get():null;
        return  refDataMapping;
    }

}
