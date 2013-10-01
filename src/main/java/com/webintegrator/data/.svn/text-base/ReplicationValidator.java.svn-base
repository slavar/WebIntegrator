/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.webintegrator.data;

import com.webintegrator.conf.Factory;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;

/**
 *
 * @author slava
 */
public class ReplicationValidator {
    public String validate(String name,
            String localPath,
            String dstServer,
            String dstPort,
            String dstPath,
            String dstUsername,
            String dstPassword){

            String retVal = null;

            Factory factory = Factory.getInstance();
            Map replications = factory.getReplications();
            if(replications.containsKey(name)){
                retVal = "Replication with the same name already exists";
            }

            try{
                FileSystemManager fsManager = VFS.getManager();
                FileObject fileObject = fsManager.resolveFile(localPath);
                if(! fileObject.exists()){
                    retVal = "Source location does not exist";
                }
            }catch(Exception exception){
                retVal = "Source location does not exist";
            }

            try{
                int port = Integer.parseInt(dstPort);
            }catch(NumberFormatException exception){
                retVal = "Enter a correct destination port number";
            }

            try{
                FileSystemManager fsManager = VFS.getManager();
                String remoteRoot = "webdav://" +
                    dstUsername + ":" +
                    dstPassword + "@" +
                    dstServer +
                    ":" + dstPort + "/WebIntegrator/webdav/" +
                    dstPath;
                FileObject fileObject = fsManager.resolveFile(remoteRoot);
                if(! fileObject.exists()){
                    retVal = "Remote location doesn not exist";
                }
            }catch(Exception exception){
                retVal = "Remote location doesn not exist";
            }

            return retVal;
    }
 
    public int credentialsValidation(String server, String port, String username, String password){
        int retVal = -1;
        try {
            Credentials defaultcreds = new UsernamePasswordCredentials(username, password);
            HttpClient httpClient = new HttpClient();
            httpClient.getParams().setAuthenticationPreemptive(true);
            //disable HTTP retries
            httpClient.getParams().setParameter(
                    HttpMethodParams.RETRY_HANDLER,
                    new DefaultHttpMethodRetryHandler(0, false));
            httpClient.getState().setCredentials(new AuthScope(server, Integer.parseInt(port), AuthScope.ANY_REALM, AuthScope.ANY_SCHEME), defaultcreds);
            HttpMethod method = new GetMethod("http://" + server + ":" + port + "/WebIntegrator");
            httpClient.executeMethod(method);
            retVal = method.getStatusCode();
        }catch(IOException iOException){
            retVal = 101;//101 stands for connection error
            Logger.getLogger(ReplicationValidator.class.getName()).log(Level.SEVERE, null, iOException);
        } catch (Exception ex) {
            Logger.getLogger(ReplicationValidator.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            return retVal;
        }
    }
}
