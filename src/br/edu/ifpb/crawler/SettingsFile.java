package br.edu.ifpb.crawler;

import java.io.File;
import java.util.Properties;

public class SettingsFile {
	 private Properties properties;
	 
	 public SettingsFile(File file) {
	        properties = new Properties();
	        try {
	            properties.loadFromXML(file.toURI().toURL().openStream());
	            System.out.println("caminho do xml: " + file.toURI().toURL().openStream());
	        }
	        catch (Exception e) {
	        }
	    }
	 
	  public int settingsXML(String setting) {
	        //Retrieve the value of the associated key
		  
		  System.out.println(properties.getProperty(setting));
	        int instruction = Integer.parseInt(properties.getProperty(setting));
	       
	        return instruction;
	    }
}
