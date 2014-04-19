package br.edu.ifpb.daos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import br.edu.ifpb.entidades.Metadatarecord;

public class MetadataDAO {
	
	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("tcc-up");;
	private static EntityManager em = emf.createEntityManager();
	
	public MetadataDAO() {
//		emf = Persistence.createEntityManagerFactory("tcc-up");
//		em = emf.createEntityManager();
//		em.clear();
	}
	
	
	public void save(Metadatarecord metadata){
		em.getTransaction().begin();
		em.merge(metadata);
		//em.flush();
		em.getTransaction().commit();
//		em.close();
		}
	
	public EntityManager getPersistenceContext()  
   {  
      return em;  
   }  
}
