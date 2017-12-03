package com.adp.smartconnect.oraclefusion.compgarn.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.adp.smartconnect.oraclefusion.compgarn.gateway.JobTrackingGateway;
import com.adp.smartconnect.oraclefusion.compgarn.gateway.JobTrackingGateway.Status;
import com.adp.smartconnect.oraclefusion.compgarn.vo.JobActivityDTO;
import com.adp.smartconnect.oraclefusion.compgarn.vo.JobStepDTO;

@Component
public class JobTrackingService {
	
	String sourceName = "Oracle HCM Cloud- CompGarn";
	
	@Autowired  JobTrackingGateway jobTrackingGateway;
	
	/* Create Job */
	public void trackStartJob(String clientName, String clientId,  String fileName, String jobId, String jobStepId, String flowType){
		jobTrackingGateway.createJob(clientName,  clientId, Status.SUCCESS,  jobId,  fileName);
		trackStartJobActivity(jobId,  jobStepId,  fileName, flowType);

    }
	

    /* Track Job Activity */
	public void trackStartJobActivity(String jobId, String jobStepId, String fileName, String flowType){
		 JobStepDTO jobStep = new JobStepDTO(jobId, jobStepId, JobTrackingGateway.Status.SUCCESS, flowType, "", sourceName, null, null, false);
		 jobTrackingGateway.processJobStep(jobStep);
		 
		 trackActivity(jobId, jobStepId, "StartedTime", (new Date()).toString(), JobTrackingGateway.Status.SUCCESS);
    }
    

	/* Create Activity  */
	public void trackActivity(String jobId, String jobStepId, String activityName, String data){
		trackActivity( jobId,  jobStepId,  activityName,  data, JobTrackingGateway.Status.SUCCESS);
    }

	
	/* Create Activity  */
	public void trackActivity(String jobId, String jobStepId, String activityName, String data, JobTrackingGateway.Status status){
        JobActivityDTO jobActivity = new JobActivityDTO(jobId, jobStepId, activityName, data, status);
        jobTrackingGateway.processJobActivity(jobActivity);
    }


    /* Track application Exception */
	public void trackException(String jobId, String jobStepId,  String stepName, String errorMessage){
        Map<Integer, String> errors = new HashMap<>();
        errors.put(1, errorMessage);
        JobStepDTO jobStep = new JobStepDTO(jobId, jobStepId, JobTrackingGateway.Status.FAILURE, stepName, null,sourceName, errors, null, true);
        jobTrackingGateway.processJobStep(jobStep);

        JobActivityDTO jobActivity = new JobActivityDTO(jobId, jobStepId, stepName, errorMessage, JobTrackingGateway.Status.FAILURE);
        jobTrackingGateway.processJobActivity(jobActivity);

    }
	
	
}
