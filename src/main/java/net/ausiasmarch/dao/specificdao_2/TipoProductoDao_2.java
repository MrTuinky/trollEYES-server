package net.ausiasmarch.dao.specificdao_2;

import net.ausiasmarch.dao.daointerface.DaoInterface;
import java.sql.Connection;
import net.ausiasmarch.bean.UsuarioBean;
import net.ausiasmarch.dao.genericdao.GenericDao;

public class TipoProductoDao_2 extends GenericDao implements DaoInterface {

    public TipoProductoDao_2(Connection oConnection, String ob,UsuarioBean oUsuarioBeanSession) {
        super(oConnection, "tipo_producto", oUsuarioBeanSession);
    }
}
