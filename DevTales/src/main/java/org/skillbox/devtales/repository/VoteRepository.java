package org.skillbox.devtales.repository;

import org.skillbox.devtales.model.PostVote;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends CrudRepository<PostVote, Integer> {

}
