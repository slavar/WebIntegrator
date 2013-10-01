<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View Logs</title>
        <link rel="stylesheet" href="/WebIntegrator/style.css" type="text/css">
        <script type='text/javascript' src='/WebIntegrator/dwr/engine.js'></script>
        <script type='text/javascript' src='/WebIntegrator/dwr/util.js'></script>
        <script language="JavaScript">
            function bodyOnLoad(){
                dwr.engine.setActiveReverseAjax(true);
            }
        </script>
    </head>
    <body onload="bodyOnLoad();">        
        <%@ include file="header.html" %>
        <table style="width:800px;" id="eventsTable">
          <caption>Replication Event List</caption>
          <thead>
            <tr>
              <th width="10%">Priority</th>
              <th width="15%">Replication Name</th>
              <th width="55%">Message</th>
              <th width="20%">Time</th>
            </tr>
          </thead>
          </table>
          <div style="overflow:auto; width:795px; height:400px; overflow-x:hidden;">
              <table>
                  <colgroup>
                        <col style="width:10%">
                        <col style="width:15%">
                        <col style="width:53%">
                        <col style="width:18%">
                    </colgroup>
                    <tbody id="eventsBody" style="WORD-BREAK:BREAK-ALL;">
                    </tbody>
              </table>
          </div>
    </body>
</html>
