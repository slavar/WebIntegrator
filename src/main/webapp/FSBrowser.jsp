<%
String server = request.getParameter("server");
if(server == null){
    server = "localhost";
}
String port = request.getParameter("port");
String username = request.getParameter("username");
String password = request.getParameter("password");
%>
<html>
<head>
    <title>File System Browser</title>
    <style type="text/css">
        @import "dojo/dojo/resources/dojo.css";
        @import "dojo/dijit/themes/tundra/tundra.css";

        .tundra .dijitLeaf {
            background: url('dojo/dijit/themes/tundra/images/folderClosed.gif') no-repeat;
        }
    </style>
    <script type="text/javascript"
            djConfig="parseOnLoad:true, isDebug:false"
            src="dojo/dojo/dojo.js">
    </script>
    <script type="text/javascript">
        dojo.require("dijit.Tree");
        dojo.require("dojox.data.FileStore");
        dojo.require("dijit.Tooltip");

        function bodyOnLoad(){
           dojo.connect(dijit.byId("tree").domNode, "ondblclick", function(evt){
                var selectedNode = dijit.getEnclosingWidget(evt.target),
                    selectedItem = selectedNode.item;
                    console.debug(selectedItem.path);
                <%if("localhost".equals(server)){%>
                    window.opener.document.AddReplicationForm.srcPath.value = selectedItem.path;
                <%}else{%>
                    window.opener.document.AddReplicationForm.dstPath.value = selectedItem.path;
                <%}%>
                    window.close();
            });
        }
    </script>
</head>
<body class="tundra" onload="bodyOnLoad();">
    <div style="width:290px;height:440px;overflow:auto">
        <div dojoType="dojox.data.FileStore" url="FileSystemData?server=<%=server%>&port=<%=port%>&username=<%=username%>&password=<%=password%>" pathAsQueryParam="true" options="dirsOnly" jsId="dojoFiles"></div>
        <div dojoType="dijit.tree.ForestStoreModel" jsId="fileModel" store="dojoFiles" query="{}" rootId="DojoFiles" rootLabel="<%=server%> Directory Tree" childrenAttrs="children" persist="false"></div>
            <div id="tree" dojoType="dijit.Tree" model="fileModel" persist="false">
            </div>
            <div dojoType="dijit.Tooltip"
                connectId="tree"
                label="Double click on folder to select it">
            </div>
    </div>
</body>
</html>