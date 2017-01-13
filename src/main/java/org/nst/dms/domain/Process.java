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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Hachiko
 */
@Entity
@Table (name = "process")
public class Process implements Serializable {
    @Id
    @Basic(optional = false)
    @NotNull
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "parent", nullable = true)
    @ManyToOne
    private Process parent;
    @Column(name = "primitive")
    private boolean primitive;
    @OneToMany
    @JoinTable(name = "input", joinColumns = @JoinColumn(name = "process"), inverseJoinColumns = @JoinColumn(name = "document"))
    private List<Document> input;
    @OneToMany
    @JoinTable(name = "output", joinColumns = @JoinColumn(name = "process"), inverseJoinColumns = @JoinColumn(name = "document"))
    private List<Document> output;

    public Process() { }

    public Process(Long id) {
        this.id = id;
    }

    public Process(Long id, String name, Process parent, boolean primitive, List<Document> input, List<Document> output) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.primitive = primitive;
        this.input = input;
        this.output = output;
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
    public Process getParent() {
        return parent;
    }
    public void setParent(Process parent) {
        this.parent = parent;
    }
    public boolean isPrimitive() {
        return primitive;
    }
    public void setPrimitive(boolean primitive) {
        this.primitive = primitive;
    }
    public List<Document> getInput() {
        return input;
    }
    public void setInput(List<Document> input) {
        this.input = input;
    }
    public List<Document> getOutput() {
        return output;
    }
    public void setOutput(List<Document> output) {
        this.output = output;
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
        final Process other = (Process) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return name;
    }
    
}
