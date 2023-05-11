package org.rundeck.app.data.model.v1.execution;

import java.io.Serializable;

public interface ExecutionData {
    Serializable getId();
    Serializable getJobId();
    String getUuid();
    String getProject();
    String getStatus();
    String getOutputfilepath();
    String getFailedNodeList();
    String getSucceededNodeList();
    String getAbortedby();
    String getExecutionType();
    String getUserRoleList();
    String getServerNodeUUID();
    String getExtraMetadata();
    Integer getRetryAttempt();
    Integer getNodeThreadcount();
    Serializable getRetryOriginalId();
    Serializable getRetryPrevId();
    Serializable getRetryExecutionId();
    Serializable getLogFileStorageRequestId();
    Boolean isCancelled();
    Boolean isTimedOut();
    Boolean isWillRetry();
    Boolean isServerNodeUUIDChanged();
}
