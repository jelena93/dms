/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.elasticsearch.domain;

import java.util.List;
import java.util.Objects;
import javax.persistence.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 *
 * @author jelenas
 */
@Document(indexName = "document")
public class DocumentElasticSearch {

    @Id
    private Long id;
    private Long companyID;
    private String fileType;
    private String fileName;
    private byte[] fileContent;
    @Field(type = FieldType.Nested)
    private List<DescriptorElasticSearch> descriptors;

    public DocumentElasticSearch() {
    }

    public DocumentElasticSearch(Long id, Long companyID, String fileType, String fileName, byte[] fileContent, List<DescriptorElasticSearch> descriptors) {
        this.id = id;
        this.companyID = companyID;
        this.fileType = fileType;
        this.fileName = fileName;
        this.fileContent = fileContent;
        this.descriptors = descriptors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Long companyID) {
        this.companyID = companyID;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public List<DescriptorElasticSearch> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<DescriptorElasticSearch> descriptors) {
        this.descriptors = descriptors;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
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
        final DocumentElasticSearch other = (DocumentElasticSearch) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DocumentElasticSearch{" + "id=" + id + ", companyID=" + companyID + ", fileType=" + fileType + ", fileName=" + fileName + ", descriptors=" + descriptors + '}';
    }

}
