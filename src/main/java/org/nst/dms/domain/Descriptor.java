/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.domain;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Hachiko
 */
@Entity
@Table (name = "descriptor")
public class Descriptor implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "descriptor_id")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "descriptor_seq")
    @SequenceGenerator(name = "descriptor_seq", sequenceName = "descriptor_seq")
    @NotNull
    private Long id;
    @Column(name = "document_type")
    @NotNull
    private Long documentType;
    @Column(name = "descriptorKey")
    @NotNull
    private String key;
    @Column(name = "descriptorValue")
    @NotNull
    private String value;

    public Descriptor() { }

    public Descriptor(String key, String value, Long documentType) {
        this.key = key;
        this.value = value;
        this.documentType = documentType;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public Long getDocumentType() {
        return documentType;
    }
    public void setDocumentType(Long documentType) {
        this.documentType = documentType;
    }
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Descriptor other = (Descriptor) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return key + ": " + value;
    }
    
    
}
