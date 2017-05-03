/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.elasticsearch.indexing;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import static org.elasticsearch.client.Requests.createIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.nst.dms.domain.Company;
import org.nst.dms.elasticsearch.util.ElasticSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author hachiko
 */
public class CompanyIndexer {

    @Autowired
    private ElasticClient elasticClient;

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
        builder.field("pib", company.getPib());
        builder.field("identificationNumber", company.getIdentificationNumber());
        builder.field("headquarters", company.getHeadquarters());
        builder.endObject();
        return builder;
    }
}
