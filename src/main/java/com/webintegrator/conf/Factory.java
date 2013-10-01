/*
 * This class implements application configuration storage
 * in form of object serialization into XML file
 * Implemented as singleton since we need to synchronize access to
 * configuration data and avoid concurrent modification
 *
 */

package com.webintegrator.conf;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author slava
 */
public class Factory {
    private static Factory factory = null;
    private Map<String,Replication> replications = null;

    private static final String CONFIGURATION_FILE = "configuration.xml";

    private Factory(){
        XMLDecoder xmlDecoder = null;
        try{
            //load serialized object
            xmlDecoder = new XMLDecoder(
                    new BufferedInputStream(new FileInputStream(CONFIGURATION_FILE)));
            replications = (Map)xmlDecoder.readObject();
        }catch(Exception exception){
        }finally{
            if(xmlDecoder != null){
                xmlDecoder.close();
            }

            if(replications == null){
                replications = new HashMap<String, Replication>();
            }
        }
    }

    public Map<String, Replication> getReplications() {
        return replications;
    }

    public static synchronized Factory getInstance(){
        if(factory == null){
            factory = new Factory();
        }

        return factory;
    }

    public void save(){
        try{
            //store configuration as serialized object in XML form
            OutputStream outputStream = new BufferedOutputStream(
                    new FileOutputStream(CONFIGURATION_FILE));
            XMLEncoder xmlEncoder = new XMLEncoder(outputStream);
            xmlEncoder.writeObject(replications);
            xmlEncoder.close();
        }catch(Exception exception){
            Logger.getLogger(Factory.class.getName()).log(Level.SEVERE, null, exception);
        }
    }
}
