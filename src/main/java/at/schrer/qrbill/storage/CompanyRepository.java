package at.schrer.qrbill.storage;

import at.schrer.qrbill.data.store.CompanyData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dizitart.no2.repository.ObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

import static org.dizitart.no2.filters.FluentFilter.$;
import static org.dizitart.no2.filters.FluentFilter.where;

@Repository
public class CompanyRepository {
    private static final String FIELD_CERT_IDS = "certIds";
    private static final String FIELD_UIDS = "uids";

    private static final Logger logger = LoggerFactory.getLogger(CompanyRepository.class);

    private final ObjectRepository<CompanyData> companyNitriteRepo;

    public CompanyRepository(ObjectRepository<CompanyData> companyNitriteRepo,
                             @Value("${company.mappings.file}") Resource companyFile) throws IOException {
        this.companyNitriteRepo = companyNitriteRepo;
        initLoad(companyFile);
    }

    public void storeCompany(CompanyData company) {
        companyNitriteRepo.insert(company);
    }

    public CompanyData getByCertId(String certId) {
        return companyNitriteRepo.find(where(FIELD_CERT_IDS).elemMatch($.eq(certId)))
                .firstOrNull();
    }

    public CompanyData getByUid(String uid) {
        return companyNitriteRepo.find(where(FIELD_UIDS).elemMatch($.eq(uid)))
                .firstOrNull();
    }

    public long count() {
        return companyNitriteRepo.size();
    }

    private void initLoad (Resource companyFile) throws IOException {
        List<CompanyData> companies = loadCompanies(companyFile);
        companies.forEach(this::storeCompany);
        logger.info("Initialized local storage with {} companies from {}", companies.size(), companyFile.getURI());
    }

    private List<CompanyData> loadCompanies(Resource companyFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(companyFile.getInputStream(),
                new TypeReference<>() {
                });
    }
}
