/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.elasticsearch.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.nst.dms.domain.Descriptor;
import org.nst.dms.domain.Document;
import org.nst.dms.domain.User;
import org.nst.dms.dto.UserDto;
import org.nst.dms.services.DocumentService;
import org.nst.dms.services.UserService;
import org.nst.elasticsearch.domain.DescriptorElasticSearch;
import org.nst.elasticsearch.domain.DocumentElasticSearch;
import org.nst.elasticsearch.services.DocumentElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author Jelena
 */
@Component
public class ElasticSearchSuccessLoginHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Autowired
    DocumentService documentService;
    @Autowired
    DocumentElasticSearchService documentElasticSearchService;
    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest hsr, HttpServletResponse hsr1, Authentication a) throws IOException, ServletException {
        handle(hsr, hsr1, a);
        clearAuthenticationAttributes(hsr);
    }

    protected void handle(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        fillElasticSearch(authentication);
        redirectStrategy.sendRedirect(request, response, "/");
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private void fillElasticSearch(Authentication a) {
        UserDto userDto = (UserDto) a.getPrincipal();
        User user = userService.findOne(userDto.getUsername());
        List<DocumentElasticSearch> documentsDto = documentElasticSearchService.findByCompanyID(user.getCompany().getId());
        if (documentsDto.isEmpty()) {
            List<Document> documents = documentService.findAll();
            for (Document doc : documents) {
                List<DescriptorElasticSearch> descriptors = new ArrayList<>();
                for (Descriptor desc : doc.getDescriptors()) {
                    descriptors.add(new DescriptorElasticSearch(desc.getId(), desc.getDocumentType(), desc.getDescriptorKey(), desc.getDescriptorType(),
                            desc.getValueAsString()));
                }
                DocumentElasticSearch document = new DocumentElasticSearch(doc.getId(), user.getCompany().getId(), doc.getFileType(), doc.getFileName(), doc.getFileContent(), descriptors);
                documentElasticSearchService.save(document);
            }
        }
    }

}
