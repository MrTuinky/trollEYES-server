/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.ausiasmarch.service.specificservice_0;

import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.gson.Gson;
import java.sql.Connection;
import java.util.Collections;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.ausiasmarch.bean.ResponseBean;
import net.ausiasmarch.bean.UsuarioBean;
import net.ausiasmarch.connection.ConnectionInterface;
import net.ausiasmarch.dao.specificdao_0.UsuarioDao_0;
import net.ausiasmarch.exceptions.MyException;
import net.ausiasmarch.factory.ConnectionFactory;
import net.ausiasmarch.factory.GsonFactory;
import net.ausiasmarch.helper.Log4jHelper;
import net.ausiasmarch.service.genericservice.GenericService;
import net.ausiasmarch.service.serviceinterface.ServiceInterface;
import net.ausiasmarch.setting.ConnectionSettings;

/**
 *
 * @author alejandro
 */
public class UsuarioService_0 extends GenericService implements ServiceInterface {

    ResponseBean oResponseBean = null;
    UsuarioBean oUsuarioBeanSession;
    ConnectionInterface oConnectionImplementation = null;
    Connection oConnection = null;
    Gson oGson = GsonFactory.getGson();
    HttpSession oSession = oRequest.getSession();

    public UsuarioService_0(HttpServletRequest oRequest) {
        super(oRequest);
        ob = oRequest.getParameter("ob");
    }
    
    public String login2Validate() throws Exception {
        UsuarioBean oUsuarioBean;
        String token = oRequest.getParameter("token");
        String login = oRequest.getParameter("username");
        try {
            if (token != null) {
                oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
                oConnection = oConnectionImplementation.newConnection();
                UsuarioDao_0 oUsuarioDao = new UsuarioDao_0(oConnection, "usuario", oUsuarioBeanSession);
                if (oUsuarioDao.validate(login, token)) {
                    String password = oRequest.getParameter("password");
                    oUsuarioBean = oUsuarioDao.get(login, password);
                    if (oUsuarioBean != null) {
                        if (oRequest.getParameter("username").equals(oUsuarioBean.getLogin()) && oRequest.getParameter("password").equalsIgnoreCase(oUsuarioBean.getPassword())) {
                            if(oUsuarioDao.userValidate(oUsuarioBean.getLogin())){
                            oSession.setAttribute("usuario", oUsuarioBean);
                            oUsuarioBean.setValidate(true);    
                            oResponseBean = new ResponseBean(200, "Welcome");
                            }else{
                            oResponseBean = new ResponseBean(500, "no ha podido validar el usuario");
                            }
                        } else {
                            oResponseBean = new ResponseBean(500, "Wrong password");
                        }
                    } else {
                        oResponseBean = new ResponseBean(500, "Wrong password");
                    }
                } else {
                    oResponseBean = new ResponseBean(500, "No ha podido validar");
                }

            } else {
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                        .setAudience(Collections.singletonList("217071584820-iu9ks9m7alnc8jpbbm9if66ipitcm6hk.apps.googleusercontent.com")).build();
                GoogleIdToken idToken = verifier.verify(token);

                if (idToken != null) {
                    String email = idToken.getPayload().getEmail();
                    int index = email.indexOf('@');
                    String username = email.substring(0, index);
                    oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
                    oConnection = oConnectionImplementation.newConnection();
                    UsuarioDao_0 oUsuarioDao = new UsuarioDao_0(oConnection, "usuario", oUsuarioBeanSession);
                    oUsuarioBean = oUsuarioDao.get(username);

                    if (oUsuarioBean != null) {
                        oSession.setAttribute("usuario", oUsuarioBean);
                        oResponseBean = new ResponseBean(200, "Welcome to Trolleyes");
                    } else {
                        if (oUsuarioDao.insert(email, username) == 0) {
                            oResponseBean = new ResponseBean(400, "The account could not be created");
                        } else {
                            oSession.setAttribute("usuario", oUsuarioBean);
                            oResponseBean = new ResponseBean(200, "Welcome to Trolleyes");
                        }
                    }
                } else {
                    oResponseBean = new ResponseBean(500, "Authentication failed");
                }

            }
            return oGson.toJson(oResponseBean);
        } catch (MyException ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName() + " ob:" + ob;
            Log4jHelper.errorLog(msg, ex);
            ex.addDescripcion(msg);
            throw ex;
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        }
    }

