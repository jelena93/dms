/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.services;

import java.util.List;
import org.nst.dms.domain.Activity;

/**
 *
 * @author Hachiko
 */
public interface ActivityService {
    List<Activity> findAll();
    Activity save(Activity activity);
    Activity find(long id);
}
