package at.schrer.qrbill.data.store;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public final class StatsData extends AbstractAppData {

    @Builder.Default
    private long uniqueAccesses = 0;
    @Builder.Default
    private long totalAccesses = 0;
    @Builder.Default
    private long companyInfoRequests = 0;

    public StatsData() {
        super(TYPE_STATS);
    }

    public StatsData(long uniqueAccesses, long totalAccesses, long companyInfoRequests) {
        this();
        this.uniqueAccesses = uniqueAccesses;
        this.totalAccesses = totalAccesses;
        this.companyInfoRequests = companyInfoRequests;
    }
}
