package net.ausiasmarch.dao.specificdao_0;

import net.ausiasmarch.dao.daointerface.DaoInterface;
import java.sql.Connection;
import net.ausiasmarch.bean.UsuarioBean;
import net.ausiasmarch.dao.genericdao.GenericDao;

public class ProductoDao_0 extends GenericDao implements DaoInterface {

    public ProductoDao_0(Connection oConnection,String ob, UsuarioBean oUsuarioBeanSession) {
        super(oConnection, "producto", oUsuarioBeanSession);
    }
}
