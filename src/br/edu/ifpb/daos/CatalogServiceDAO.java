package br.edu.ifpb.daos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.edu.ifpb.entidades.Catalogservice;

public class CatalogServiceDAO {
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public CatalogServiceDAO(){
		emf = Persistence.createEntityManagerFactory("tcc-up");
		em = emf.createEntityManager();
		em.clear();
	}
	
	public void save(Catalogservice cs){
		em.getTransaction().begin();
		em.persist(cs);
		//em.flush();
		em.getTransaction().commit();
		em.close();
	}
}
