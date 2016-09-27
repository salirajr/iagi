package com.rj.sysinvest.dao;

import com.rj.sysinvest.model.Staff;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author salirajr
 */
public interface StaffRepository extends CrudRepository<Staff, Long> {

    @Query("from Staff where rank.id in (2)")
    public List<Staff> getCovenant();

    public Staff findByUserLoginUserName(String value);

}
