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
@Table (name = "company")
public class Company implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "company_id")
    @NotNull
    private Long id;
    @Column(name = "name")
    @NotNull
    private String name;
    @Column(name = "pib")
    @NotNull
    private int pib;
    @Column(name = "identification_number")
    @NotNull
    private String identificationNumber;
    @Column(name = "headquarters")
    @NotNull
    private String headquarters;
    @OneToMany
    @JoinTable(name = "company_processes", joinColumns = @JoinColumn(name = "company"), inverseJoinColumns = @JoinColumn(name = "process"))
    private List<Process> processes;

    public Company() { }

    public Company(Long id, String name, int pib, String identificationNumber, String headquarters, List<Process> processes) {
        this.id = id;
        this.name = name;
        this.pib = pib;
        this.identificationNumber = identificationNumber;
        this.headquarters = headquarters;
        this.processes = processes;
    }

    public Company(String name, int pib, String identificationNumber, String headquarters) {
        this.name = name;
        this.pib = pib;
        this.identificationNumber = identificationNumber;
        this.headquarters = headquarters;
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
    public int getPib() {
        return pib;
    }
    public void setPib(int pib) {
        this.pib = pib;
    }
    public String getIdentificationNumber() {
        return identificationNumber;
    }
    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }
    public String getHeadquarters() {
        return headquarters;
    }
    public void setHeadquarters(String headquarters) {
        this.headquarters = headquarters;
    }
    public List<Process> getProcesses() {
        return processes;
    }
    public void setProcesses(List<Process> processes) {
        this.processes = processes;
    }
    @Override
    public int hashCode() {
        int hash = 7;
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
        final Company other = (Company) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return pib + ": " + name;
    }

    
}
