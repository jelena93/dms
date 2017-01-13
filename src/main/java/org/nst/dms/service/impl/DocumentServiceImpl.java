/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.service.impl;

import org.nst.dms.domain.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.nst.dms.repositories.DocumentRepository;
import org.nst.dms.service.DocumentService;

/**
 *
 * @author Hachiko
 */
@Service
public class DocumentServiceImpl implements DocumentService {
    @Autowired
    private DocumentRepository documentRepository;
    @Override
    public Document save(Document document) {
        return documentRepository.save(document);
    }

}
