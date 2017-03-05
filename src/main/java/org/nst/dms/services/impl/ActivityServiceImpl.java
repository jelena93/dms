/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.services.impl;

import java.util.List;
import org.nst.dms.domain.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.nst.dms.repositories.ActivityRepository;
import org.nst.dms.services.ActivityService;

/**
 *
 * @author Hachiko
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityRepository activityRepository;
    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }
    @Override
    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }
    @Override
    public Activity find(long id) {
        return activityRepository.findOne(id);
    }
}
