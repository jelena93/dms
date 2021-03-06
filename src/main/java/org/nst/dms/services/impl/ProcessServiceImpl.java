/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.services.impl;

import java.util.List;
import org.nst.dms.repositories.ProcessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.nst.dms.services.ProcessService;
import org.nst.dms.domain.Process;

/**
 *
 * @author Hachiko
 */
@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    private ProcessRepository processRepository;
    @Override
    public List<Process> findAll() {
        return processRepository.findAll();
    }
    @Override
    public Process save(Process process) {
        return processRepository.save(process);
    }
    @Override
    public Process find(long id) {
        return processRepository.findOne(id);
    }
    @Override
    public List<Process> findByParentIsNull() {
        return processRepository.findByParentIsNull();
    }
    @Override
    public void delete(Process process) {
        processRepository.delete(process);
    }

    @Override
    public Process findByParent(Long id) {
        return processRepository.findByParent(id);
    }
}
