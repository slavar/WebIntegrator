/*
 * Implements a server side of an "Add Replication" functionality
 */

package com.webintegrator.actions;

import com.webintegrator.FullSyncReplicator;
import com.webintegrator.conf.Factory;
import com.webintegrator.conf.Replication;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author slava
 */
public class AddReplicationAction extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        String srcPath = request.getParameter("srcPath");
        String dstServer = request.getParameter("dstServer");
        String dstPath = request.getParameter("dstPath");
        String dstPort = request.getParameter("dstPort");
        String name = request.getParameter("name");
        String dstUsername = request.getParameter("dstUsername");
        String dstPassword = request.getParameter("dstPassword");

        Factory factory = Factory.getInstance();
        Replication replication = null;
        if(request.getParameter("action").equals("edit")){
            String originalName = request.getParameter("originalName");
            Map <String, Replication> replications = factory.getReplications();
            replication = replications.get(originalName);
            replications.remove(originalName);
        }else{
            replication = new Replication();
        }

        replication.setName(name);
        replication.setSourcePath(srcPath);
        replication.setDestinationServer(dstServer);
        replication.setDestinationPort(dstPort);
        replication.setDestinationPath(dstPath);
        replication.setDestinationUsername(dstUsername);
        replication.setDestinationPassword(dstPassword);

        Map replications = factory.getReplications();
        replications.put(name, replication);
        factory.save();

        //run full sync replication once saved successfully
        Thread replicator = new FullSyncReplicator(replication);
        replicator.start();

        response.sendRedirect("ReplicationList.jsp");
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

}
