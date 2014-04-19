package br.edu.ifpb.daos;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.edu.ifpb.entidades.Metadatarecordurl;

public class MetadataUrlsDAO {
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public MetadataUrlsDAO() {
		emf = Persistence.createEntityManagerFactory("tcc-up");
		em = emf.createEntityManager();
		em.clear();
	}
	
	
	public void save(Metadatarecordurl metadata){
		em.getTransaction().begin();
		em.merge(metadata);
		//em.flush();
		em.getTransaction().commit();
		em.close();
		}
	
	public EntityManager getPersistenceContext()  
   {  
      return em;  
   }  
}