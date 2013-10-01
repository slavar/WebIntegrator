/*
 * This class is the application core and allows replication
 * of a single directory in directory walk mode
 */

package com.webintegrator;

import com.webintegrator.logging.LogFactory;
import com.webintegrator.logging.Priority;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.vfs.AllFileSelector;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.webdav.lib.WebdavFile;


/**
 *
 * @author slava
 */
public class ReplicatorClient{

    private FileObject localRoot = null;
    private FileObject remoteRoot = null;
    private String replicationName = null;

    /*
     * Yhis recursive function traverses directory tree and replicates it's content
     * to remote location
     */
    public void replicateDirectoryContent(FileObject currentDirectory) throws FileSystemException, InterruptedException{
        LogFactory logFactory = LogFactory.getInstance();
        FileObject[] children = currentDirectory.getChildren();
        for(int i = 0; i < children.length; i++){
            FileType type = children[i].getType();
            FileName fileName = children[i].getName();
            String relativeName = localRoot.getName().getRelativeName(fileName);
            FileObject remoteFile = remoteRoot.resolveFile(relativeName);

            if(remoteFile.exists() && type != remoteFile.getType()){
                remoteFile.delete(new AllFileSelector());
                logFactory.addEvent(Priority.INFO, replicationName, "Deleted: " + children[i].getName().getPath());
            }

            if(type.equals(FileType.FOLDER)){               
                if(! remoteFile.exists()){
                    try{
                        String url = remoteFile.getURL().toExternalForm().replaceFirst("webdav", "http");
                        HttpURL httpURL = new HttpURL(url);
                        new WebdavFile(httpURL).mkdir();
                    }catch(Exception e){}
                }
                replicateDirectoryContent(children[i]);
            }else{
                if(! remoteFile.exists()){
                    remoteFile.copyFrom(children[i], new AllFileSelector());
                    logFactory.addEvent(Priority.INFO, replicationName, "Copied: " + children[i].getName().getPath());
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public ReplicatorClient(String replicationName, FileObject localRoot, FileObject remoteRoot) {
        this.localRoot = localRoot;
        this.remoteRoot = remoteRoot;
        this.replicationName = replicationName;
    }
}


