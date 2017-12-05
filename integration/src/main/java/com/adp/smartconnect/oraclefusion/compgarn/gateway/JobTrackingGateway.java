package com.adp.smartconnect.oraclefusion.compgarn.gateway;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.adp.smartconnect.oraclefusion.compgarn.listeners.APPConfigHolder;
import com.adp.smartconnect.oraclefusion.compgarn.vo.JobActivityDTO;
import com.adp.smartconnect.oraclefusion.compgarn.vo.JobStepDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * Its a utility to create/update job status/steps/activity
 */
@Component
@Lazy
@Slf4j
public class JobTrackingGateway {
	
	private RestTemplate restTemplate = new RestTemplate();

	private String baseURL ;
	private Boolean trackingEnabled = true;
	
    private static final String CREATE_JOB_STEPS = "/jobstep/create/jobid/{jobId}";
    private static final String CREATE_JOB = "/job/create";
    private static final String CREATE_JOB_ACTIVITY = "/activity/create/jobid/{jobid}/jobstepid/{jobstepid}";
    private static final String UPDATE_JOB_STATUS = "/updatestatus/jobid/{jobId}";
	private static final String OBTAIN_JOB_STATUS = "/job/id/{jobId}";


	@PostConstruct
	public void setConfig() {
		baseURL = APPConfigHolder.getKeyStringValue("jobtracking.base.uri");
		trackingEnabled = APPConfigHolder.getKeyBooleanValue("tracking.enabled");
		log.info("Job Tracking URL:{}, Tracking Flag:{}", baseURL, trackingEnabled);
	}
	
	/* Create Job */
	public void createJob(final String clientName,  final String jobId, final String fileName) {
		createJob(clientName, clientName, Status.SUCCESS, jobId, fileName);
	}
	

    /**
     * @param clientName name of the client
     * @param orgId      scOrgId
     * @param status     status of the job
     * @return Created job
     */
	public void createJob(final String clientName, final String orgId, final Status status, final String jobId, final String fileName) {
		if(trackingEnabled==false) return;
		try{
			String url = baseURL + CREATE_JOB;
			
			JSONObject createJobJsonObj = new JSONObject();
			createJobJsonObj.put("org_id", orgId);
			createJobJsonObj.put("job_id", jobId);
			createJobJsonObj.put("status", status);
			createJobJsonObj.put("file_name", fileName);
			createJobJsonObj.put("client_name", clientName);
			String request = createJobJsonObj.toString();
			
			restTemplate.postForObject(url, createHeaders(request, jobId), String.class);
			log.debug("Job created, jobId:{}", jobId);
		}catch(Exception e){
			log.error("Error in creating job :: " + e.getLocalizedMessage());
		}
	}
	
	  /**
     * Process JOB Step
     * @param jobStep
     */
    public void processJobStep(JobStepDTO jobStep){
        if (jobStep.isUpdate()) {
            updateJobStatus(jobStep.getJobId(), jobStep.getJobStepId(), jobStep.getJobName(), jobStep.getErrors(), jobStep.getWarnings());
        }else{
        	createJobSteps(jobStep.getJobName(), jobStep.getSourceData(), jobStep.getErrors(), null,
                    jobStep.getJobStepId(), jobStep.getStatus(), jobStep.getJobId(), jobStep.getWarnings());

        }
    }

	
	
	

    /**
     * @param jobName    Name of the job
     * @param sourceData source data
     * @param sourceName name of the source
     * @param status     status of the job step
     * @param warnings   warning
     * @param errors     Errors
     * @param jobId      parent job id
     * @return
     */
    public void createJobSteps(final String jobName, final String sourceData, final Map<Integer, String> errors, final String sourceName, 
    						   final String jobStepId, final Status status, final String jobId, final Map<Integer, String> warnings) {
    	if(trackingEnabled==false) return; 
    	try {
			 	String url = baseURL + CREATE_JOB_STEPS;
	        	
	            JSONObject createJobStepsJsonObj = new JSONObject();
	            createJobStepsJsonObj.put("job_name", jobName);
	            createJobStepsJsonObj.put("status", status);
	            createJobStepsJsonObj.put("job_step_id", jobStepId);
	            createJobStepsJsonObj.put("source_data", sourceData);
	            createJobStepsJsonObj.put("source_name", sourceName);
	            createJobStepsJsonObj.put("warning", getWarningArrays(warnings));
	            createJobStepsJsonObj.put("error", getWarningArrays(errors));
	            String request = createJobStepsJsonObj.toString();
	            
	            Map<String, String> params = new HashMap<String, String>();
	            params.put("jobId", jobId);

	            restTemplate.postForObject(url, createHeaders(request, jobId), String.class, params);
	            log.debug("Job Steps created successfully. jobId:{}", jobId);
	        } catch (Throwable e) {
	            log.error("Error in creating job steps :: " + e.getLocalizedMessage());
	        } 
    }

