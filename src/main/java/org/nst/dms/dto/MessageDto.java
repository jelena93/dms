/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.dto;

/**
 *
 * @author Jelena
 */
public class MessageDto {

    public static String MESSAGE_TYPE_SUCCESS = "alert-success";
    public static String MESSAGE_TYPE_ERROR = "alert-danger";
    public static String MESSAGE_TYPE_QUESTION  = "question";

    private String messageType;
    private String messageText;

    public MessageDto(String messageType, String messageText) {
        this.messageType = messageType;
        this.messageText = messageText;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    @Override
    public String toString() {
        return "MessageDto{" + "messageType=" + messageType + ", messageText=" + messageText + '}';
    }

}
