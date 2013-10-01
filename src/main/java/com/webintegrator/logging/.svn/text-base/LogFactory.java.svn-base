/*
 * This class implements a server side functionality of replication event viewer,
 * uses DWR AJAX library to deliver events to client/browser without need
 * of page refresh
 * Implemented as singleton to synchronize access to logging facility
 */

package com.webintegrator.logging;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.directwebremoting.Browser;
import org.directwebremoting.ui.dwr.Util;

/**
 *
 * @author slava
 */
public class LogFactory {
    private static LogFactory logFactory = null;
    private Logger logger = null;

    private LogFactory(){
        logger = Logger.getLogger(this.getClass().getName());
    }

    public static synchronized LogFactory getInstance(){
        if(logFactory == null){
            logFactory = new LogFactory();
        }

        return logFactory;
    }

    public void addEvent(Priority priority, String replicationName, String message){
        AddRow addRow = new AddRow(priority.name(), replicationName, message);
        try{
            Browser.withPage("/WebIntegrator/ViewLogs.jsp", addRow);
        }catch(Exception exception){
            logger.log(Level.WARNING, null, exception);
        }
    }

    class AddRow implements Runnable{
        private String priority = null;
        private String replicationName = null;
        private String message = null;
        private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd");

        public AddRow(String priority, String replicationName, String message){
            this.priority = priority;
            this.replicationName = replicationName;
            this.message = message;
        }

        public void run() {
            String data[][] = new String[][]{
                {priority,replicationName,message, dateFormat.format(new Date())},
                {"","","",""},
                {"","","",""},
                {"","","",""}
            };

              String customMethod = "{rowCreator:function(options) {" +
                    "if(options.rowNum == 0){" +                            
                            "var tbl = document.getElementById('eventsBody');" +
                            "if(tbl.rows.length > 50){" +
                                "tbl.deleteRow(50);" +
                            "}" +
                            "var _row = tbl.insertRow(0);" +
                            "var _cell = _row.insertCell(0);" +
                            "_cell.innerHTML=options.rowData[0];" +
                            "_cell = _row.insertCell(1);" +
                            "_cell.innerHTML=options.rowData[1];" +
                            "_cell = _row.insertCell(2);" +
                            "_cell.innerHTML=options.rowData[2];" +
                            "_cell = _row.insertCell(3);" +
                            "_cell.innerHTML=options.rowData[3];" +
                            "if(tbl.rows.length % 2){" +
                                "_row.className = 'odd';" +
                            "}" +
                            "return null;" +
                    "}}" +
            "}";

            Util.addRows("eventsBody",data , customMethod);
        }

    }

}