    private JSONArray getWarningArrays(Map<Integer, String> warnings) {
        log.trace("Received warning/errors :: " + warnings);
        JSONArray warningsArray = new JSONArray();
        if (warnings == null || warnings.isEmpty())
            return null;

        for (Map.Entry<Integer, String> entry : warnings.entrySet()) {
            log.trace("Warning Code " + entry.getKey() + ", warning message " + entry.getValue());
            JSONObject warning = new JSONObject();
            warning.put("code", entry.getKey());
            warning.put("description", entry.getValue());
            warningsArray.put(warning);
        }
        return warningsArray;
    }

    /**
     * @param activityName
     * @param status
     * @param message
     * @param jobStepId
     * @param jobId
     * @return
     */

    public void createJobActivity(final String activityName, final Status status, final String message, final String jobStepId, final String jobId) {
    	if(trackingEnabled==false) return;
    	
    	try {
        	String url = baseURL + CREATE_JOB_ACTIVITY;
        	
            JSONObject createJobActivity = new JSONObject();
            createJobActivity.put("activity_name", activityName);
            createJobActivity.put("status", status);
            createJobActivity.put("job_step_id", jobStepId);
            createJobActivity.put("message", message);
            String request = createJobActivity.toString();

            Map<String, String> params = new HashMap<String, String>();
            params.put("jobid", jobId);
            params.put("jobstepid", jobStepId);
			
            restTemplate.postForObject(url, createHeaders(request, jobId), String.class, params);
            log.debug("Job Activity Created. jobId:{}", jobId);
        } catch(Throwable e){
			log.error("Error in creating job :: " + e.getLocalizedMessage());
		}
    }

    public void processJobActivity(JobActivityDTO jobActivity ){
        createJobActivity(jobActivity.getActivityName(), jobActivity.getStatus(), jobActivity.getMessage(), jobActivity.getJobStepId(), jobActivity.getJobId());

    }
    
    

	public void updateJobStatus(final String jobId, final String jobStepId, final String acitivityName, 
								final Map<Integer, String> errors, final Map<Integer, String> warnings) {
		if(trackingEnabled==false) return;
		try {
			StringBuilder url = new StringBuilder(baseURL);
			url.append(UPDATE_JOB_STATUS);
			if(!(jobStepId == null || jobStepId.equals(""))) {
				url.append("?jobStepId=");
				url.append(jobStepId);
			}
			if(!(acitivityName == null || acitivityName.equals(""))) {
				url.append("&activityName=");
				url.append(acitivityName);
			}
			
			JSONObject updateJobStatus = new JSONObject();
			updateJobStatus.put("warnings", getWarningArrays(warnings));
			updateJobStatus.put("errors", getWarningArrays(errors));
			String request = updateJobStatus.toString();
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("jobId", jobId);
			
			
			restTemplate.put(url.toString(), createHeaders(request,jobId), params);
			log.debug("Updated status successfully. jobId:{}", jobId);
		} catch(Throwable e){
			log.error("Error in creating job :: " + e.getLocalizedMessage());
		}
	}
    
    
    
	public String obtainJobStatus(String jobId) {
		String url = baseURL + OBTAIN_JOB_STATUS;
		log.info("Obtaining job status for jobId :: " + jobId);
		try {
			log.info("Calling uri to obtain job status :: " + url);
			Map<String, String> params = new HashMap<>();
			params.put("jobId", jobId);
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class, params);
			log.debug("Obtained status successfully");
			return response.getBody();
		} catch (Throwable e) {
			log.error("Error while obtaining status :::"+ e.getLocalizedMessage());
		}
		return null;
	}

    private HttpEntity<String> createHeaders(String request, String transactionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("transactionId", transactionId);
        HttpEntity<String> entity = new HttpEntity<String>(request, headers);
        return entity;
    }

    public enum Status {
        SUCCESS, FAILURE, INPROGRESS, COMPLETED
    }
    
}

