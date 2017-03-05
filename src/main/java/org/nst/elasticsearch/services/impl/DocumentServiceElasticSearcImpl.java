/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.elasticsearch.services.impl;

import org.nst.elasticsearch.services.DocumentServiceElasticSearch;
import java.util.List;
import org.nst.dms.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.nst.elasticsearch.repositories.DocumentRepositoryElasticSearch;

/**
 *
 * @author Jelena
 */
@Service
public class DocumentServiceElasticSearcImpl implements DocumentServiceElasticSearch {

    @Autowired
    private DocumentRepositoryElasticSearch documentRepositoryElasticSearch;

    @Override
    public List<Document> findById(Long id) {
        return documentRepositoryElasticSearch.findById(id);
    }

    @Override
    public Document save(Document document) {
        return documentRepositoryElasticSearch.save(document);
    }

}
