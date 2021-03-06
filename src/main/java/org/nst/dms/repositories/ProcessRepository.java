/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.nst.dms.domain.Process;

/**
 *
 * @author Hachiko
 */
@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
    List<Process> findByParentIsNull();
    Process findByParent(Long parent);
}
