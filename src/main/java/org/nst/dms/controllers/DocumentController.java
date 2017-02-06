/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.controllers;

import org.nst.dms.dto.MessageDto;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.nst.dms.controllers.exceptions.CustomException;
import org.nst.dms.domain.Activity;
import org.nst.dms.domain.DocumentType;
import org.nst.dms.service.DescriptorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.nst.dms.service.DocumentTypeService;
import org.springframework.web.multipart.MultipartFile;
import org.nst.dms.service.ActivityService;
import org.springframework.util.FileCopyUtils;

/**
 *
 * @author Hachiko
 */
@Controller
@RequestMapping("/documents")
public class DocumentController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private DescriptorService descriptorService;
    @Autowired
    ServletContext context;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public ModelAndView save() throws MalformedURLException, URISyntaxException {
        ModelAndView mv = new ModelAndView("add_document");
        List<DocumentType> documentTypes = documentTypeService.findAll();
        mv.addObject("documentTypes", documentTypes);
        mv.addObject("action_type_processes_search", "add_document");
       String url =""+this.getClass().getClassLoader().getResource(".").getPath();
//        if(context.getResource("/resources/css/style.css")!=null)
//         url=context.getResource("/resources/css/style.css").getPath();
//        else
//            url=context.getRealPath("resources")+"ili"+context.getRealPath("/resources/")+"shit";
        mv.addObject("message", new MessageDto(MessageDto.MESSAGE_TYPE_ERROR, url));
        return mv;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ModelAndView save(String inputOutput, MultipartFile file, long docType, HttpServletRequest request, long activityID) {
        Activity activity = activityService.find(activityID);
        String url = saveFile(file);
        System.out.println("@url"+url);
//        DocumentType documentType = documentTypeService.find(docType);
//        List<Descriptor> descriptors = documentType.getDescriptors();
//        List<Descriptor> newDescriptors = new ArrayList<>();
//        List<Descriptor> existingDescriptors = descriptorService.getDescriptorValuesForDocumentType(docType);
//        int numberOfIdenticalDescriptors = 0;
//        for (Descriptor descriptor : descriptors) {
//            String key = descriptor.getDescriptorKey();
//            String value = request.getParameter(key);
//            Descriptor newDescriptor = new Descriptor(key, value, docType, descriptor.getDescriptorType());
//            newDescriptors.add(newDescriptor);
//            //@TODO napisati equals metodu koja proverava key-value a ne id i radice bolje?
//            for (Descriptor existingDescriptor : existingDescriptors) {
//                if(newDescriptor.getDescriptorKey().equals(existingDescriptor.getDescriptorKey()) && newDescriptor.getDescriptorValue().equals(existingDescriptor.getDescriptorValue())) numberOfIdenticalDescriptors++;
//            }
//        }
//        //@TODO neki dijalog da li ste sigurni da zelite da pregazite fajl il nesto tako?
//        if(numberOfIdenticalDescriptors == descriptors.size()) throw new CustomException("Document already exists", "500");
//        Document document = new Document(url);
//        document.setDescriptors(newDescriptors);
//        if(inputOutput.equals("input")) activity.getInputList().add(document);
//        else activity.getOutputList().add(document);
//        documentTypeService.save(documentType);
//        activityService.save(activity);
        return new ModelAndView("add_document", "message", new MessageDto(MessageDto.MESSAGE_TYPE_SUCCESS, "Document successfully added to "+url));
    }

    private String saveFile(MultipartFile file) {
        File uploadedFile = null;
        try {
            byte[] bytes = file.getBytes();
//            String webappRoot = request.getServletContext().getRealPath("/");
             uploadedFile = new File(request.getServletContext().getRealPath("/WEB-INF/resources"), file.getOriginalFilename());
            try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile))) {
                stream.write(bytes);
            }
            return uploadedFile.getPath()+"   "+request.getServletContext().getContextPath()+" ili "+
                    this.getClass().getResource("/").getPath()+ " - ";
        } catch (IOException e) {
            throw new CustomException(uploadedFile.getPath()+" ili "+request.getServletContext().getContextPath()+" ili "+
                    this.getClass().getResource("/").getPath()+ " ili " + e.getMessage(), "500");
        }
    }
}
