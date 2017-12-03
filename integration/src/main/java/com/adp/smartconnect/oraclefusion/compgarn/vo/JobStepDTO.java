package com.adp.smartconnect.oraclefusion.compgarn.vo;

import java.util.Map;

import com.adp.smartconnect.oraclefusion.compgarn.gateway.JobTrackingGateway.Status;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString()
public class JobStepDTO {
    private String jobId;
    private String jobStepId;
    private Status status;
    private String jobName;
    private String sourceData;
    private Map<Integer, String> errors;
    private String sourceName;
    private Map<Integer, String> warnings;
    private boolean isUpdate;

    public JobStepDTO(String jobId, String jobStepId, Status status, String jobName, String sourceData, String sourceName,
                      Map<Integer, String> errors, Map<Integer, String> warnings, boolean isUpdate) {
        this.jobId = jobId;
        this.jobStepId = jobStepId;
        this.status = status;
        this.jobName = jobName;
        this.sourceData = sourceData;
        this.sourceName = sourceName;
        this.errors = errors;
        this.warnings = warnings;
        this.isUpdate = isUpdate;
    }


}