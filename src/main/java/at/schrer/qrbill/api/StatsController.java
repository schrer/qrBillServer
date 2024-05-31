package at.schrer.qrbill.api;

import at.schrer.qrbill.data.model.AccessModel;
import at.schrer.qrbill.data.model.StatsModel;
import at.schrer.qrbill.service.CompanyService;
import at.schrer.qrbill.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatsController {

    private final StatsService statsService;
    private final CompanyService companyService;

    public StatsController(StatsService statsService, CompanyService companyService) {
        this.statsService = statsService;
        this.companyService = companyService;
    }

    @GetMapping("/health")
    public void health() {
        // Just a dummy endpoint to check if the service is running
    }

    @PostMapping("/access")
    public void updateStats(AccessModel accessModel) {
        if (accessModel.isFirstTimer()) {
            statsService.addFirstTimeAccess();
        } else {
            statsService.addReturningAccess();
        }
    }

    @GetMapping("/stats")
    public StatsModel getStats() {
        StatsModel.AccessStats accessStats = StatsModel.AccessStats.builder()
                .uniqueAccesses(statsService.getUniqueAccesses())
                .totalAccesses(statsService.getTotalAccesses())
                .companyInfoRequests(statsService.getCompanyInfoRequests())
                .build();

        return StatsModel.builder()
                .siteAccesses(accessStats)
                .cachedCompanies(companyService.countCompanies())
                .build();
    }
}
