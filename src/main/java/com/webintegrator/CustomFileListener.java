/*
 * This class keeps callbacks for file system events
 * A replication will be triggered once event is thrown
 */
package com.webintegrator;

import com.webintegrator.logging.LogFactory;
import com.webintegrator.logging.Priority;
import org.apache.commons.httpclient.HttpURL;
import org.apache.commons.vfs.AllFileSelector;
import org.apache.commons.vfs.FileChangeEvent;
import org.apache.commons.vfs.FileListener;
import org.apache.commons.vfs.FileName;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileType;
import org.apache.webdav.lib.WebdavFile;

/**
 *
 * @author slava
 */
public class CustomFileListener implements FileListener {
    //keeps local root file object
    private FileObject localRoot = null;
    //keeps remote file object
    private FileObject remoteRoot = null;
    //replication name
    private String replicationName = null;

    public CustomFileListener(String replicationName, FileObject localRoot, FileObject remoteRoot) {
        this.localRoot = localRoot;
        this.remoteRoot = remoteRoot;
        this.replicationName = replicationName;
    }

    /*
     * callback for file creation event
     */
    public void fileCreated(FileChangeEvent event) throws Exception {
        FileObject changedFile = event.getFile();
        copyFile(changedFile);
    }

    /* 
     * 'file deleted' callback
     * if local file has been deleted,
     * remote file will be deleted too
     */
    public void fileDeleted(FileChangeEvent event) throws Exception {
        FileObject changedFile = event.getFile();
        FileName fileName = changedFile.getName();
        String relativeName = localRoot.getName().getRelativeName(fileName);
        FileObject remoteFile = remoteRoot.resolveFile(relativeName);

        String url = remoteFile.getURL().toExternalForm().replaceFirst("webdav", "http");
        HttpURL httpURL = new HttpURL(url);
        new WebdavFile(httpURL).delete();

        LogFactory logFactory = LogFactory.getInstance();
        logFactory.addEvent(Priority.INFO, replicationName, "Deleted: " + changedFile.getName().getPath());
    }

    /*
     * 'file changed' callback
     * changed file will be copied to destination
     */
    public void fileChanged(FileChangeEvent event) throws Exception {
        FileObject changedFile = event.getFile();
        copyFile(changedFile);
    }

    /*
     * A basic method for remote file copy
     */
    private void copyFile(FileObject localFile) throws Exception {
        FileName fileName = localFile.getName();
        String relativeName = localRoot.getName().getRelativeName(fileName);
        FileObject remoteFile = remoteRoot.resolveFile(relativeName);

        // either remote file doesn't exist or local file is newer
        if (!remoteFile.exists() || 
                localFile.getContent().getLastModifiedTime() >
                remoteFile.getContent().getLastModifiedTime()) {
            LogFactory logFactory = LogFactory.getInstance();
            if (localFile.getType().equals(FileType.FOLDER)) {
                String url = remoteFile.getURL().toExternalForm().replaceFirst("webdav", "http");
                HttpURL httpURL = new HttpURL(url);
                new WebdavFile(httpURL).mkdir();
                logFactory.addEvent(Priority.INFO, replicationName, "Created Directory: " + localFile.getName().getPath());
            } else {
                remoteFile.copyFrom(localFile, new AllFileSelector());
                logFactory.addEvent(Priority.INFO, replicationName, "Copied: " + localFile.getName().getPath());
            }
        }
    }
}
