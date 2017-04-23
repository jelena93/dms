package org.nst.dms.elasticsearch.indexing;

import java.io.IOException;
import java.util.List;
import org.elasticsearch.action.delete.DeleteResponse;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;

public class DocumentIndexer {

    @Autowired
    private ElasticClient elasticClient;

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
            // index document descriptors
            List<Descriptor> descriptors = document.getDescriptors();

            builder.startArray("descriptors");

            for (Descriptor d : descriptors) {
                builder.startObject();
                builder.field("id", d.getId());
                builder.field("documentType", d.getDocumentType());
                builder.field("descriptorType", d.getDescriptorType().getId());
                builder.field("descriptorKey", d.getDescriptorKey());
                builder.field("descriptorValue", d.getValueAsString());
                builder.endObject();
            }
            builder.endArray();

            builder.endObject();

            @SuppressWarnings("unused")
            IndexResponse response = elasticClient.getClient().prepareIndex(
                    IndexName.DOCUMENT_INDEX.value(), IndexType.DOCUMENT.name(), String.valueOf(document.getId()))
                    .setSource(builder)
                    .get();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void deleteDocumentIndexes() {
        elasticClient.getClient().admin().indices().delete(Requests.deleteIndexRequest(IndexName.DOCUMENT_INDEX.name()));
    }

    public void deleteDocument(Document document) {
        DeleteResponse response = elasticClient.getClient().prepareDelete(IndexName.DOCUMENT_INDEX.value(),
                IndexType.DOCUMENT.name(), String.valueOf(document.getId())).get();
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
