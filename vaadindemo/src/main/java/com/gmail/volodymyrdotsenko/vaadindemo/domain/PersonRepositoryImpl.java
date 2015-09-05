package com.gmail.volodymyrdotsenko.vaadindemo.domain;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
public class PersonRepositoryImpl implements PersonRepository {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Person> findAllBy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(Person p) {
		// TODO Auto-generated method stub

	}

	@Override
	@Transactional
	public void save(Person p) {
		em.persist(p);
		em.flush();
	}
}