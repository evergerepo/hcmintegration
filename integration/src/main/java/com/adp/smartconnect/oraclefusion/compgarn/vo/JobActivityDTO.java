package com.adp.smartconnect.oraclefusion.compgarn.vo;


import com.adp.smartconnect.oraclefusion.compgarn.gateway.JobTrackingGateway.Status;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString()
public class JobActivityDTO {
    private String activityName;
    private String message;
    private String jobStepId;
    private Status status;
    private String jobId;

    public JobActivityDTO(String jobId, String jobStepId, String activityName, String message, Status status) {
    	this.jobId = jobId;
        this.jobStepId = jobStepId;
        this.activityName = activityName;
        this.message = message;
        this.status = status;
    }



}

