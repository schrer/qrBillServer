package at.schrer.qrbill.service;

import at.schrer.qrbill.data.store.StatsData;
import at.schrer.qrbill.storage.ApplicationDataRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class StatsService {
    private final AtomicStatsCounter counter;
    private final ApplicationDataRepository applicationDataRepository;

    public StatsService(ApplicationDataRepository applicationDataRepository) {
        this.applicationDataRepository = applicationDataRepository;
        StatsData stats = applicationDataRepository.getStats();
        this.counter = new AtomicStatsCounter(stats.getUniqueAccesses(), stats.getTotalAccesses());
    }

    public void addFirstTimeAccess() {
        counter.incrementForFirstAccess();
        applicationDataRepository.storeStats(
                new StatsData(counter.getUniqueAccesses(), counter.getTotalAccesses()));
    }

    public void addReturningAccess() {
        counter.incrementNonUnique();
    }

    public long getUniqueAccesses() {
        return counter.getUniqueAccesses();
    }

    public long getTotalAccesses() {
        return counter.getTotalAccesses();
    }

    private static class AtomicStatsCounter {
        private final AtomicLong uniqueAccesses;
        private final AtomicLong totalAccesses;

        public AtomicStatsCounter(long uniqueAccesses, long totalAccesses) {
            this.uniqueAccesses = new AtomicLong(uniqueAccesses);
            this.totalAccesses = new AtomicLong(totalAccesses);
        }

        public void incrementForFirstAccess() {
            uniqueAccesses.incrementAndGet();
            totalAccesses.incrementAndGet();
        }

        public void incrementNonUnique() {
            totalAccesses.incrementAndGet();
        }

        public long getUniqueAccesses() {
            return uniqueAccesses.longValue();
        }

        public long getTotalAccesses() {
            return totalAccesses.longValue();
        }
    }
}
