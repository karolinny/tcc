package br.edu.ifpb.test;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.sis.storage.DataStoreException;
import org.geotoolkit.data.FeatureStore;
import org.geotoolkit.data.FeatureStoreFactory;
import org.geotoolkit.data.query.QueryCapabilities;
import org.geotoolkit.data.wfs.GetCapabilitiesRequest;
import org.geotoolkit.data.wfs.GetFeatureRequest;
import org.geotoolkit.data.wfs.WFSFeatureStore;
import org.geotoolkit.data.wfs.WebFeatureServer;
import org.opengis.util.InternationalString;

public class WFSClientDemo {

    public static void main(String[] args) throws URISyntaxException, IOException, DataStoreException {
        
        final WebFeatureServer wfsServer = new WebFeatureServer(new URL("http://www.geoservicos.ibge.gov.br/geoserver/wfs?"), "1.1.0");
        final GetCapabilitiesRequest getCapa = wfsServer.createGetCapabilities();
        final GetFeatureRequest getFeature = wfsServer.createGetFeature();

        //simplify WFS like a datastore
        final FeatureStore store = new WFSFeatureStore(wfsServer);
        String[] typeNames = store.getTypeNames();
        
        InternationalString test = store.getFeatureType(typeNames[1]).getDescription();        
              
        System.out.println(test);
        
        FeatureStoreFactory novaFactory = store.getFactory();
        Map<String, Serializable> testmap = new HashMap<String, Serializable>();
        
        testmap.put("url", wfsServer.getURL());
        FeatureStore newDataStore = novaFactory.create(testmap);
       // QueryCapabilities capa = newDtaStore.get
        
        
        System.out.println(store.getFactory().getIdentification().getAbstract());
        
        
        

    }

}