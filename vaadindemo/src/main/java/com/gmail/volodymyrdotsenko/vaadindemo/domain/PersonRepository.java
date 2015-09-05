
package com.gmail.volodymyrdotsenko.vaadindemo.domain;

import java.util.List;

public interface PersonRepository {
    
    List<Person> findAllBy();
    
    long count();
    void delete(Person p);
    void save(Person p);
}