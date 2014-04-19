package br.edu.ifpb.daos;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.edu.ifpb.entidades.Featuretype;

public class FeatureDAO {
	
		private EntityManagerFactory emf;
		private EntityManager em;
		
		public FeatureDAO() {
			emf = Persistence.createEntityManagerFactory("tcc-up");
			em = emf.createEntityManager();
			em.clear();
		}
		
		
		public void save(Featuretype feature){
			em.getTransaction().begin();
			em.merge(feature);
			//em.flush();
			em.getTransaction().commit();
			em.close();
			}
}
