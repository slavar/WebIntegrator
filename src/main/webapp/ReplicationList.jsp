<%-- 
    Document   : index
    Created on : Nov 5, 2008, 4:06:56 PM
    Author     : slava
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%
    com.webintegrator.conf.Factory factory = com.webintegrator.conf.Factory.getInstance();
    java.util.Map <String, com.webintegrator.conf.Replication> replications = factory.getReplications();
    java.util.Iterator<String> replicationNames = replications.keySet().iterator();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="/WebIntegrator/style.css" type="text/css">
        <title>Replication List</title>
    </head>
    <body>
    <%@ include file="header.html" %>
    <table width="800px">
        <caption>Replication List</caption>
        <thead>
        </thead>
        <tbody>
        <%while(replicationNames.hasNext()){%>
            <%
                String replicationName = replicationNames.next();
                com.webintegrator.conf.Replication replication = replications.get(replicationName);
            %>            
           <tr><td colspan="5"><%=replicationName %></td></tr>
           <tr class="odd">
               <td>Source: <%=replication.getSourcePath() %></td>
               <td>Destination: <%=replication.getDestinationServer() + ":" + replication.getDestinationPath()%></td>
               <td><input type="button" value="Edit" onclick="window.location='AddReplication.jsp?action=edit&replication=<%=replicationName%>'">
               <td><input type="button" value="Delete" onclick="window.location='ReplicationListAction?action=delete&replication=<%=replicationName%>'">
           </tr>
        <%}%>
        </tbody>
    </table>
    </body>
</html>
