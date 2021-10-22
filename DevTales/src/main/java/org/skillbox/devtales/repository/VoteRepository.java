package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.Vote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Integer> {

}
