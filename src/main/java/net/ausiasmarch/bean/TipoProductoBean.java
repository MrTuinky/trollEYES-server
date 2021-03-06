package net.ausiasmarch.bean;

import com.google.gson.annotations.Expose;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import net.ausiasmarch.exceptions.MyException;

public class TipoProductoBean implements BeanInterface {

    @Expose
    private Integer id;
    @Expose
    private String descripcion;
     @Expose(deserialize = false)
    private Integer link_producto;

    public Integer getLink_producto() {
        return link_producto;
    }

    public void setLink_producto(Integer link_producto) {
        this.link_producto = link_producto;
    }
    
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public TipoProductoBean fill(ResultSet oResultSet, Connection oConnection,
            int spread, UsuarioBean oUsuarioBeanSession) throws Exception {
        this.setId(oResultSet.getInt("id"));
        this.setDescripcion(oResultSet.getString("descripcion"));
        return this;
    }

    @Override
    public PreparedStatement orderSQL(List<String> orden, PreparedStatement oPreparedStatement) throws Exception {
        for (int i = 1; i < orden.size(); i++) {
            if (orden.get((i - 1)).equalsIgnoreCase("id")) {
                oPreparedStatement.setInt(i, 1);
            } else if (orden.get((i - 1)).equalsIgnoreCase("descripcion")) {
                oPreparedStatement.setInt(i, 2);
            }
        }
        return oPreparedStatement;
    }

    @Override
    public String getFieldInsert() {
        return " (descripcion) VALUES(?)";
    }

     private String getFieldFilter(String campo) {
        return " OR " + campo + " LIKE ? ";
    }

    @Override
    public String getFieldConcat() {
        
        return " id LIKE ? " + 
                getFieldFilter("descripcion");
    }
    @Override
    public PreparedStatement setFilter(int numparam,
            PreparedStatement oPreparedStatement,String word,
            int rpp, int offset) throws Exception {
        for (int i=0;i<=1;i++){
                            oPreparedStatement.setString(++numparam, word);
        }
        oPreparedStatement.setInt(++numparam, rpp);
        oPreparedStatement.setInt(++numparam, offset);
        return oPreparedStatement;
    }
    
    @Override
    public PreparedStatement setFieldInsert(BeanInterface oBeanParam, PreparedStatement oPreparedStatement)
            throws Exception {
        TipoProductoBean oTipoProductoBean = (TipoProductoBean) oBeanParam;
        oPreparedStatement.setString(1, oTipoProductoBean.getDescripcion());
        return oPreparedStatement;
    }

    @Override
    public String getFieldUpdate() {
        return " descripcion=? ";
    }

    @Override
    public PreparedStatement setFieldUpdate(BeanInterface oBeanParam, PreparedStatement oPreparedStatement)
            throws Exception {
        TipoProductoBean oTipoProductoBean = (TipoProductoBean) oBeanParam;
        oPreparedStatement.setString(1, oTipoProductoBean.getDescripcion());
        oPreparedStatement.setInt(2, oTipoProductoBean.getId());
        return oPreparedStatement;
    }

     @Override
    public String getFieldLink() {
       return "link_producto";
    }

    @Override
    public String getFieldId(String filter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public PreparedStatement setFieldId(int numparam,
            PreparedStatement oPreparedStatement, int id, int rpp,int offset) throws Exception {
        oPreparedStatement.setInt(++numparam, id);
        oPreparedStatement.setInt(++numparam, rpp);
        oPreparedStatement.setInt(++numparam, offset);
        return oPreparedStatement;
    }
    
    @Override
    public String getFieldOrder(String orden) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
