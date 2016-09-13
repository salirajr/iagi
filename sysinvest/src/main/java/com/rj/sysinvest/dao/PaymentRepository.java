package com.rj.sysinvest.dao;

import com.rj.sysinvest.model.Payment;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author salirajr
 */
public interface PaymentRepository extends CrudRepository<Payment, Long> {

}
