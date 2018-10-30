/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.umg.encuesta.util;

import org.primefaces.model.UploadedFile;

/**
 *
 * @author NESPINOZA
 */
public class ArchivosCarga {

    public ArchivosCarga() {
    }

    private String nombre;
    private UploadedFile archivo;

    public void setArchivo(UploadedFile archivo) {
        this.archivo = archivo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public UploadedFile getArchivo() {
        return archivo;
    }

    public String getNombre() {
        return nombre;
    }

}
