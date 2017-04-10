/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.services;

import java.util.List;
import org.nst.dms.domain.DocumentType;

/**
 *
 * @author Hachiko
 */
public interface DocumentTypeService {
    List<DocumentType> findAll();
    DocumentType save(DocumentType documentType);
    DocumentType find(Long id);
    List<DocumentType> findByIdIn(List<Long> ids);
}
