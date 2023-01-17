package ru.ssau.lab1.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ru.ssau.lab1.model.Seat;

import java.util.List;

@Stateless
public class SeatDAO {
    @PersistenceContext(unitName = "default")
    private EntityManager em;

    public Seat findById(Integer id) {
        return em.find(Seat.class, id);
    }

    public List<Seat> getAllSeats() {
        TypedQuery<Seat> seatQuery = em.createNamedQuery("Seat.getAll", Seat.class);
        return seatQuery.getResultList();
    }

    public void create(Seat seat) {
        em.persist(seat);
        em.flush();
    }

    public void update(Seat seat) {
        em.merge(seat);
    }
}
