/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.elasticsearch.services;

import java.util.List;
import org.nst.dms.domain.Document;

/**
 *
 * @author jelenas
 */
public interface DocumentElasticSearchService {

    Document save(Document document);

    List<Document> findAll();

    List<Document> findByFileName(String filename);

    List<Document> findByDescriptorsDescriptorKey(String descriptorKey);

}
