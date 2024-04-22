package at.schrer.qrbill.config;

import at.schrer.qrbill.data.store.CompanyData;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.collection.NitriteCollection;
import org.dizitart.no2.common.mapper.JacksonMapperModule;
import org.dizitart.no2.repository.ObjectRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NitriteConfig {
    @Bean
    public Nitrite nitrite() {
        return Nitrite.builder()
                .loadModule(new JacksonMapperModule())
                .schemaVersion(1)
                .openOrCreate();
    }

    @Bean
    public ObjectRepository<CompanyData> companyNitriteRepos(Nitrite nitrite) {
        ObjectRepository<CompanyData> companyRepository = nitrite.getRepository(CompanyData.class, "companies");
        companyRepository.createIndex("certIds");
        return companyRepository;
    }

    @Bean
    public NitriteCollection applicationDataRepo(Nitrite nitrite) {
        return nitrite.getCollection("applicationData");
    }
}
