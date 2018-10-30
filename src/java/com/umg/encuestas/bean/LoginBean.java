/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.umg.encuestas.bean;

import java.io.IOException;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

public class LoginBean implements Serializable {

    private static final long serialVersionUID = -2152389656664659476L;
    private String nombre = "";
    private String clave = "";
    private String tipo = "A";
    private boolean logeado = false;

    public LoginBean() {
        nombre = "";
        clave = "";
        tipo = "";
    }

    public boolean estaLogeado() {
        return logeado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void login(ActionEvent actionEvent) {
        RequestContext context = RequestContext.getCurrentInstance();
        FacesMessage msg = null;

        if (nombre != null && nombre.equals("admin") && clave != null
                && clave.equals("admin")) {
            tipo = "A";
            logeado = true;
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenid@", nombre);

        } else {
            logeado = false;
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Login Error",
                    "Credenciales no válidas");
        }
//        FacesContext.getCurrentInstance().addMessage(null, msg);
//        context.addCallbackParam("estaLogeado", logeado);

        if (logeado) {
            FacesContext context2 = FacesContext.getCurrentInstance();
            context2.getExternalContext().getSessionMap().put("loginBean", new LoginBean());
            HttpSession ses = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            ses.setAttribute("currentUser", nombre);
            ses.setAttribute("tipoUsuario", tipo);
            context.addCallbackParam("estaLogeado", logeado);
            if (tipo.toUpperCase().equals("A")) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/UMG-EncuestaCatedraticos/faces/pages/gestiones/inicioadmin.xhtml");
                } catch (IOException ex) {
                    System.out.println("Error al redireccionar");
                }
            } else if (tipo.toUpperCase().equals("B")) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("/UMG-EncuestaCatedraticos/faces/pages/gestiones/inicioalumno.xhtml");
                } catch (IOException ex) {
                    System.out.println("Error al redireccionar");
                }
            }
            //context.addCallbackParam("view", "./gauge.xhtml");
        }
    }

    public void logout() {
        System.out.println("Logout");
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
                .getExternalContext().getSession(false);
        session.invalidate();
        logeado = false;

        FacesContext context = FacesContext.getCurrentInstance();

        context.getExternalContext().getSessionMap().clear();
        try {
            context.getExternalContext().redirect("/UMG-EncuestaCatedraticos/faces/index.xhtml");
        } catch (IOException ex) {
            System.out.println("Error de logout [" + ex.getMessage() + "]");
        }
        System.out.println("logueado: " + logeado);
    }
}
