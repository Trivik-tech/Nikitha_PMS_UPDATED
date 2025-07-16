package com.triviktech.payloads.response.employee;

public class PmsStatuscountDto {
    private long completedCount;
    private long pendingCount;

    public PmsStatuscountDto() {}

    public PmsStatuscountDto(long completedCount, long pendingCount) {
        this.completedCount = completedCount;
        this.pendingCount = pendingCount;
    }

    public long getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(long completedCount) {
        this.completedCount = completedCount;
    }

    public long getPendingCount() {
        return pendingCount;
    }

    public void setPendingCount(long pendingCount) {
        this.pendingCount = pendingCount;
    }
}
