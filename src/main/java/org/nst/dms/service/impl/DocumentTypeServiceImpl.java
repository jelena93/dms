/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.service.impl;

import java.util.List;
import org.nst.dms.domain.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.nst.dms.repositories.DocumentTypeRepository;
import org.nst.dms.service.DocumentTypeService;

/**
 *
 * @author Hachiko
 */
@Service
public class DocumentTypeServiceImpl implements DocumentTypeService {
    @Autowired
    private DocumentTypeRepository documentRepository;
    @Override
    public List<DocumentType> findAll() {
        return documentRepository.findAll();
    }
    @Override
    public DocumentType save(DocumentType documentType) {
        return documentRepository.save(documentType);
    }
    @Override
    public DocumentType find(Long id) {
        return documentRepository.findOne(id);
    }

}
