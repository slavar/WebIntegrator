/*
 * This class instantiates an initial replication and sets
 * file system event listener
 */

package com.webintegrator;

import com.webintegrator.conf.Replication;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.impl.DefaultFileMonitor;

/**
 *
 * @author slava
 */
public class FullSyncReplicator extends Thread{
        private FileObject localRootObject = null;
        private FileObject remoteRootObject = null;
        private String replicationName = null;

        private String dstUsername = null;
        private String dstPassword = null;
        private int dstPort;

        public FullSyncReplicator(Replication replication) throws FileSystemException{
            String localRoot = replication.getSourcePath();
            dstUsername = replication.getDestinationUsername();
            dstPassword = replication.getDestinationPassword();
            dstPort = Integer.parseInt(replication.getDestinationPort());

            String remoteRoot = "webdav://" + 
                    dstUsername + ":" +
                    dstPassword + "@" +
                    replication.getDestinationServer() +
                    ":" + Integer.toString(dstPort) +
                    "/WebIntegrator/webdav/" +
                    replication.getDestinationPath();
            FileSystemManager fsManager = VFS.getManager();
            localRootObject = fsManager.resolveFile(localRoot);
            remoteRootObject = fsManager.resolveFile(remoteRoot);
            replicationName = replication.getName();
        }

        @Override
        public void run() {
            ReplicatorClient replicatorClient = new ReplicatorClient(replicationName, localRootObject, remoteRootObject);
            try{
                replicatorClient.replicateDirectoryContent(localRootObject);

                CustomFileListener customFileListener = new CustomFileListener(replicationName, localRootObject, remoteRootObject);
                DefaultFileMonitor fm = new DefaultFileMonitor(customFileListener);
                fm.setRecursive(true);
                fm.addFile(localRootObject);
                fm.start();
            }catch(Exception exception){
                Logger.getLogger(CustomContextListener.class.getName()).log(Level.SEVERE, null, exception);
            }
        }
}
