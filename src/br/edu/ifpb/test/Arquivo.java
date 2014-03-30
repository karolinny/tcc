package br.edu.ifpb.test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import com.sun.xml.internal.ws.api.addressing.WSEndpointReference.Metadata;


public class Arquivo {
	
	private BufferedWriter bw;
	private BufferedReader br;
	private File arquivo,arq;
	
	public Arquivo(){
		this.arquivo = new File("/home/karol/ArquivosTCC");
	    this.arq = new File(arquivo, "MetadadosINDE.txt");
	}
	
	public void abrir() throws IOException{
		this.bw = new BufferedWriter(new PrintWriter(new FileWriter(this.arquivo, true), true));
		this.br = new BufferedReader(new FileReader(this.arquivo));
	}
	
	public void fechar() throws IOException{
		this.bw.close();
		this.br.close();
	}
	

	public void escrever(org.opengis.metadata.Metadata meta) {
	    

	    try {

	        FileWriter fileWriter = new FileWriter(arq, true);

	        PrintWriter printWriter = new PrintWriter(fileWriter);

	        printWriter.println(meta);
	       
	        printWriter.flush();

	        printWriter.close();

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	}
}
