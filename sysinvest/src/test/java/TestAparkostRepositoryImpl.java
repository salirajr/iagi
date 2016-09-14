
import com.rj.sysinvest.dao.AparkostRepository;
import com.rj.sysinvest.model.Aparkost;
import com.rj.sysinvest.model.Tower;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Dummy impl for com.rj.sysinvest.dao.AparkostRepository
 *
 * @author Rais <rais.gowa@gmail.com>
 */
public class TestAparkostRepositoryImpl implements AparkostRepository {

    List<Aparkost> dataList = TestUtil.createAparkostList();

    @Override
    public List<Aparkost> findByTowerId(Long towerId) {
        return dataList.stream()
                .filter(a -> towerId.equals(a.getTower().getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Aparkost> findByTowerIdAndFloor(Long towerId, String floor) {
        return dataList.stream()
                .filter(a -> towerId.equals(a.getTower().getId()))
                .filter(a -> floor.equals(a.getFloor()))
                .collect(Collectors.toList());
    }

    @Override
    public <S extends Aparkost> S save(S s) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <S extends Aparkost> Iterable<S> save(Iterable<S> itrbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Aparkost findOne(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean exists(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<Aparkost> findAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterable<Aparkost> findAll(Iterable<Long> itrbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long count() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Aparkost t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Iterable<? extends Aparkost> itrbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Aparkost findByTowerAndName(Tower tower, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
