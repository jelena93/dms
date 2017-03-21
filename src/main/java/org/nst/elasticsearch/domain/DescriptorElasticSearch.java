/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.elasticsearch.domain;

import java.util.Objects;
import org.nst.dms.domain.DescriptorType;

/**
 *
 * @author jelenas
 */
public class DescriptorElasticSearch {

    private Long id;
    private Long documentType;
    private String descriptorKey;
    private DescriptorType descriptorType;
    private String value;

    public DescriptorElasticSearch() {
    }

    public DescriptorElasticSearch(Long id, Long documentType, String descriptorKey, DescriptorType descriptorType, String value) {
        this.id = id;
        this.documentType = documentType;
        this.descriptorKey = descriptorKey;
        this.descriptorType = descriptorType;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescriptorKey() {
        return descriptorKey;
    }

    public void setDescriptorKey(String descriptorKey) {
        this.descriptorKey = descriptorKey;
    }

    public Long getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Long documentType) {
        this.documentType = documentType;
    }

    public DescriptorType getDescriptorType() {
        return descriptorType;
    }

    public void setDescriptorType(DescriptorType descriptorType) {
        this.descriptorType = descriptorType;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DescriptorElasticSearch other = (DescriptorElasticSearch) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DocumentDto{" + "id=" + id + ", documentType=" + documentType + ", descriptorKey=" + descriptorKey + ", descriptorType=" + descriptorType + ", value=" + value + '}';
    }
}
