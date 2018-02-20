package open.source.learn.resttemplate.services.util;

public interface ConfigHelperService {
	
	public int getDefaultMaxHttpConnections();
	
	public int getDefaultMaxHttpConnectionsPerRoute();
	
	public int getConnectionReadTimeoutInMilliSecond();
	
	public int getDefaultConnectionReadTimeoutInMilliSecond();

}
