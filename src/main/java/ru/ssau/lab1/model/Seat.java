package ru.ssau.lab1.model;


import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "cinema_hall")
@NamedQuery(name = "Seat.getAll", query = "SELECT c from cinema_hall c")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "row", nullable = false)
    private int row;

    @Column(name = "col", nullable = false)
    private int col;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = true)
    private Account account;

    @Transient
    private int customID;

    @Column(name = "price", nullable = false)
    private float price;

    public Seat() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void generateCustomId() {
        this.customID = Integer.parseInt("" + row + col);
    }

    public int getCustomID() {
        return customID;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seat seat = (Seat) o;
        return id == seat.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
