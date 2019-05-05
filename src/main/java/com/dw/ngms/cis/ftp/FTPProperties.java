package com.dw.ngms.cis.ftp;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FTPProperties {

	private String server;
    private String username = "";
    private String password = "";
    @Min(0)
    @Max(65535)
    private int port;
    private int keepAliveTimeout;
    private boolean autoStart;

    @PostConstruct
    public void init() {
        if (port == 0) {
            port = 21;
        }
    }

	public FTPProperties(String server, String username, String password, @Min(0) @Max(65535) int port,
			int keepAliveTimeout, boolean autoStart) {
		super();
		this.server = server;
		this.username = username;
		this.password = password;
		this.port = port;
		this.keepAliveTimeout = keepAliveTimeout;
		this.autoStart = autoStart;
	}
    
	public FTPProperties(String server, String username, String password, @Min(0) @Max(65535) int port) {
		super();
		this.server = server;
		this.username = username;
		this.password = password;
		this.port = port;
	}
}
