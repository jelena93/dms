/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.services;

import org.nst.dms.domain.Document;

/**
 *
 * @author Hachiko
 */
public interface DocumentService {
    Document findOne(Long id);
}
