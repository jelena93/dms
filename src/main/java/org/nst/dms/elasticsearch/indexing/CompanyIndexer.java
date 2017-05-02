/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.elasticsearch.indexing;

import java.io.ByteArrayInputStream;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tika.Tika;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import static org.elasticsearch.client.Requests.createIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.nst.dms.domain.Company;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.nst.dms.elasticsearch.util.ElasticSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.nst.dms.domain.Process;

/**
 *
 * @author hachiko
 */
public class CompanyIndexer {
    @Autowired
    private ElasticClient elasticClient;
    @Autowired
    private Tika tika;
    private final Logger logger = LogManager.getLogger(ElasticClient.class);

    public void indexCompany(Company company) throws Exception {
        elasticClient.getClient().prepareIndex(
                ElasticSearchUtil.COMPANY_INDEX, ElasticSearchUtil.COMPANY_TYPE, String.valueOf(company.getId()))
                .setSource(buildCompany(company)).get();
    }

    public void updateCompany(Company company) throws Exception {
        elasticClient.getClient().prepareUpdate(
                ElasticSearchUtil.COMPANY_INDEX, ElasticSearchUtil.COMPANY_TYPE, String.valueOf(company.getId()))
                .setDoc(buildCompany(company)).get();
    }

    public void createCompanyIndexIfNotExists() {
        boolean exists = elasticClient.getClient().admin().indices()
                .prepareExists(ElasticSearchUtil.COMPANY_INDEX)
                .execute().actionGet().isExists();
        if (!exists) {
            elasticClient.getClient().admin().indices().
                    create(createIndexRequest(ElasticSearchUtil.COMPANY_INDEX)).actionGet();
        }
    }

    public void deleteCompanyIndexes() {
        boolean exists = elasticClient.getClient().admin().indices()
                .prepareExists(ElasticSearchUtil.COMPANY_INDEX)
                .execute().actionGet().isExists();
        if (exists) {
            elasticClient.getClient().
                    admin().indices().delete(new DeleteIndexRequest(ElasticSearchUtil.COMPANY_INDEX)).actionGet();
        }
    }

    public void deleteCompany(Company company) {
        elasticClient.getClient().prepareDelete(ElasticSearchUtil.COMPANY_INDEX,
                ElasticSearchUtil.COMPANY_TYPE, String.valueOf(company.getId())).get();
    }

    public XContentBuilder buildCompany(Company company) throws Exception {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        builder.field("id", company.getId());
        builder.field("name", company.getName());
        List<Process> processes = company.getProcesses();
        builder.startArray("processes");
        for (Process p : processes) {
            builder.startObject();
            builder.field("id", p.getId());
            builder.field("name", p.getName());
            builder.field("primitive", p.isPrimitive());
            //sta da radim za proces u procesu?
            builder.endObject();
        }
        builder.endArray();
        builder.endObject();
        return builder;
    }
}
