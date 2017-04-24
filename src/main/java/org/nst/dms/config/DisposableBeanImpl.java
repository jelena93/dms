/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.config;

import org.nst.dms.elasticsearch.indexing.DocumentIndexer;
import org.nst.dms.elasticsearch.indexing.ElasticClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Jelena
 */
public class DisposableBeanImpl implements DisposableBean {

    @Autowired
    private ElasticClient elasticClient;
    @Autowired
    private DocumentIndexer documentIndexer;

    @Override
    public void destroy() throws Exception {
//        documentIndexer.deleteDocumentIndexes();
        elasticClient.getClient().close();
    }

}
