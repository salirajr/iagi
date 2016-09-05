package com.rj.sysinvest.dao;

import com.rj.sysinvest.model.Site;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author salirajr
 */
public interface SiteRepository extends CrudRepository<Site, Long> {

}
