package org.nst.dms.elasticsearch.services;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.nst.dms.domain.Document;
import org.nst.dms.elasticsearch.indexing.ElasticClient;
import org.nst.dms.elasticsearch.indexing.IndexName;
import org.nst.dms.elasticsearch.indexing.IndexType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchService {

    @Autowired
    private ElasticClient elasticClient;

    public List<Document> searchDocumentsForCompany(String query, int limit, int page) {
        int offset = (page - 1) * limit;
        QueryBuilder qb;
        if ("".equals(query)) {
            qb = QueryBuilders.matchAllQuery();
        } else {
            qb = QueryBuilders.queryStringQuery(query + "*").field("companyID");
        }
        SearchResponse searchResponse = elasticClient.getClient()
                .prepareSearch(IndexName.DOCUMENT_INDEX.value())
                .setTypes(IndexType.DOCUMENT.name())
                .setQuery(qb)
                .setFrom(offset)
                .setSize(limit)
                .execute().actionGet();
        List<Document> documents = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits()) {

//            documents.add(Long.parseLong(hit.getId()));
        }

        return documents;
    }

//    public List<Long> searchAuthors(String query, int limit, int page) {
//        int offset = (page - 1) * limit;
//        QueryBuilder qb;
//
//        if ("".equals(query)) {
//            qb = QueryBuilders.matchAllQuery();
//        } else {
//            qb = QueryBuilders.queryStringQuery(query + "*")
//                    .field("name").field("lastname");
//        }
//
//        SearchResponse searchResponse = ElasticClient.getInstance().getClient()
//                .prepareSearch(IndexName.AUTHOR_INDEX.value())
//                .setTypes(IndexType.AUTHOR.name())
//                .setQuery(qb)
//                .setFrom(offset)
//                .setSize(limit)
//                .execute().actionGet();
//
//        List<Long> ids = new ArrayList<Long>();
//
//        for (SearchHit hit : searchResponse.getHits()) {
//            ids.add(Long.parseLong(hit.getId()));
//        }
//
//        return ids;
//    }
//    public List<Long> searchAuthorsWithBookTitle(String query, int limit, int page) {
//        int offset = (page - 1) * limit;
//        QueryBuilder qb;
//
//        if ("".equals(query)) {
//            qb = QueryBuilders.matchAllQuery();
//        } else {
//            qb = QueryBuilders.queryStringQuery(query + "*")
//                    .field("books.title");
//        }
//
//        SearchResponse searchResponse = ElasticClient.getInstance().getClient()
//                .prepareSearch(IndexName.AUTHOR_INDEX.value())
//                .setTypes(IndexType.AUTHOR.name())
//                .setQuery(qb)
//                .setFrom(offset)
//                .setSize(limit)
//                .execute().actionGet();
//
//        List<Long> ids = new ArrayList<Long>();
//
//        for (SearchHit hit : searchResponse.getHits()) {
//            ids.add(Long.parseLong(hit.getId()));
//        }
//
//        return ids;
//    }
}
