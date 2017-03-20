/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.repositories;

import java.util.List;
import org.nst.dms.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Hachiko
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByDescriptorsDescriptorKey(String descriptorKey);

}
