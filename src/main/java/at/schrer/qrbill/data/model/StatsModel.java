package at.schrer.qrbill.data.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatsModel {

    AccessStats siteAccesses;
    long cachedCompanies;

    @Getter
    @Builder
    public static class AccessStats {
        long uniqueAccesses;
        long totalAccesses;
        long companyInfoRequests;
    }
}
