/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.elasticsearch.repositories;

import org.nst.dms.domain.Document;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 *
 * @author jelenas
 */
public interface DocumentElasticSearchRepository extends ElasticsearchCrudRepository<Document, Long> {

}
