/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.elasticsearch.services.impl;

import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import org.nst.elasticsearch.domain.DocumentElasticSearch;
import org.nst.elasticsearch.repositories.DocumentElasticSearchRepository;
import org.nst.elasticsearch.services.DocumentElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jelenas
 */
@Service
public class DocumentElasticSearchServiceImpl implements DocumentElasticSearchService {

    @Autowired
    private DocumentElasticSearchRepository documentElasticSearchRepository;

    @Override
    public DocumentElasticSearch save(DocumentElasticSearch document) {
        return documentElasticSearchRepository.save(document);
    }

    @Override
    public List<DocumentElasticSearch> findAll() {
        return IteratorUtils.toList(documentElasticSearchRepository.findAll().iterator());
    }

    @Override
    public List<DocumentElasticSearch> findByFileName(String filename) {
        return documentElasticSearchRepository.findByFileName(filename);
    }

    @Override
    public List<DocumentElasticSearch> findByDescriptorsDescriptorKey(String descriptorKey) {
        return documentElasticSearchRepository.findByDescriptorsDescriptorKey(descriptorKey);
    }

}
