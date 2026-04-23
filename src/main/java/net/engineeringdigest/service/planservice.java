package net.engineeringdigest.service;



import net.engineeringdigest.entity.Plan;
import java.util.List;

public interface planservice {
    void savePlan(Plan plan);
    List<Plan> getAll();
}
