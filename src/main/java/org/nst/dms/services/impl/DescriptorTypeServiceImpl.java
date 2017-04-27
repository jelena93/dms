/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.services.impl;

import org.nst.dms.domain.DescriptorType;
import org.nst.dms.repositories.DescriptorTypeRepository;
import org.nst.dms.services.DescriptorTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jelenas
 */
@Service
public class DescriptorTypeServiceImpl implements DescriptorTypeService {

    @Autowired
    private DescriptorTypeRepository descriptorTypeRepository;

    @Override
    public DescriptorType save(DescriptorType descriptorType) {
        return descriptorTypeRepository.save(descriptorType);
    }

}
