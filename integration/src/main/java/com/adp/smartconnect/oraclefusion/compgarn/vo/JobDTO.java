package com.adp.smartconnect.oraclefusion.compgarn.vo;

import java.util.Date;

import com.adp.smartconnect.oraclefusion.compgarn.gateway.JobTrackingGateway.Status;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString()
public class JobDTO {

    private String jobId;
    private String orgId;
    private String clientName;
    private String fileName;
    private Date createdDate;
    private Date updatedDate;
    private Status status;

    public JobDTO(String jobId, String orgId, String clientName, String fileName, Date createdDate, Date updatedDate, Status status) {
        this.jobId = jobId;
        this.orgId = orgId;
        this.clientName = clientName;
        this.fileName = fileName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.status = status;
    }


}
