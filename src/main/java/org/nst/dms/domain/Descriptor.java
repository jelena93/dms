/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Hachiko
 */
@Entity
@Table (name = "descriptor", indexes = {
    @Index(columnList = "NUMBER_VALUE", name = "idx_param_number_value"),
    @Index(columnList = "DOUBLE_VALUE", name = "idx_param_double_value"),
    @Index(columnList = "DATE_VALUE", name = "idx_param_date_value"),
    @Index(columnList = "STRING_VALUE", name = "idx_param_string_value")
})
public class Descriptor implements Serializable {
    @Id
    @Basic(optional = false)
    @Column(name = "descriptor_id")
    @GeneratedValue
    @NotNull
    private Long id;
    @Column(name = "document_type")
    @NotNull
    private Long documentType;
    @Column(name = "descriptorKey")
    @NotNull
    private String descriptorKey;
    @Column(name = "descriptorValue")
    @NotNull
    private String DescriptorValue;
    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "descriptor_type", nullable = false) 
    private DescriptorType descriptorType;
   
    @Column(name = "NUMBER_VALUE") 
    private Long longValue;
 
    @Column(name = "DOUBLE_VALUE") 
    private Double doubleValue;
   
    @Column(name = "DATE_VALUE") 
    @Temporal(TemporalType.DATE)
    private Date dateValue;
   
    @Column(name = "STRING_VALUE") 
    private String stringValue;
   
    @Lob
//    @Type(type = "text")
    @Column(name = "JSON_VALUE") 
    private String jsonValue;

    public Descriptor() { }

    public Descriptor(String key, String value, Long documentType, DescriptorType descriptorType) {
        this.descriptorKey = key;
        this.DescriptorValue = value;
        this.documentType = documentType;
        this.descriptorType = descriptorType;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescriptorKey() { return descriptorKey; }
    public void setDescriptorKey(String descriptorKey) { this.descriptorKey = descriptorKey; }
    public String getDescriptorValue() { return DescriptorValue; }
    public void setDescriptorValue(String DescriptorValue) { this.DescriptorValue = DescriptorValue; }
    public Long getDocumentType() { return documentType; }
    public void setDocumentType(Long documentType) { this.documentType = documentType; }
    public DescriptorType getDescriptorType() { return descriptorType; }
    public void setDescriptorType(DescriptorType descriptorType) { this.descriptorType = descriptorType; }
    
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
        return descriptorKey + ": " + DescriptorValue;
    }
    
    public Object getValue() {
        Class paramClass = descriptorType.getParamClass();
        if (Integer.class.equals(paramClass)) return longValue != null ? longValue.intValue() : null;
        else if (Long.class.equals(paramClass)) return longValue;
        else if (Double.class.equals(paramClass)) return doubleValue;
        else if (String.class.equals(paramClass)) return stringValue;
        else if (Date.class.equals(paramClass)) return dateValue;
        else if (Date.class.equals(paramClass)) return dateValue;
//        else if (jsonValue != null && !jsonValue.trim().isEmpty()) {
//            Gson gson = new Gson();
//            return gson.fromJson(jsonValue, paramClass);
//        }
        return null;
    }
   
    public void setValue(Object value) {
        Class paramClass = descriptorType.getParamClass();
        if (value == null) {
            longValue = null;
            doubleValue = null;
            stringValue = null;
            dateValue = null;
            jsonValue = null;
        } else {
//            Gson gson = new Gson();
            if (Integer.class.equals(paramClass)) {
                longValue = ((Integer) value).longValue();
                doubleValue = longValue.doubleValue();
            } else if (Long.class.equals(paramClass)) {
                longValue = ((Long) value);
                doubleValue = longValue.doubleValue();
            } else if (Double.class.equals(paramClass)) {
                doubleValue = ((Double) value);
                longValue = doubleValue.longValue();
            } else if (String.class.equals(paramClass)) stringValue = (String) value;
            else if (Date.class.equals(paramClass)) dateValue = (Date) value;
            StringBuilder builder = new StringBuilder();
//            gson.toJson(value, builder);
            jsonValue = builder.toString();
        }
    }
}
