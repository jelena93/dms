/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.repositories;

import java.util.List;
import org.nst.dms.domain.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jelena
 */
@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM document_type WHERE document_type_id = ?1")
    List<DocumentType> findById(List<Long> ids);
}
