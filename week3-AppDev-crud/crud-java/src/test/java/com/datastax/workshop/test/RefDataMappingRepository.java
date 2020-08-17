package repository;

import model.RefDataMapping;
import org.springframework.data.cassandra.core.mapping.MapId;
import org.springframework.data.cassandra.repository.CassandraRepository;

public interface RefDataMappingRepository extends CassandraRepository<RefDataMapping, MapId> {
}
