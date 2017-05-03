package org.nst.dms.elasticsearch.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.nst.dms.dto.DocumentDto;
import org.nst.dms.elasticsearch.indexing.ElasticClient;
import org.nst.dms.elasticsearch.util.ElasticSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchService {

    @Autowired
    private ElasticClient elasticClient;

    public SearchResponse searchDocumentsForCompany(long companyID, String query, int limit, int page) throws IOException {
        int offset = (page - 1) * limit;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("companyID", companyID));
        if (query != null && !query.isEmpty()) {
            boolQuery.should(QueryBuilders.queryStringQuery(query + "*").field("fileName"))
                    .should(QueryBuilders.queryStringQuery(query + "*").field("content"))
                    .should(QueryBuilders.queryStringQuery(query + "*").field("descriptors.descriptorKey"))
                    .should(QueryBuilders.queryStringQuery(query + "*").field("descriptors.value"))
                    .minimumNumberShouldMatch(1);
        }
        SearchResponse searchResponse = elasticClient.getClient()
                .prepareSearch(ElasticSearchUtil.DOCUMENT_INDEX)
                .setTypes(ElasticSearchUtil.DOCUMENT_TYPE)
                .setQuery(boolQuery)
                .setFrom(offset)
                .setSize(limit)
                .execute().actionGet();
        return searchResponse;
    }

    public List<DocumentDto> findDocumentsForCompanyByDescriptors(long companyID, String descriptorKey, long docType, String value, int limit, int page) throws IOException {
        int offset = (page - 1) * limit;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("companyID", companyID)).
                must(QueryBuilders.termQuery("descriptors.documentType", docType))
                .must(QueryBuilders.matchQuery("descriptors.descriptorKey", descriptorKey))
                .must(QueryBuilders.matchQuery("descriptors.value", value));

        SearchResponse searchResponse = elasticClient.getClient()
                .prepareSearch(ElasticSearchUtil.DOCUMENT_INDEX)
                .setTypes(ElasticSearchUtil.DOCUMENT_TYPE)
                .setQuery(boolQuery)
                .setFrom(offset)
                .setSize(limit)
                .execute().actionGet();
        List<DocumentDto> documents = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (SearchHit hit : searchResponse.getHits()) {
            documents.add(mapper.readValue(hit.getSourceAsString(), DocumentDto.class));
        }
        return documents;
    }

    public List<DocumentDto> findDocumentsForCompanyByFileName(long companyID, String fileName, int limit, int page) throws IOException {
        int offset = (page - 1) * limit;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.termQuery("companyID", companyID)).
                must(QueryBuilders.termQuery("fileName", fileName));

        SearchResponse searchResponse = elasticClient.getClient()
                .prepareSearch(ElasticSearchUtil.DOCUMENT_INDEX)
                .setTypes(ElasticSearchUtil.DOCUMENT_TYPE)
                .setQuery(boolQuery)
                .setFrom(offset)
                .setSize(limit)
                .execute().actionGet();
        List<DocumentDto> documents = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        for (SearchHit hit : searchResponse.getHits()) {
            documents.add(mapper.readValue(hit.getSourceAsString(), DocumentDto.class));
        }
        return documents;
    }

    public SearchResponse searchCompanies(String query, int limit, int page) throws IOException {
        int offset = (page - 1) * limit;
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        if (query != null && !query.isEmpty()) {
            boolQuery.should(QueryBuilders.queryStringQuery(query + "*").field("name"))
                    .should(QueryBuilders.queryStringQuery(query + "*").field("pib"))
                    .should(QueryBuilders.queryStringQuery(query + "*").field("identificationNumber"))
                    .should(QueryBuilders.queryStringQuery(query + "*").field("headquarters"))
                    .minimumNumberShouldMatch(1);
        } else {
            boolQuery.must(QueryBuilders.matchAllQuery());
        }

        SearchResponse searchResponse = elasticClient.getClient()
                .prepareSearch(ElasticSearchUtil.COMPANY_INDEX)
                .setTypes(ElasticSearchUtil.COMPANY_TYPE)
                .setQuery(boolQuery)
                .setFrom(offset)
                .setSize(limit)
                .execute().actionGet();
        return searchResponse;
    }
}
