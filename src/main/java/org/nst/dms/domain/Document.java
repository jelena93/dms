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
@Table (name = "document")
public class Document implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "document_id")
    @GeneratedValue
    @NotNull
    private Long id;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "document_descriptors", joinColumns = @JoinColumn(name = "document"), inverseJoinColumns = @JoinColumn(name = "descriptor"))
    private List<Descriptor> descriptors;
    @Column(name = "url")
    @NotNull
    private String url;

    public Document() { }
    
    public Document(String url) {
        this.url = url;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public List<Descriptor> getDescriptors() { return descriptors; }
    public void setDescriptors(List<Descriptor> descriptors) { this.descriptors = descriptors; }
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
        final Document other = (Document) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return url;
    }
}
