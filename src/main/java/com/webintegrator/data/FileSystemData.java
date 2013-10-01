/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webintegrator.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import java.io.File;
import java.io.FileFilter;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;

/**
 *
 * @author slava
 */
public class FileSystemData extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String pathParam = request.getParameter("path");
        String server = request.getParameter("server");

        if (pathParam == null) {
            pathParam = "/";
        }

        try {
            if (! "localhost".equals(server) && server != null) {
                String dstPort = request.getParameter("port");
                String dstUsername = request.getParameter("username");
                String dstPassword = request.getParameter("password");
                Credentials defaultcreds = new UsernamePasswordCredentials(dstUsername, dstPassword);
                HttpClient httpClient = new HttpClient();
                httpClient.getParams().setAuthenticationPreemptive(true);
                httpClient.getState().setCredentials(new AuthScope(server, Integer.parseInt(dstPort), AuthScope.ANY_REALM, AuthScope.ANY_SCHEME), defaultcreds);
                HttpMethod method = new GetMethod("http://" +
                        server + ":" + dstPort +
                        request.getRequestURI() + "?path=" + pathParam);
                httpClient.executeMethod(method);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(method.getResponseBodyAsStream()));
                String line = null;
                while((line = reader.readLine()) != null){
                    out.println(line);
                }
            } else {
                if("/".equals(pathParam)){
                    out.println("{");
                    out.println("'items': [");

                    File[] roots = File.listRoots();
                    for(int i = 0; i < roots.length; i++){
                        if(roots[i].getAbsolutePath().equals("/")){
                            File[] subRoots = roots[i].listFiles(new DirectoryFileFilter());
                            for(int j = 0; j < subRoots.length; j++){
                                printChildren(out, subRoots[j], true);
                                out.print(",");
                            }
                        }else{
                            printChildren(out, roots[i], true);
                            out.print(",");
                        }
                    }

                    out.println("]");
                    out.println("}");
                }else{
                    File file = new File(pathParam);
                    printChildren(out, file, false);                    
                }
            }
        } finally {
            out.close();
        }
    }

    private void printChildren(PrintWriter out, File file, boolean root){
        String fileName = file.getName().replace('\\', '/');
        if("".equals(fileName)){
            fileName = file.getAbsolutePath().replace('\\', '/');
        }
        out.println("{'name':'" + fileName + "','directory':true,'path':'" + file.getAbsolutePath().replace('\\', '/') + "',");
        out.print("'children': [");

        File[] children = file.listFiles(new DirectoryFileFilter());
        boolean first = true;
        for(int i = 0; children != null && i < children.length; i++){
            if(! first){
                out.print(",");
            }
            out.print("'" + children[i].getName().replace('\\', '/') + "'");
            if(first){
                first = false;
            }
        }

        out.print("]");
        out.println("}");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    class DirectoryFileFilter implements FileFilter{

        public boolean accept(File file) {
            if(file.isDirectory() && ! file.isHidden() && (file.getName().indexOf("'") == -1)){
                return true;
            }else{
                return false;
            }
        }

    }
}
