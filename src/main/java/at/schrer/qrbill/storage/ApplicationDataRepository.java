package at.schrer.qrbill.storage;

import at.schrer.qrbill.data.store.AbstractAppData;
import at.schrer.qrbill.data.store.StatsData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dizitart.no2.collection.Document;
import org.dizitart.no2.collection.NitriteCollection;
import static org.dizitart.no2.filters.FluentFilter.where;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationDataRepository {
    private final ObjectMapper objectMapper;
    private final NitriteCollection applicationDataRepo;

    public ApplicationDataRepository(NitriteCollection applicationDataRepo) {
        this.applicationDataRepo = applicationDataRepo;
        objectMapper = new ObjectMapper();
    }

    public StatsData getStats() {

        Document document = applicationDataRepo.find(where("type").eq(AbstractAppData.TYPE_STATS)).firstOrNull();
        if (document == null) {
            return new StatsData();
        }
        return mapDocument(document, StatsData.class);
    }

    public void storeStats(StatsData stats) {
        Document document = mapToDocument(stats);
        applicationDataRepo.update(where("type").eq(AbstractAppData.TYPE_STATS), document);
    }

    private Document mapToDocument(Object object) {
        return Document.createDocument(objectMapper.convertValue(object, new TypeReference<>() {}));
    }

    private <T> T mapDocument(Document document, Class<T> clazz) {
        return objectMapper.convertValue(document, clazz);
    }
}
