package at.schrer.qrbill.api;

import at.schrer.qrbill.data.model.CompanyModel;
import at.schrer.qrbill.service.CompanyService;
import at.schrer.qrbill.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;
    private final StatsService statsService;

    public CompanyController(CompanyService companyService, StatsService statsService) {
        this.companyService = companyService;
        this.statsService = statsService;
    }

    @GetMapping("/{certId}")
    public CompanyModel getOneByCertId(@PathVariable String certId) {
        statsService.addCompanyInfoRequest();
        return companyService.matchCompany(certId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    @GetMapping
    public List<CompanyModel> getMoreByCertIdsNoSlash(@RequestParam("certId") List<String> certIds) {
        return getMoreByCertIds(certIds);
    }

    @GetMapping("/")
    public List<CompanyModel> getMoreByCertIds(@RequestParam("certId") List<String> certIds) {
        if (certIds == null || certIds.isEmpty()) {
            return List.of();
        }
        statsService.addCompanyInfoRequests(certIds.size());
        return companyService.matchCompanies(certIds);
    }
}
