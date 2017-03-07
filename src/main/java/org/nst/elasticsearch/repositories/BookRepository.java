package org.nst.elasticsearch.repositories;

import java.util.List;
import org.nst.dms.domain.Document;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ElasticsearchCrudRepository<Document, Long> {

}