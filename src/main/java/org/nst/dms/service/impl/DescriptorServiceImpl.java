/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.service.impl;

import java.util.List;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.repositories.DescriptorRepository;
import org.nst.dms.service.DescriptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Hachiko
 */
@Service
public class DescriptorServiceImpl implements DescriptorService {
    @Autowired
    private DescriptorRepository descriptorRepository;
    @Override
    public List<Descriptor> getDescriptorValuesForDocumentType(Long id) {
        return descriptorRepository.getDescriptorValuesForDocumentType(id);
    }
 }
