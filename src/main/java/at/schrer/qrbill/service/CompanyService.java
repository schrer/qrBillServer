package at.schrer.qrbill.service;

import at.schrer.qrbill.data.model.CompanyModel;
import at.schrer.qrbill.data.store.CompanyData;
import at.schrer.qrbill.storage.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CompanyService {
    private static final Pattern AT_UID_PATTERN = Pattern.compile("ATU\\d{8}");

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Optional<CompanyModel> matchCompany(String certId) {
        Optional<String> containedUid = getContainedUid(certId);
        Optional<CompanyData> company;
        company = containedUid.map(this::getCompanyByUid).orElseGet(() -> getCompanyByCertId(certId));
        return company.map(c -> CompanyModel.builder()
                .shopName(c.getPublicName())
                .companyName(c.getName())
                .certId(certId)
                .uid(containedUid.orElse(
                        c.getUids().isEmpty() ? "" : c.getUids().getFirst()))
                .build());
    }

    public List<CompanyModel> matchCompanies(List<String> certIds) {
        return certIds.stream()
                .map(this::matchCompany)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
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
}
