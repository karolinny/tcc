package br.edu.ifpb.daos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.edu.ifpb.entidades.Service;

public class ServiceDAO {
	
		private EntityManagerFactory emf;
		private EntityManager em;
		
		public ServiceDAO() {
			emf = Persistence.createEntityManagerFactory("tcc-up");
			em = emf.createEntityManager();
			em.clear();
		}
		
		
		public void save(Service service){
			em.getTransaction().begin();
			em.merge(service);
			//em.flush();
			em.getTransaction().commit();
			em.close();
			}
}
