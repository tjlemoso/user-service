package org.acme.com.repository;

import javax.enterprise.context.ApplicationScoped;

import org.acme.com.model.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User>{
    public User findByName(String name){
        return find("name", name).firstResult();
    }
}
