package com.rj.sysinvest.dao;

import com.rj.sysinvest.model.Booking;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public interface BookingRepository extends CrudRepository<Booking, Long> {
    

}