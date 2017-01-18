/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.nst.dms.domain.Process;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Hachiko
 */
@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
    @Query("SELECT p FROM Process p WHERE p.name LIKE CONCAT('%',:name,'%')")
    List<Process> search(@Param("name") String name);
    @Query("SELECT p FROM Process p WHERE p.parent is NULL")
    List<Process> getRootProcesses();
}
