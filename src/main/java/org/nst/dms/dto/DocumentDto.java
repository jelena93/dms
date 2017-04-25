/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Jelena
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DocumentDto {

    private Long id;
    private long companyID;
    private String fileType;
    private String fileName;
    private List<DescriptorDto> descriptors;

    public DocumentDto() {
    }

    public DocumentDto(Long id, long companyID, String fileType, String fileName) {
        this.id = id;
        this.companyID = companyID;
        this.fileType = fileType;
        this.fileName = fileName;
        descriptors = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCompanyID() {
        return companyID;
    }

    public void setCompanyID(long companyID) {
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

    public List<DescriptorDto> getDescriptors() {
        return descriptors;
    }

    public void setDescriptors(List<DescriptorDto> descriptors) {
        this.descriptors = descriptors;
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
        final DocumentDto other = (DocumentDto) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DocumentDto{" + "id=" + id + ", companyID=" + companyID + ", fileType=" + fileType + ", fileName=" + fileName + ", descriptors=" + descriptors + '}';
    }

}
