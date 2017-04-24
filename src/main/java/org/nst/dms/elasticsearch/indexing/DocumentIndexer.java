package org.nst.dms.elasticsearch.indexing;

import org.nst.dms.elasticsearch.util.ElasticSearchUtil;
import java.io.IOException;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
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

    public void indexDocuments(List<Document> documents) throws Exception {
        for (Document document : documents) {
            indexDocument(document);
        }
    }

    public void indexDocument(Document document) throws Exception {
        elasticClient.getClient().prepareIndex(
                ElasticSearchUtil.DOCUMENT_INDEX, ElasticSearchUtil.DOCUMENT_TYPE, String.valueOf(document.getId()))
                .setSource(buildDocument(document)).get();
    }

    public void updateDocument(Document document) throws Exception {
        elasticClient.getClient().prepareUpdate(
                ElasticSearchUtil.DOCUMENT_INDEX, ElasticSearchUtil.DOCUMENT_TYPE, String.valueOf(document.getId()))
                .setDoc(buildDocument(document)).get();
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

    private XContentBuilder buildDocument(Document document) throws Exception {
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        builder.field("id", document.getId());
        builder.field("companyID", document.getCompanyID());
        builder.field("fileName", document.getFileName());
        builder.field("fileContent", Base64.encodeBase64String(document.getFileContent()));
        List<Descriptor> descriptors = document.getDescriptors();
        builder.startArray("descriptors");
        for (Descriptor d : descriptors) {
            builder.startObject();
            builder.field("id", d.getId());
            builder.field("documentType", d.getDocumentType());
            builder.field("descriptorKey", d.getDescriptorKey());
            builder.field("valueAsString", d.asd());
            builder.endObject();
        }
        builder.endArray();
        builder.endObject();
        return builder;
    }
}
