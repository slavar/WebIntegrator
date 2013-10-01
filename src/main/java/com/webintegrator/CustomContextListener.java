/*
 * This class is activated with start of web application
 * it will start all existing replications at once
 */

package com.webintegrator;

import com.webintegrator.conf.Factory;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.webintegrator.conf.Replication;

/**
 *
 * @author slava
 */
public class CustomContextListener implements ServletContextListener{
    
    public void contextInitialized(ServletContextEvent sce) {
        Factory factory = Factory.getInstance();
        Iterator replications = factory.getReplications().values().iterator();
        try {
            //iterate through replication list and
            //start replications one by one
            while(replications.hasNext()){
                Replication replication = (Replication)replications.next();                
                Thread replicator = new FullSyncReplicator(replication);
                replicator.start();
            }
        } catch (Exception ex) {
            Logger.getLogger(CustomContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
