/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.service;

import java.util.List;
import org.nst.dms.domain.Process;

/**
 *
 * @author Hachiko
 */
public interface ProcessService {
    List<Process> findAll();
    Process save(Process process);
    Process find(long id);
    List<Process> findByParentIsNull();
    void delete(Process process);
    Process findByParent(Long id);
}
