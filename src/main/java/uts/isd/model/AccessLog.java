// File: uts/isd/model/AccessLog.java
package uts.isd.model;

import java.time.LocalDateTime;

public class AccessLog {
    private final int    logId;
    private final String email;
    private final String action;
    private final LocalDateTime timestamp;

    public AccessLog(int logId, String email, String action, LocalDateTime ts) {
        this.logId     = logId;
        this.email     = email;
        this.action    = action;
        this.timestamp = ts;
    }
    // ─── Getters ────────────────────────────────────────────────
    public int    getLogId()     { return logId; }
    public String getEmail()     { return email; }
    public String getAction()    { return action; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
