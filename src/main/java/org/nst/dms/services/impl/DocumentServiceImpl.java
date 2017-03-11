/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.services.impl;

import org.nst.dms.domain.Document;
import org.nst.dms.repositories.DocumentRepository;
import org.springframework.stereotype.Service;
import org.nst.dms.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Hachiko
 */
@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    public Document findOne(Long id) {return documentRepository.findOne(id); }

}
