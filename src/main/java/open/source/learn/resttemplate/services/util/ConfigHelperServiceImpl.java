package open.source.learn.resttemplate.services.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ConfigHelperServiceImpl implements ConfigHelperService {
	
	@Autowired
	private Environment environment;
	
	private static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 500;
	
	private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;

	private static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (5 * 60 * 1000);

	private static final int READ_TIMEOUT = (5 * 60 * 1000);

	@Override
	public int getDefaultMaxHttpConnections() {
		return environment.getProperty("http.client.config.connection.max", Integer.class, DEFAULT_MAX_TOTAL_CONNECTIONS);
	}

	@Override
	public int getDefaultMaxHttpConnectionsPerRoute() {
		return environment.getProperty("http.client.config.connection.routemax", Integer.class, DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
	}

	@Override
	public int getConnectionReadTimeoutInMilliSecond() {
		return environment.getProperty("http.client.config.connection.read.timeout.millisec", Integer.class, READ_TIMEOUT);
	}

	@Override
	public int getDefaultConnectionReadTimeoutInMilliSecond() {
		return environment.getProperty("http.client.config.connection.read.timeout.default.millisec", Integer.class, DEFAULT_READ_TIMEOUT_MILLISECONDS);
	}

}
