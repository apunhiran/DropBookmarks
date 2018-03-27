package com.udemy.dropbookmarks.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Objects;

/**
 * Created by apun.hiran on 3/20/18.
 */
@Entity
@Table(name="bookmarks")
@NamedQueries({
        @NamedQuery(name = "com.udemy.dropbookmarks.core.Bookmarks.findForUser",
        query = "select b from Bookmarks b where b.user.id = :id"),
        @NamedQuery(name = "com.udemy.dropbookmarks.core.Bookmarks.remove",
        query= "delete from Bookmarks b where b.id = :id"),
        @NamedQuery(name = "com.udemy.dropbookmarks.core.Bookmarks.removeAllForUser",
        query = "delete from Bookmarks b where b.user.id = :id")
})
public class Bookmarks implements Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="name")
    private String name;
    @Column(name="url")
    private String url;
    @Column(name = "description")
    private String description;

    @JsonIgnore
    @ManyToOne
    private User user;

    public Bookmarks() {
    }

    public Bookmarks(String name, String url, String description, User userId) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.user = userId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 97 * hash + Objects.hashCode(this.name);
        hash = 97 * hash + Objects.hashCode(this.url);
        hash = 97 * hash + Objects.hashCode(this.description);
        hash = 97 * hash + Objects.hashCode(this.user);
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
        final Bookmarks other = (Bookmarks) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.url, other.url)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return true;
    }
}
