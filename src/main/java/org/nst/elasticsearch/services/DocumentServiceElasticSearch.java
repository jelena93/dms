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
 * @author Jelena
 */
public interface DocumentServiceElasticSearch {

    List<Document> findById(Long id);

    Document save(Document document);
}
