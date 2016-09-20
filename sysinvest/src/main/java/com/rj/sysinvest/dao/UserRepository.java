package com.rj.sysinvest.dao;

import com.rj.sysinvest.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author salirajr
 */
public interface UserRepository extends CrudRepository<User, String> {

}
