package com.rj.sysinvest.akad;

import com.rj.sysinvest.model.Payment;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import lombok.Data;
import org.springframework.stereotype.Component;

/**
 *
 * @author Rais <rais.gowa@gmail.com>
 */
@Component
@Data
public class LampiranPembayaranDataMapper implements Function<Payment, List<String>> {

    private SimpleDateFormat shortDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private NumberFormat jumlahFormatter = NumberFormat.getInstance();

    @Override
    public List<String> apply(Payment payment) {
        return Arrays.asList(new String[]{
            String.valueOf(payment.getIndex() + 1),
            payment.getType(),
            shortDateFormat.format(payment.getPaydate()),
            jumlahFormatter.format(payment.getNominal())
        });
    }
}
