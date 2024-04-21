package at.schrer.qrbill.api;

import at.schrer.qrbill.data.model.CompanyModel;
import at.schrer.qrbill.service.CompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }



    @GetMapping("/{certId}")
    public CompanyModel getOneByCertId(@PathVariable String certId) {
        return companyService.matchCompany(certId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
    }

    @GetMapping
    public List<CompanyModel> getMoreByCertIdsNoSlash(@RequestParam("certId") List<String> certIds) {
        return getMoreByCertIds(certIds);
    }

    @GetMapping("/")
    public List<CompanyModel> getMoreByCertIds(@RequestParam("certId") List<String> certIds) {
        return companyService.matchCompanies(certIds);
    }
}
