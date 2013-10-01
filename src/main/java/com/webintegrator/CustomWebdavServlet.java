/*
 * This class extends regular Tomcat Webdav servlet,
 * we need this hack it since original servlet doesn't allow to operate
 * with context root directory
 */

package com.webintegrator;

import java.io.File;
import java.io.IOException;
import javax.naming.directory.DirContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.catalina.servlets.WebdavServlet;
import org.apache.naming.resources.FileDirContext;

/**
 *
 * @author slava
 */
public class CustomWebdavServlet extends WebdavServlet{

    @Override
    protected String getRelativePath(HttpServletRequest request) {
        String result = request.getPathInfo();
        File file = new File(result);
        while(file.getParentFile() != null){
            file = file.getParentFile();
        }

        String rootDrive = file.getPath();
        int appender = 0;
        if(! rootDrive.equals("/")){
            ++appender;
        }
        result = result.substring(result.indexOf(rootDrive) + rootDrive.length() + appender);

        return result;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        DirContext dirContext = resources.getDirContext();
        String tmpDocBase = ((FileDirContext)dirContext).getDocBase();
        //set to root to allow application accessing any path and
        //not only web application directory
        ((FileDirContext)dirContext).setDocBase("/");
        super.service(req, res);
        ((FileDirContext)dirContext).setDocBase(tmpDocBase);
    }
}
