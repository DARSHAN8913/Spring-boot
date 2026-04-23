package net.engineeringdigest.service.impl;




import net.engineeringdigest.entity.Plan;
import net.engineeringdigest.repository.jmsrepo;
import net.engineeringdigest.service.planservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class planserviceimpl implements planservice {
    
    private final jmsrepo planRepository;

    // Constructor Injection: The standard way to inject dependencies
    public planserviceimpl(jmsrepo planRepository) {
        this.planRepository = planRepository;
    }


    public void savePlan(Plan plan) {
        planRepository.save(plan);
    }


    public List<Plan> getAll() {
        return planRepository.findAll();
    }
}