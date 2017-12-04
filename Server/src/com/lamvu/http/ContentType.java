/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lamvu.http;

/**
 *
 * @author lamvu
 */
public enum ContentType {
    CSS("CSS"),
    GIF("GIF"),
    HTM("HTM"),
    HTML("HTML"),
    ICO("ICO"),
    JPG("JPG"),
    JPEG("JPEG"),
    PNG("PNG"),
    TXT("TXT"),
    XML("XML");

    private String extension;

    ContentType(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        switch (this) {
            case CSS:
                return "Content-Type: text/css";
            case GIF:
                return "Content-Type: image/gif";
            case HTM:
            case HTML:
                return "Content-Type: text/html";
            case ICO:
                return "Content-Type: image/gif";
            case JPG:
            case JPEG:
                return "Content-Type: image/jpeg";
            case PNG:
                return "Content-Type: image/png";
            case TXT:
                return "Content-Type: text/plain";
            case XML:
                return "Content-Type: text/xml";
            default:
                return null;
        }
    }
}
