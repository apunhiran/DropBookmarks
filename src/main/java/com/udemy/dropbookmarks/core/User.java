package com.udemy.dropbookmarks.core;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.CascadeType.ALL;

/**
 * Created by apun.hiran on 3/12/18.
 */
@Entity
@Table(name="users")
@NamedQueries({
        @NamedQuery(name = "com.udemy.dropbookmarks.core.User.findAll",
        query = "select u from User u"),
        @NamedQuery(name = "com.udemy.dropbookmarks.core.User.findByUserNamePassword",
        query = "select u from User u " +
                "where u.username = :username " +
                "and u.password = :password")
})
public class User
        implements Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name="username")
    private String username;

    private String password;

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Bookmarks> bookmarks = new ArrayList<>();

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Bookmarks> getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(List<Bookmarks> bookmarks) {
        this.bookmarks = bookmarks;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 59 * hash + Objects.hashCode(this.username);
        hash = 59 * hash + Objects.hashCode(this.password);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return true;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }
}
