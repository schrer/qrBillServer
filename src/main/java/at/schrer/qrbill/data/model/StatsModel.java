package at.schrer.qrbill.data.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class StatsModel {
    long uniqueAccesses;
    long totalAccesses;
}
