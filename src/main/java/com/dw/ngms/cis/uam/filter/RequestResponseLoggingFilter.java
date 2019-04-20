package com.dw.ngms.cis.uam.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.dw.ngms.cis.uam.entity.AuditEntry;
import com.dw.ngms.cis.uam.service.AuditEntryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RequestResponseLoggingFilter implements Filter {

	@Autowired
	private AuditEntryService auditEntryService;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper((HttpServletRequest) request);
		ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper((HttpServletResponse) response);
		AuditEntry auditEntry = logRequestAuditEntry(requestWrapper);
		log.trace("Logging request :{}", auditEntry.toString());
		try {
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
        	auditEntry = logResponseAuditEntry(responseWrapper, auditEntry);
        	log.trace("Logging response :{}", auditEntry.getRequestUrl());
            responseWrapper.copyBodyToResponse();
        }
	}//doFilter	

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	private AuditEntry logRequestAuditEntry(ContentCachingRequestWrapper requestWrapper) {
		if(requestWrapper == null) {
			log.error("Request wrapper is required to log an audit entry");
			return null;
		}
		String operation = requestWrapper.getHeader("operation");
		String userCode = requestWrapper.getHeader("usercode");
		String userName = requestWrapper.getHeader("username");
		String userType = requestWrapper.getHeader("usertype");
		String requestUrl = getRequestUrl(requestWrapper);		
		String requestBody = new String(requestWrapper.getContentAsByteArray());
		return auditEntryService.logAuditEntry(new AuditEntry(operation, requestUrl, userCode, userName, userType, requestBody));
	}//logRequestAuditEntry
	
	private AuditEntry logResponseAuditEntry(ContentCachingResponseWrapper responseWrapper, AuditEntry auditEntry) {
		if(responseWrapper == null) {
			log.error("Response wrapper is required to log an audit entry");
			return null;
		}
		if(auditEntry == null) {
			log.error("Audit entry is required to log");
			return null;
		}
		String responseBody = new String(responseWrapper.getContentAsByteArray());
		auditEntry.setResponseDatetime(new Date());
		auditEntry.setResponseJson(responseBody);
		return auditEntryService.logAuditEntry(auditEntry);
	}//logResponseAuditEntry
	
	private String getRequestUrl(ContentCachingRequestWrapper requestWrapper) {
		String requestUrl = requestWrapper.getRequestURI();
		if("GET".equals(requestWrapper.getMethod()))
			requestUrl = requestUrl+"?"+requestWrapper.getQueryString();
		return requestUrl;
	}//getRequestUrl
}
