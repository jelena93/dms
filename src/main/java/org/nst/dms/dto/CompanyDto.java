/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.dto;

import java.util.Objects;

/**
 *
 * @author hachiko
 */
public class CompanyDto {
    private Long id;
    private String name;
    private String pib;
    private String identificationNumber;
    private String headquarters;

    public CompanyDto() { }

    public CompanyDto(Long id, String name, String pib, String identificationNumber, String headquarters) {
        this.id = id;
        this.name = name;
        this.pib = pib;
        this.identificationNumber = identificationNumber;
        this.headquarters = headquarters;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPib() { return pib; }
    public void setPib(String pib) { this.pib = pib; }
    public String getIdentificationNumber() { return identificationNumber; }
    public void setIdentificationNumber(String identificationNumber) { this.identificationNumber = identificationNumber; }
    public String getHeadquarters() { return headquarters; }
    public void setHeadquarters(String headquarters) { this.headquarters = headquarters; }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.name);
        hash = 53 * hash + Objects.hashCode(this.pib);
        hash = 53 * hash + Objects.hashCode(this.identificationNumber);
        hash = 53 * hash + Objects.hashCode(this.headquarters);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final CompanyDto other = (CompanyDto) obj;
        if (!Objects.equals(this.name, other.name)) return false;
        if (!Objects.equals(this.pib, other.pib)) return false;
        if (!Objects.equals(this.identificationNumber, other.identificationNumber)) return false;
        if (!Objects.equals(this.headquarters, other.headquarters)) return false;
        if (!Objects.equals(this.id, other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "CompanyDto{" + "id=" + id + ", name=" + name + ", pib=" + pib + ", identificationNumber=" + identificationNumber + ", headquarters=" + headquarters + '}';
    }
}
