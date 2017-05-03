package org.nst.dms.elasticsearch.indexing;

import java.io.ByteArrayInputStream;
import org.nst.dms.elasticsearch.util.ElasticSearchUtil;
import java.util.List;
import org.apache.tika.Tika;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import static org.elasticsearch.client.Requests.createIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;

public class DocumentIndexer {

    @Autowired
    private ElasticClient elasticClient;
    @Autowired
    private Tika tika;

    public void indexDocument(long companyID, Document document) throws Exception {
        elasticClient.getClient().prepareIndex(
                ElasticSearchUtil.DOCUMENT_INDEX, ElasticSearchUtil.DOCUMENT_TYPE, String.valueOf(document.getId()))
                .setSource(buildDocument(companyID, document)).get();
    }

    public void insertOrUpdateDocuments(long companyID, List<Document> documents) throws Exception {
        for (Document document : documents) {
            insertOrUpdateDocument(companyID, document);
        }
    }

    public void insertOrUpdateDocument(long companyID, Document document) throws Exception {
        IndexRequest indexRequest = new IndexRequest(ElasticSearchUtil.DOCUMENT_INDEX, ElasticSearchUtil.DOCUMENT_TYPE, String.valueOf(document.getId()))
                .source(buildDocument(companyID, document));
        UpdateRequest updateRequest = new UpdateRequest(ElasticSearchUtil.DOCUMENT_INDEX, ElasticSearchUtil.DOCUMENT_TYPE, String.valueOf(document.getId()))
                .doc(buildDocument(companyID, document)).upsert(indexRequest);
        elasticClient.getClient().update(updateRequest).get();
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

    public XContentBuilder buildDocument(long companyID, Document document) throws Exception {
        String parsedContent = tika.parseToString(new ByteArrayInputStream(document.getFileContent()));
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        builder.field("id", document.getId());
        builder.field("companyID", companyID);
        builder.field("fileName", document.getFileName());
        List<Descriptor> descriptors = document.getDescriptors();
        builder.startArray("descriptors");
        for (Descriptor d : descriptors) {
            builder.startObject();
            builder.field("id", d.getId());
            builder.field("documentType", d.getDocumentType());
            builder.field("descriptorKey", d.getDescriptorKey());
            builder.field("value", d.convertValueToString());
            builder.endObject();
        }
        builder.endArray();
        builder.field("content", parsedContent);
        builder.field("fileContent", document.getFileContent());
        builder.endObject();
        return builder;
    }
}
