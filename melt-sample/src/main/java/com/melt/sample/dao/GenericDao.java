package com.melt.sample.dao;

import com.melt.orm.criteria.Criteria;
import com.melt.orm.session.Session;
import com.melt.orm.session.SessionFactory;

import java.util.List;

public abstract class GenericDao<T> {
    private SessionFactory sessionFactory;

    public int insert(T t) {
        return getSession().insert(t);
    }

    public List<T> find(Criteria criteria) {
        return getSession().find(getModelClass(), criteria);
    }

    public T findById(int id) {
        return getSession().findById(getModelClass(), id);
    }

    public void delete(Criteria criteria) {
        //TODO
    }


    public void update(T t, Criteria criteria) {
        //TODO
    }



    protected Session getSession() {
        return sessionFactory.createSession();
    }

    protected abstract Class getModelClass();

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
}
