/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.services.impl;

import java.util.List;
import org.nst.dms.domain.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.nst.dms.repositories.DocumentTypeRepository;
import org.nst.dms.services.DocumentTypeService;

/**
 *
 * @author Hachiko
 */
@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Override
    public List<DocumentType> findAll() {
        return documentTypeRepository.findAll();
    }
    @Override
    public DocumentType save(DocumentType documentType) {
        return documentTypeRepository.save(documentType);
    }
    @Override
    public DocumentType find(Long id) {
        return documentTypeRepository.findOne(id);
    }
    @Override
    public List<DocumentType> findByIdIn(List<Long> ids) {
        return documentTypeRepository.findByIdIn(ids);
    }

}
