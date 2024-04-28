package at.schrer.qrbill.service;

import at.schrer.qrbill.client.OpenDataHostClient;
import at.schrer.qrbill.data.model.CompanyModel;
import at.schrer.qrbill.data.store.CompanyData;
import at.schrer.qrbill.storage.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CompanyService {
    private static final Pattern AT_UID_PATTERN = Pattern.compile("ATU\\d{8}");

    private final CompanyRepository companyRepository;
    private final OpenDataHostClient openDataHostClient;

    public CompanyService(CompanyRepository companyRepository,
                          @Autowired(required = false) OpenDataHostClient openDataHostClient) {
        this.companyRepository = companyRepository;
        this.openDataHostClient = openDataHostClient;
    }

    public Optional<CompanyModel> matchCompany(String certId) {
        return matchCompany(certId, true);
    }

    public Optional<CompanyModel> matchCompany(String certId, boolean useFetch) {
        Optional<String> containedUid = getContainedUid(certId);
        Optional<CompanyData> company;
        company = containedUid.map(this::getCompanyByUid)
                .orElseGet(() -> getCompanyByCertId(certId));
        if (useFetch && company.isEmpty() && containedUid.isPresent()) {
            String uid = containedUid.get();
            company = fetchCompanyByUid(uid);
            company.ifPresent(companyRepository::storeCompany);
        }
        return company.map(c -> dataToCompanyModel(c, certId));
    }

    public List<CompanyModel> matchCompanies(List<String> certIds) {
        return certIds.stream()
                .map(this::matchCompany)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    public long countCompanies() {
        return companyRepository.count();
    }

    private Optional<String> getContainedUid(String certId) {
        Matcher matcher = AT_UID_PATTERN.matcher(certId);
        if (!matcher.find()) {
            return Optional.empty();
        }

        return Optional.of(matcher.group());
    }

    private Optional<CompanyData> getCompanyByUid(String uid) {
        return Optional.ofNullable(companyRepository.getByUid(uid));
    }

    private Optional<CompanyData> getCompanyByCertId(String certId) {
        return Optional.ofNullable(companyRepository.getByCertId(certId));
    }

    private Optional<CompanyData> fetchCompanyByUid(String uid) {
        if (openDataHostClient == null) {
            return Optional.empty();
        }
        return openDataHostClient.fetchCompanyByUid(uid)
                .map(c -> responseToCompanyData(c, uid));
    }

    private CompanyModel dataToCompanyModel(CompanyData companyData, String certId) {
        return CompanyModel.builder()
                .shopName(companyData.getPublicName())
                .companyName(companyData.getName())
                .certId(certId)
                .uid(companyData.getUids().getFirst())
                .build();
    }

    private CompanyData responseToCompanyData(OpenDataHostClient.ValidateResponse companyData, String uid) {
        return CompanyData.builder()
                .name(companyData.getName())
                .publicName(companyData.getName())
                .uids(List.of(uid))
                .build();
    }
}
