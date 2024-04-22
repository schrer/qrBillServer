package at.schrer.qrbill.api;

import at.schrer.qrbill.data.model.AccessModel;
import at.schrer.qrbill.data.model.StatsModel;
import at.schrer.qrbill.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @PostMapping("/access")
    public void updateStats(AccessModel accessModel) {
        if (accessModel.isFirstTimer()) {
            statsService.addFirstTimeAccess();
        } else {
            statsService.addReturningAccess();
        }
    }

    @GetMapping
    public StatsModel getStats() {
        return StatsModel.builder()
                .totalAccesses(statsService.getTotalAccesses())
                .uniqueAccesses(statsService.getUniqueAccesses())
                .build();
    }
}
