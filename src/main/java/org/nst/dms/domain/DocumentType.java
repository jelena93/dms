/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Hachiko
 */
@Entity
@Table (name = "document_type")
public class DocumentType implements Serializable {
    @Id
    @Basic(optional = false)
    @GeneratedValue
    @Column(name = "document_type_id")
    @NotNull
    private Long id;
    @Column(name = "name")
    @NotNull
    private String name;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "document_type_descriptors", joinColumns = @JoinColumn(name = "document_type"), inverseJoinColumns = @JoinColumn(name = "descriptor"))
    private List<Descriptor> descriptors;

    public DocumentType() { }

    public DocumentType(String name, List<Descriptor> descriptors) {
        this.name = name;
        this.descriptors = descriptors;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Descriptor> getDescriptors() {
        return descriptors;
    }
    public void setDescriptors(List<Descriptor> descriptors) {
        this.descriptors = descriptors;
    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final DocumentType other = (DocumentType) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return name + ": "+ descriptors;
    }
    
}
