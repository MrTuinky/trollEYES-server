package net.ausiasmarch.service.serviceinterface;

import java.sql.SQLException;
import net.ausiasmarch.exceptions.MyException;

public interface ServiceInterface {

    public String get() throws Exception;

    public String getCount() throws Exception;

    public String getPage() throws Exception;

    public String insert() throws Exception;

    public String remove() throws Exception;

    public String update() throws Exception;
    
}
