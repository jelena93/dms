/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.elasticsearch.repositories;

import java.util.List;
import org.nst.elasticsearch.domain.DocumentElasticSearch;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;

/**
 *
 * @author jelenas
 */
public interface DocumentElasticSearchRepository extends ElasticsearchCrudRepository<DocumentElasticSearch, Long> {

    List<DocumentElasticSearch> findByCompanyIDAndFileName(Long companyID, String filename);

    List<DocumentElasticSearch> findByCompanyIDAndFileNameContainingIgnoreCase(Long companyID, String filename);

    List<DocumentElasticSearch> findByCompanyID(Long companyID);

    List<DocumentElasticSearch> findByDescriptorsDescriptorKey(String descriptorKey);

}
