/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.elasticsearch.services;

import org.nst.dms.domain.Document;

/**
 *
 * @author jelenas
 */
public interface DocumentElasticSearchService {

    void save(Document document);
}