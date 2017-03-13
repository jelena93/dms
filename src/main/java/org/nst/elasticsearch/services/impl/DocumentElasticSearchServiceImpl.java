/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.elasticsearch.services.impl;

import java.util.List;
import org.nst.dms.domain.Document;
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
    public void save(List<Document> documents) {
        documentElasticSearchRepository.save(documents);
    }
}
