package org.nst.elasticsearch.repositories;

import java.util.List;
import org.nst.dms.domain.Document;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepositoryElasticSearch extends ElasticsearchCrudRepository<Document, Long> {

    List<Document> findById(Long id);

}
