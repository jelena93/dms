/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.repositories;

import java.util.List;
import org.nst.dms.domain.Descriptor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Hachiko
 */
@Repository
public interface DescriptorRepository extends JpaRepository<Descriptor, Long> {
    @Query("SELECT d FROM Descriptor d WHERE d.documentType=?1")
    List<Descriptor> getDescriptorValuesForDocumentType(Long id);
}
