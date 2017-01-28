/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nst.dms.domain.dto;

/**
 *
 * @author Jelena
 */
public class TreeDto {
    public static final String ACTION_ICON = "glyphicon glyphicon-ok";
    public static final String PROCESS_ICON = "glyphicon glyphicon-folder-open";
    private Long id;
    private String parent;
    private String text;
    private String icon;
    private boolean primitive;
    private boolean action = false;

    public TreeDto(Long id, String parent, String text, String icon) {
        this.id = id;
        this.parent = parent;
        this.text = text;
        this.icon = icon;
        this.action = true;
    }

    public TreeDto(Long id, String parent, String text, String icon, boolean primitive) {
        this.id = id;
        this.parent = parent;
        this.text = text;
        this.icon = icon;
        this.primitive = primitive;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getParent() { return parent; }
    public void setParent(String parent) { this.parent = parent; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public boolean isPrimitive() { return primitive; }
    public void setPrimitive(boolean primitive) { this.primitive = primitive; }
    public boolean isAction() { return action; }
    public void setAction(boolean action) { this.action = action; }
}
