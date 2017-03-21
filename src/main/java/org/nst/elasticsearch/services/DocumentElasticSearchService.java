/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.elasticsearch.services;

import java.util.List;
import org.nst.elasticsearch.domain.DocumentElasticSearch;

/**
 *
 * @author jelenas
 */
public interface DocumentElasticSearchService {

    DocumentElasticSearch save(DocumentElasticSearch document);

    List<DocumentElasticSearch> findAll();

    List<DocumentElasticSearch> findByFileName(String filename);

    List<DocumentElasticSearch> findByDescriptorsDescriptorKey(String descriptorKey);

}
