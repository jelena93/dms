/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.service.impl;

import java.util.List;
import org.nst.dms.domain.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.nst.dms.repositories.ActionRepository;
import org.nst.dms.service.ActionService;

/**
 *
 * @author Hachiko
 */
@Service
public class ActionServiceImpl implements ActionService {
    @Autowired
    private ActionRepository actionRepository;
    @Override
    public List<Action> findAll() {
        return actionRepository.findAll();
    }
    @Override
    public Action save(Action action) {
        return actionRepository.save(action);
    }
    @Override
    public Action find(long id) {
        return actionRepository.findOne(id);
    }
}
