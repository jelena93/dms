package org.nst.dms.elasticsearch.indexing;

import java.io.IOException;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Requests;
import static org.elasticsearch.client.Requests.createIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;

public class DocumentIndexer {

    @Autowired
    private ElasticClient elasticClient;
    private final Logger log = LogManager.getLogger(ElasticClient.class);

    public void indexDocuments(List<Document> documents) {
        for (Document document : documents) {
            indexDocument(document);
        }
    }

    public void indexDocument(Document document) {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            builder.field("id", document.getId());
            builder.field("companyID", document.getCompanyID());
            builder.field("fileName", document.getFileName());
            builder.field("fileContent", document.getFileContent());

            List<Descriptor> descriptors = document.getDescriptors();
            builder.startArray("descriptors");

            for (Descriptor d : descriptors) {
                builder.startObject();
                builder.field("id", d.getId());
                builder.field("documentType", d.getDocumentType());
                builder.field("descriptorKey", d.getDescriptorKey());
                String valueAsString = d.asd();
                builder.field("valueAsString", valueAsString);
                builder.endObject();
            }
            builder.endArray();
            builder.endObject();

            elasticClient.getClient().prepareIndex(
                    ElasticSearchUtil.DOCUMENT_INDEX, ElasticSearchUtil.DOCUMENT_TYPE, String.valueOf(document.getId()))
                    .setSource(builder)
                    .get();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void createDocumentIndexIfNotExists() {
        boolean exists = elasticClient.getClient().admin().indices()
                .prepareExists(ElasticSearchUtil.DOCUMENT_INDEX)
                .execute().actionGet().isExists();
        if (!exists) {
            elasticClient.getClient().admin().indices().
                    create(createIndexRequest(ElasticSearchUtil.DOCUMENT_INDEX)).actionGet();
        }
    }

    public void deleteDocumentIndexes() {
        boolean exists = elasticClient.getClient().admin().indices()
                .prepareExists(ElasticSearchUtil.DOCUMENT_INDEX)
                .execute().actionGet().isExists();
        if (exists) {
            elasticClient.getClient().
                    admin().indices().delete(new DeleteIndexRequest(ElasticSearchUtil.DOCUMENT_INDEX)).actionGet();
        }
    }

    public void deleteDocument(Document document) {
        elasticClient.getClient().prepareDelete(ElasticSearchUtil.DOCUMENT_INDEX,
                ElasticSearchUtil.DOCUMENT_TYPE, String.valueOf(document.getId())).get();
    }
//    public void updateDocument(Document document) {
//UpdateRequest updateRequest = new UpdateRequest(IndexName.DOCUMENT_INDEX.value(),  IndexType.DOCUMENT.name(), 
//String.valueOf(document.getId()));
//        .doc(jsonBuilder()
//            .startObject()
//                .field("gender", "male")
//            .endObject());
//client.update(updateRequest).get();

//    }
//    public void insertOrUpdateDocument(Document document) {
//IndexRequest indexRequest = new IndexRequest(IndexName.DOCUMENT_INDEX.value(),  IndexType.DOCUMENT.name(), 
//String.valueOf(document.getId()))
//        .source(jsonBuilder()
//            .startObject()
//                .field("name", "Joe Smith")
//                .field("gender", "male")
//            .endObject());
//UpdateRequest updateRequest = new UpdateRequest(IndexName.DOCUMENT_INDEX.value(),  IndexType.DOCUMENT.name(), 
//String.valueOf(document.getId()))
//        .doc(jsonBuilder()
//            .startObject()
//                .field("gender", "male")
//            .endObject())
//        .upsert(indexRequest);              
//client.update(updateRequest).get();
//    }
}
