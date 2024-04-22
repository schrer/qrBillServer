package at.schrer.qrbill.data.store;

import lombok.Getter;

@Getter
public abstract sealed class AbstractAppData permits StatsData {
    public static final String TYPE_STATS = "stats";

    private final String type;

    protected AbstractAppData(String type) {
        this.type = type;
    }

}
