/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ausiasmarch.controller;

import javax.servlet.http.HttpServlet;
import net.ausiasmarch.helper.TraceHelper;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author a035595797p
 */
public class Log4jInit extends HttpServlet {

    @Override
    public void init() {

        String prefix = getServletContext().getRealPath("/");
        String file = getInitParameter("log4j-trolleyes");

        if (file != null) {
            PropertyConfigurator.configure(prefix + file);
            TraceHelper.trace("Log4J Logging started: " + prefix + file);
        } else {
            TraceHelper.trace("Log4J Is not configured for your Application: " + prefix + file);
        }
    }

}
