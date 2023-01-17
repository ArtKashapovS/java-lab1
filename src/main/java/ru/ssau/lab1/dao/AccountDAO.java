package ru.ssau.lab1.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ru.ssau.lab1.model.Account;

import java.util.List;

@Stateless
public class AccountDAO {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public Account findByID(Integer id) {
        return em.find(Account.class, id);
    }

    public List<Account> getAll() {
        TypedQuery<Account> accountQuery = em.createNamedQuery("Account.getAll", Account.class);
        return accountQuery.getResultList();
    }

    public void create(Account account) {
        em.persist(account);
        em.flush();
    }

    public void update(Account account) {
        em.merge(account);
    }

    public void removeById(Integer id) {
        em.remove(findByID(id));
    }
}
