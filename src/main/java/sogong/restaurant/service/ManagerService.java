package sogong.restaurant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.restaurant.domain.Manager;
import sogong.restaurant.repository.ManagerRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ManagerService {

    @Autowired
    private ManagerRepository managerRepository;

    public Optional<Manager> getOneManager(Long id) {
        return managerRepository.findById(id);
    }
}
