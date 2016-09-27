package com.rj.sysinvest.model.util;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author salirajr
 */

public interface LookupRepository extends CrudRepository<Lookup, Long>  {

    public List<Lookup> findByGroupName(@Param("groupName") String groupName);
    
    @Query("select distinct(l.groupName) from Lookup l")
    public List<String> listGroupName();

}
