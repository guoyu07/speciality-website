package com.hiczp.web.speciality.entity;

import javax.persistence.*;

/**
 * Created by czp on 17-5-18.
 */
@Entity
@Table(name = "SPRING_SESSION", schema = "speciality_website", catalog = "")
public class SpringSessionEntity {
    private String sessionId;
    private long creationTime;
    private long lastAccessTime;
    private int maxInactiveInterval;
    private String principalName;

    @Id
    @Column(name = "SESSION_ID")
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Basic
    @Column(name = "CREATION_TIME")
    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    @Basic
    @Column(name = "LAST_ACCESS_TIME")
    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    @Basic
    @Column(name = "MAX_INACTIVE_INTERVAL")
    public int getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    @Basic
    @Column(name = "PRINCIPAL_NAME")
    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpringSessionEntity that = (SpringSessionEntity) o;

        if (creationTime != that.creationTime) return false;
        if (lastAccessTime != that.lastAccessTime) return false;
        if (maxInactiveInterval != that.maxInactiveInterval) return false;
        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        if (principalName != null ? !principalName.equals(that.principalName) : that.principalName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sessionId != null ? sessionId.hashCode() : 0;
        result = 31 * result + (int) (creationTime ^ (creationTime >>> 32));
        result = 31 * result + (int) (lastAccessTime ^ (lastAccessTime >>> 32));
        result = 31 * result + maxInactiveInterval;
        result = 31 * result + (principalName != null ? principalName.hashCode() : 0);
        return result;
    }
}
