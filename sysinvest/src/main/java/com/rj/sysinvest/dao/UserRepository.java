package com.rj.sysinvest.dao;

import com.rj.sysinvest.model.UserStaff;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author salirajr
 */
public interface UserRepository extends CrudRepository<UserStaff, String> {

}
