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
        this.counter = new AtomicStatsCounter(stats.getUniqueAccesses(), stats.getTotalAccesses(), stats.getCompanyInfoRequests());
    }

    public void addFirstTimeAccess() {
        counter.incrementForFirstAccess();
        storeStats();
    }

    public void addReturningAccess() {
        counter.incrementNonUnique();
        storeStats();
    }

    public void addCompanyInfoRequest() {
        counter.incrementCompanyInfoRequests();
        storeStats();
    }

    public void addCompanyInfoRequests(long amount) {
        counter.incrementCompanyInfoRequestsBy(amount);
        storeStats();
    }

    public long getUniqueAccesses() {
        return counter.getUniqueAccesses();
    }

    public long getTotalAccesses() {
        return counter.getTotalAccesses();
    }

    private void storeStats() {
        StatsData stats = new StatsData(
                counter.getUniqueAccesses(),
                counter.getTotalAccesses(),
                counter.getTotalCompanyInfoRequests()
        );
        applicationDataRepository.storeStats(stats);
    }

    private static class AtomicStatsCounter {
        private final AtomicLong uniqueAccesses;
        private final AtomicLong totalAccesses;
        private final AtomicLong companyInfoRequests;

        public AtomicStatsCounter(long uniqueAccesses, long totalAccesses, long companyInfoRequests) {
            this.uniqueAccesses = new AtomicLong(uniqueAccesses);
            this.totalAccesses = new AtomicLong(totalAccesses);
            this.companyInfoRequests = new AtomicLong(companyInfoRequests);
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

        public long incrementCompanyInfoRequests() {
            return companyInfoRequests.incrementAndGet();
        }

        public long incrementCompanyInfoRequestsBy(long delta) {
            return companyInfoRequests.addAndGet(delta);
        }

        public long getTotalAccesses() {
            return totalAccesses.longValue();
        }

        public long getTotalCompanyInfoRequests() {
            return companyInfoRequests.longValue();
        }

    }
}
