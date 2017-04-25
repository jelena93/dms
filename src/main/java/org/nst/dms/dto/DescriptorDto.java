/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.dto;

import java.util.Objects;

/**
 *
 * @author Jelena
 */
public class DescriptorDto {

    private Long id;
    private Long documentType;
    private String descriptorKey;
    private String value;

    public DescriptorDto() {
    }

    public DescriptorDto(Long id, Long documentType, String descriptorKey, String value) {
        this.id = id;
        this.documentType = documentType;
        this.descriptorKey = descriptorKey;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentType() {
        return documentType;
    }

    public void setDocumentType(Long documentType) {
        this.documentType = documentType;
    }

    public String getDescriptorKey() {
        return descriptorKey;
    }

    public void setDescriptorKey(String descriptorKey) {
        this.descriptorKey = descriptorKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        final DescriptorDto other = (DescriptorDto) obj;
        if (!Objects.equals(this.descriptorKey, other.descriptorKey)) {
            return false;
        }
        if (!Objects.equals(this.value, other.value)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DescriptorDto{" + "id=" + id + ", documentType=" + documentType + ", descriptorKey=" + descriptorKey + ", value=" + value + '}';
    }

}
