package com.twenty20.dao.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.twenty20.dao.UserDao;
import com.twenty20.domain.User;
@Repository("userDAO")
public class UserDaoImpl extends JpaDAOImpl<Long, User> implements UserDao{
	@Autowired
    EntityManagerFactory entityManagerFactory;
	
	@PersistenceContext(unitName="V2CRM_PersistenceUnit")
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager em) {
		this.entityManager = em;
		super.setEntityManager(entityManager);
		}
    
    @PostConstruct
    public void init() {
        super.setEntityManagerFactory(entityManagerFactory);
        super.setEntityManager(entityManager);
    }

    

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

	@Override
	public List<User> fetchValidatedUsersForCompanies(String companyName) {
		
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<User> query = builder.createQuery(User.class);
		Root<User> root = query.from(User.class);
		
		Predicate where = builder.conjunction();
		where  = builder.and(builder.equal(root.get("company").get("companyName"),companyName));
		query = query.where(where);
		TypedQuery<User> qry = getEntityManager().createQuery(query);
		List<User> users = qry.getResultList();
			
		return users;
	}
	
	
	
	
}