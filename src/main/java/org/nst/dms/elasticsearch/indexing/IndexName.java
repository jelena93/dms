package org.nst.dms.elasticsearch.indexing;

public enum IndexName {

    DOCUMENT_INDEX("document_index");

    private String value;

    IndexName(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
