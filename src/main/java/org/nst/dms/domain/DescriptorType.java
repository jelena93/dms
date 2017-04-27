/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author ana
 */
@Entity
@Table(name = "descriptor_type")
public class DescriptorType {
    @Id
    @SequenceGenerator(name = "ParamTypeGen", sequenceName = "PARAM_TYPE_ID_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "ParamTypeGen", strategy = GenerationType.SEQUENCE)
    @Column(name = "ID", unique = true, nullable = false) 
    private Long id;
    @Column(name = "PARAM_CLASS")
    private Class paramClass;

    public DescriptorType() { }

    public DescriptorType(Class paramClass) {
        this.paramClass = paramClass;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Class getParamClass() { return paramClass; }
    public void setParamClass(Class paramClass) { this.paramClass = paramClass; }
    public String getStringMessageByParamClass () {
        if(Integer.class.equals(paramClass)) return "integer";
        else if(Double.class.equals(paramClass)) return "decimal number";
        else if(Date.class.equals(paramClass)) return "date";
        return "N/A";
    }

}