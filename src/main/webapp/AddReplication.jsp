<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<%
String replicationName = "";
String srcPath = "";
String dstServer = "";
String dstPath = "";
String dstUsername = "";
String dstPassword = "";
String dstPort = "8080";

String action = request.getParameter("action");
replicationName = request.getParameter("replication");

if(action != null && action.equals("edit")){
    com.webintegrator.conf.Factory factory = com.webintegrator.conf.Factory.getInstance();
    java.util.Map <String, com.webintegrator.conf.Replication> replications = factory.getReplications();

    com.webintegrator.conf.Replication replication = replications.get(replicationName);
    srcPath = replication.getSourcePath();
    dstServer = replication.getDestinationServer();
    dstPort = replication.getDestinationPort();
    dstPath = replication.getDestinationPath();
    dstUsername = replication.getDestinationUsername();
    dstPassword = replication.getDestinationPassword();
}else
if(action == null){//add replication
    replicationName = "";
}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="/WebIntegrator/style.css" type="text/css">
        <script type='text/javascript' src='/WebIntegrator/dwr/interface/ReplicationValidator.js'></script>
        <script type='text/javascript' src='/WebIntegrator/dwr/engine.js'></script>
        <script type='text/javascript' src='/WebIntegrator/dwr/util.js'></script>
        <script type="text/javascript"
            djConfig="parseOnLoad:true, isDebug:false"
            src="dojo/dojo/dojo.js">
        </script>
        <script type="text/javascript">
            dojo.require("dojo.io.iframe");
            dojo.require("dojo._base.xhr");

            function validate(){
                var replicationName = dwr.util.getValue("name");
                var srcPath = dwr.util.getValue("srcPath");
                var dstServer = dwr.util.getValue("dstServer");
                var dstPort = dwr.util.getValue("dstPort");
                var dstPath = dwr.util.getValue("dstPath");
                var dstUsername = dwr.util.getValue("dstUsername");
                var dstPassword = dwr.util.getValue("dstPassword");

                var error = false;

                if(replicationName == ""){
                    var nameField = dwr.util.byId("name");
                    nameField.style.borderColor = "red";
                    nameField.style.color = "red";
                    nameField.value = "Please enter a replication name";

                    nameField.addEventListener("click", resetField, false);

                    error = true;
                }

                if(srcPath == ""){
                    var srcPathField = dwr.util.byId("srcPath");
                    srcPathField.style.borderColor = "red";
                    srcPathField.style.color = "red";
                    srcPathField.value = "Please enter a source path";

                    srcPathField.addEventListener("click", resetField, false);

                    error = true;
                }

                if(dstServer == ""){
                    var dstServerField = dwr.util.byId("dstServer");
                    dstServerField.style.borderColor = "red";
                    dstServerField.style.color = "red";
                    dstServerField.value = "Please enter a destination server name";

                    dstServerField.addEventListener("click", resetField, false);

                    error = true;
                }

                 if(dstServer == ""){
                    var dstServerField = dwr.util.byId("dstServer");
                    dstServerField.style.borderColor = "red";
                    dstServerField.style.color = "red";
                    dstServerField.value = "Please enter a destination server name";

                    dstServerField.addEventListener("click", resetField, false);

                    error = true;
                }

             if(dstPort == "" || dstPort != parseInt(dstPort)){
                    var dstPortField = dwr.util.byId("dstPort");
                    dstPortField.style.borderColor = "red";
                    dstPortField.style.color = "red";
                    dstPortField.value = "";
                    dstPortField.addEventListener("click", resetField, false);

                    error = true;
                }

                if(dstUsername == ""){
                    var dstUsernameField = dwr.util.byId("dstUsername");
                    dstUsernameField.style.borderColor = "red";
                    dstUsernameField.style.color = "red";
                    dstUsernameField.value = "Please enter a replication username";

                    dstUsernameField.addEventListener("click", resetField, false);

                    error = true;
                }

                if(error){
                    return;
                }

                ReplicationValidator.validate(
                    replicationName,
                    srcPath,
                    dstServer,
                    dstPort,
                    dstPath,
                    dstUsername,
                    dstPassword,
                    validationCallback
                );
            }

            function resetField(){
                this.value = "";
                this.removeEventListener("click",resetField,false);
                this.style.color ="#781351";
                this.style.border = "1px solid #781351";
            }

            function validationCallback(error){
                if(error != null){
                    alert(error);
                }else{
                    var form = dwr.util.byId("AddReplicationForm");
                    form.submit();
                }
            }

            function browseFS(){
                var dstServer = dwr.util.getValue("dstServer");
                if(dstServer == ""){
                    var dstServerField = dwr.util.byId("dstServer");
                    dstServerField.style.borderColor = "red";
                    dstServerField.style.color = "red";
                    dstServerField.value = "Please enter a destination server name";

                    dstServerField.addEventListener("click", resetField, false);
                    return;
                }else
                if(dstServer == "localhost"){
                    var dstServerField = dwr.util.byId("dstServer");
                    dstServerField.style.borderColor = "red";
                    dstServerField.style.color = "red";
                    dstServerField.value = "Please enter a non-local server name";

                    dstServerField.addEventListener("click", resetField, false);
                    return;
                }

                var dstPort = dwr.util.getValue("dstPort");
                if(dstPort == "" || dstPort != parseInt(dstPort)){
                    var dstPortField = dwr.util.byId("dstPort");
                    dstPortField.style.borderColor = "red";
                    dstPortField.style.color = "red";
                    dstPortField.value = "";

                    dstPortField.addEventListener("click", resetField, false);
                    return;
                }
                
                var dstUsername = dwr.util.getValue("dstUsername");
                var dstPassword = dwr.util.getValue("dstPassword");

                var dstPathField = dwr.util.byId("dstPath");
                dstPathField.value = "";
                dstPathField.removeEventListener("click",resetField,false);
                dstPathField.style.color ="#781351";
                dstPathField.style.border = "1px solid #781351";                                    

                ReplicationValidator.credentialsValidation(
                    dstServer,
                    dstPort,
                    dstUsername,
                    dstPassword,
                    credentialsCheckCallback
                );
            }

            function credentialsCheckCallback(error){
                switch(error){
                    case 401:
                        var dstUsernameField = dwr.util.byId("dstUsername");
                        dstUsernameField.style.borderColor = "red";
                        dstUsernameField.style.color = "red";
                        dstUsernameField.value = "Please enter a valid username";
                        dstUsernameField.addEventListener("click", resetField, false);

                        var dstPasswordField = dwr.util.byId("dstPassword");
                        dstPasswordField.style.borderColor = "red";
                        //dstPasswordField.style.color = "red";
                        dstPasswordField.value = "";
                        dstPasswordField.addEventListener("click", resetField, false);

                        break;
                    case 101:
                        var dstServerField = dwr.util.byId("dstServer");
                        dstServerField.style.borderColor = "red";
                        dstServerField.style.color = "red";
                        dstServerField.value = "Please enter a correct server address";
                        dstServerField.addEventListener("click", resetField, false);
                        break;
                    default:
                        window.open('FSBrowser.jsp?server=' + document.AddReplicationForm.dstServer.value +
                               '&port=' + document.AddReplicationForm.dstPort.value +
                               '&username=' + document.AddReplicationForm.dstUsername.value +
                               '&password=' + document.AddReplicationForm.dstPassword.value, null,
                               'width=300,height=450,menubar=no,toolbar=no,location=no,status=no,titlebar=no');
                } 
            }
        </script>
    </head>
    <body>
    <%@ include file="header.html" %>
    <form id="AddReplicationForm" name="AddReplicationForm" action="AddReplicationAction">
        <table width="800">
            <caption>Add/Edit Replication</caption>
            <tr>
                <td>Replication Name</td>
                <td>
                    <input type="text" id="name" name="name" size="32" value="<%=replicationName%>" />
                </td>
            </tr>         
            <tr>
                <td>Source Path</td>
                <td>
                    <input type="text" id="srcPath" name="srcPath" size="32" value="<%=srcPath%>"/>
                    <input type="button" value="Browse" onclick="window.open('FSBrowser.jsp',null, 'width=300,height=450');">
                </td>                
            </tr>
            <tr>
                <td>Destination Server</td>
                <td>
                    <input type="text" id="dstServer" name="dstServer" size="24" value="<%=dstServer%>" />
                    :<input type="text" id="dstPort" name="dstPort" size="2" value="<%=dstPort%>" />
                </td>
            </tr>
            <tr>
                <td>Username</td>
                <td>
                    <input type="text" id="dstUsername" name="dstUsername" size="32" value="<%=dstUsername%>" />
                </td>
            </tr>
            <tr>
                <td>Password</td>
                <td>
                    <input type="password" id="dstPassword" name="dstPassword" size="32" value="<%=dstPassword%>" />
                </td>
            </tr>
            <tr>
                <td>Destination Path</td>
                <td>
                    <input type="text" id="dstPath" name="dstPath" size="32" value="<%=dstPath%>"/>
                    <input type="button" value="Browse" onclick="browseFS();">
                </td>                
            </tr>
        </table>
        <input type="hidden" name="action" value="<%=action%>"/>
        <input type="hidden" name="originalName" value="<%=replicationName%>" />
        <input type="button" onclick="validate();" value="Submit" style="background: #CFD784;"/>
    </form>
    </body>
</html>
