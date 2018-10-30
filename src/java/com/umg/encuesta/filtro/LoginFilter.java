/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.umg.encuesta.filtro;

import com.umg.encuestas.bean.LoginBean;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author NESPINOZA
 */
public class LoginFilter implements Filter {

    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;

    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        //System.out.println("dofilter");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
        res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
        HttpSession session = req.getSession(true);
        String pageRequested = req.getRequestURL().toString();
        // Obtengo el bean que representa el usuario desde el scope sesión
        LoginBean loginBean = (LoginBean) req.getSession().getAttribute("loginBean");

        //Proceso la URL que está requiriendo el cliente
        String urlStr = req.getRequestURL().toString().toLowerCase();
        boolean noProteger = noProteger(urlStr);
        boolean noProtegerAlumno = noProtegerDeAlumno(urlStr);
        System.out.println(urlStr + " - desprotegido=[" + noProteger + "]");
        System.out.println(urlStr + " - desprotegido alumno=[" + noProtegerAlumno + "]");

        //Si no requiere protección continúo normalmente.
        if (session.getAttribute("tipoUsuario") == "B") {
            System.out.println("restriccion alumno");
            if (noProtegerDeAlumno(urlStr)) {
                System.out.println("restriccion alumno true");
                chain.doFilter(request, response);
                return;

            } else {
                System.out.println("restriccion alumno false");
                res.sendRedirect(req.getContextPath() + "/faces/pages/gestiones/sinpermisosalumno.xhtml");
                return;
            }

        } else {
            if (noProteger(urlStr)) {
                chain.doFilter(request, response);
                return;
            }

            //El usuario no está logueado
            System.out.println("usuario: [" + session.getAttribute("currentUser") + "] tipo: [" + session.getAttribute("tipoUsuario") + "]");
            if (session.getAttribute("currentUser") == null) {
                System.out.println("usuario no esta logueado");
                res.sendRedirect(req.getContextPath() + "/faces/index.xhtml");
                return;
            }

            //El recurso requiere protección, pero el usuario ya está logueado.
            chain.doFilter(request, response);
        }

    }

    private boolean noProteger(String urlStr) {

        /*
 * Este es un buen lugar para colocar y programar todos los patrones que
 * creamos convenientes para determinar cuales de los recursos no
 * requieren protección. Sin duda que habría que crear un mecanizmo tal
 * que se obtengan de un archivo de configuración o algo que no requiera
 * compilación.
         */
        if (urlStr.endsWith("/")) {
            return true;
        }
        if (urlStr.endsWith("index.xhtml")) {
            return true;
        }

        if (urlStr.contains("/javax.faces.resource/")) {
            return true;
        }

        if (urlStr.contains("jsessionid")) {
            return true;
        }

        return false;
    }

    private boolean noProtegerDeAlumno(String urlStr) {

        if (urlStr.endsWith("/")) {
            return true;
        }
        if (urlStr.endsWith("index.xhtml")) {
            return true;
        }

        if (urlStr.contains("/javax.faces.resource/")) {
            return true;
        }

        if (urlStr.contains("jsessionid")) {
            return true;
        }

        if (urlStr.endsWith("inicioalumno.xhtml")) {
            return true;
        }
        if (urlStr.endsWith("sinpermisosalumno.xhtml")) {
            return true;
        }
        return false;
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

}