    public String login() throws Exception {
        UsuarioBean oUsuarioBean;
        String token = oRequest.getParameter("token");
        try {
            if (token == null || token.equalsIgnoreCase("")){
                oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
                oConnection = oConnectionImplementation.newConnection();
                UsuarioDao_0 oUsuarioDao = new UsuarioDao_0(oConnection, "usuario", oUsuarioBeanSession);
                String login = oRequest.getParameter("username");
                String password = oRequest.getParameter("password");
                oUsuarioBean = oUsuarioDao.get(login, password);

                if (oUsuarioBean != null) {
                    if (oRequest.getParameter("username").equals(oUsuarioBean.getLogin()) && oRequest.getParameter("password").equalsIgnoreCase(oUsuarioBean.getPassword())) {
                        oSession.setAttribute("usuario", oUsuarioBean);
                        oResponseBean = new ResponseBean(200, "Welcome");
                    } else {
                        oResponseBean = new ResponseBean(500, "Wrong password");
                    }
                } else {
                    oResponseBean = new ResponseBean(500, "Wrong password");
                }
            }else {
                GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                        .setAudience(Collections.singletonList("217071584820-iu9ks9m7alnc8jpbbm9if66ipitcm6hk.apps.googleusercontent.com")).build();       
                GoogleIdToken idToken = verifier.verify(token);
                
                if (idToken != null) {
                    String email = idToken.getPayload().getEmail();
                    int index = email.indexOf('@');
                    String username = email.substring(0, index);
                    oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
                    oConnection = oConnectionImplementation.newConnection();
                    UsuarioDao_0 oUsuarioDao = new UsuarioDao_0(oConnection, "usuario", oUsuarioBeanSession);
                    oUsuarioBean = oUsuarioDao.get(username);

                    if (oUsuarioBean != null) {
                        oSession.setAttribute("usuario", oUsuarioBean);
                        oResponseBean = new ResponseBean(200, "Welcome to Trolleyes");
                    } else {
                        if (oUsuarioDao.insert(email, username) == 0) {
                            oResponseBean = new ResponseBean(400, "The account could not be created");
                        } else {
                            oSession.setAttribute("usuario", oUsuarioBean);
                            oResponseBean = new ResponseBean(200, "Welcome to Trolleyes");
                        }
                    }
                } else {
                    oResponseBean = new ResponseBean(500, "Authentication failed");
                }

            }
            return oGson.toJson(oResponseBean);
        } catch (MyException ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName() + " ob:" + ob;
            Log4jHelper.errorLog(msg, ex);
            ex.addDescripcion(msg);
            throw ex;
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        }
    }

    public String logout() {
        oRequest.getSession().invalidate();
        oResponseBean = new ResponseBean(200, "No active session");
        return oGson.toJson(oResponseBean);
    }

    public String check() throws Exception {
        try {
            oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
            oConnection = oConnectionImplementation.newConnection();
            UsuarioBean oUsuarioBean;
            oUsuarioBean = (UsuarioBean) oSession.getAttribute("usuario");
            UsuarioDao_0 oUsuarioDao = new UsuarioDao_0(oConnection, "usuario", oUsuarioBeanSession);

            if (oUsuarioBean == null) {
                oResponseBean = new ResponseBean(500, "No autorizado");
            } else {
                oUsuarioBean = oUsuarioDao.get(oUsuarioBean.getLogin());
                return "{\"status\":200,\"message\":" + oGson.toJson(oUsuarioBean) + "}";
            }

        } catch (MyException ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName() + " ob:" + ob;
            Log4jHelper.errorLog(msg, ex);
            ex.addDescripcion(msg);
            throw ex;
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        }
        return oGson.toJson(oResponseBean);
    }

    public String signup() throws Exception {
        try {
            oConnectionImplementation = ConnectionFactory.getConnection(ConnectionSettings.connectionPool);
            oConnection = oConnectionImplementation.newConnection();
            UsuarioDao_0 oUsuarioDao = new UsuarioDao_0(oConnection, "usuario", oUsuarioBeanSession);
            UsuarioBean oUsuarioBean = new UsuarioBean();
            oUsuarioBean.setDni(oRequest.getParameter("dni"));
            oUsuarioBean.setNombre(oRequest.getParameter("nombre"));
            oUsuarioBean.setApellido1(oRequest.getParameter("apellido1"));
            oUsuarioBean.setApellido2(oRequest.getParameter("apellido2"));
            oUsuarioBean.setLogin(oRequest.getParameter("username"));
            oUsuarioBean.setPassword(oRequest.getParameter("password"));
            oUsuarioBean.setEmail(oRequest.getParameter("email"));
            // GENERAS EL TOKEN QUE LE ENVIAS POR CORREO
            oUsuarioBean.setToken(generaToken());
            oUsuarioBean.setValidate(Boolean.FALSE);
            String token = oUsuarioBean.getToken();
            String email = oRequest.getParameter("email");
            String username = oRequest.getParameter("username");
            oUsuarioDao.register(oUsuarioBean);
           /* String emailText = "Hola, " + username + "\n Hemos recibido su solicitud para acceder a TrollEyes. Si has sido tú, accede al siguienete enlace para la confirmación: http://localhost:8080/trollEYES-client-master/loginValidar/" + token + "\n Si no has solicitado el registro, puedes ignorar este correo.";
            EmailRegister.sendEmail("trolleyesclientmaster@gmail.com", email, "soycarlos", "Registro en TrollEyes, se necesita confirmación", emailText, "html");*/
            
            oResponseBean = new ResponseBean(200, "Usuario registrado con exito, falta VER SI ESTA o validar");
        } catch (MyException ex) {
            String msg = this.getClass().getName() + ":" + (ex.getStackTrace()[0]).getMethodName() + " ob:" + ob;
            Log4jHelper.errorLog(msg, ex);
            ex.addDescripcion(msg);
            throw ex;
        } finally {
            if (oConnection != null) {
                oConnection.close();
            }
            if (oConnectionImplementation != null) {
                oConnectionImplementation.disposeConnection();
            }
        }
        return oGson.toJson(oResponseBean);
    }

    private String generaToken() {
        int numAleatorio = (int) Math.floor(Math.random() * (100000 - 999999) + 999999);
        String tokenAleatorio = String.valueOf(numAleatorio) + "CARLOSHDZ";
        return tokenAleatorio;
    }
}
