package at.schrer.qrbill.data.store;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class StatsData extends AbstractAppData {

    private long uniqueAccesses = 0;
    private long totalAccesses = 0;

    public StatsData() {
        super(TYPE_STATS);
    }

    public StatsData(long uniqueAccesses, long totalAccesses) {
        this();
        this.uniqueAccesses = uniqueAccesses;
        this.totalAccesses = totalAccesses;
    }
}
