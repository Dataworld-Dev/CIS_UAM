package com.dw.ngms.cis.exception;

import org.springframework.stereotype.Component;

@Component
public class ResponseBuilderAgent implements ExceptionConstants {

	public RestResponse createSuccessResponse(String url) throws Exception
	{
		RestResponse restResponse = new RestResponse();
		UtilitiesAgent utilties = new UtilitiesAgent();
		restResponse.setMessage(SUCCESS);
		restResponse.setTimestamp(utilties.getTimeStampInMillis());
		restResponse.setError(NO_ERROR);
		restResponse.setStatus(RESPONSE_SUCCESS_CODE);
		restResponse.setException(NO_EXCEPTION);
		restResponse.setPath(url);
		return restResponse;
	}
	
	public RestResponse createFailureResponse(Exception e, String url, int httpErrorCode)
	{
		RestResponse restResponse = new RestResponse();
		UtilitiesAgent utilties = new UtilitiesAgent();
		restResponse.setMessage("The execution of the service failed in some way");
		restResponse.setTimestamp(utilties.getTimeStampInMillis());
		restResponse.setError(e.getMessage());
		restResponse.setStatus(httpErrorCode);
		restResponse.setPath(url);
		restResponse.setException(e.getClass().getSimpleName());
		return restResponse;
	}
	
	public RestResponse createTrueResponse(String url) throws Exception
	{
		RestResponse restResponse = new RestResponse();
		UtilitiesAgent utilties = new UtilitiesAgent();
		restResponse.setMessage(TRUE);
		restResponse.setTimestamp(utilties.getTimeStampInMillis());
		restResponse.setError(NO_ERROR);
		restResponse.setStatus(RESPONSE_SUCCESS_CODE);
		restResponse.setException(NO_EXCEPTION);
		restResponse.setPath(url);
		return restResponse;
	}
	
	public RestResponse createFalseResponse(String url) throws Exception
	{
		RestResponse restResponse = new RestResponse();
		UtilitiesAgent utilties = new UtilitiesAgent();
		restResponse.setMessage(FALSE);
		restResponse.setTimestamp(utilties.getTimeStampInMillis());
		restResponse.setError(NO_ERROR);
		restResponse.setStatus(RESPONSE_SUCCESS_CODE);
		restResponse.setException(NO_EXCEPTION);
		restResponse.setPath(url);
		return restResponse;
	}

	public RestResponse createErrorResponse(String url, String message, int errorCode)
	{
		RestResponse restResponse = new RestResponse();
		UtilitiesAgent utilties = new UtilitiesAgent();
		restResponse.setMessage(message);
		restResponse.setTimestamp(utilties.getTimeStampInMillis());
		restResponse.setError(ERROR);
		restResponse.setStatus(errorCode);
		restResponse.setException(NO_EXCEPTION);
		restResponse.setPath(url);
		return restResponse;
	}

}
