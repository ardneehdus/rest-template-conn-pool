package open.source.learn.resttemplate.config;

import java.util.ArrayList;
import java.util.List;


import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import open.source.learn.resttemplate.services.util.ConfigHelperService;


@Configuration
@ComponentScan({"open.source.learn.resttemplate.services"})
@PropertySource({"classpath:/environment.properties"})
public class HttpClientConfig {
	
	@Autowired
	private ConfigHelperService configHelperService;
	
	@Bean
	@Qualifier("restTemplate")
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Primary
	@Bean
	@Qualifier("restTemplateWithHttpConnPool")	
	public RestTemplate getRestTemplate() {
		RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
		restTemplate.setInterceptors(getCustomInterceptorsForRestTemplate());
		return restTemplate;
	}
	
	@Bean
	public ClientHttpRequestFactory httpRequestFactory() {
		HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient());
		httpComponentsClientHttpRequestFactory.setReadTimeout(configHelperService.getConnectionReadTimeoutInMilliSecond());
		return httpComponentsClientHttpRequestFactory;
	}
	
	@Bean
	public HttpClient httpClient() {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(configHelperService.getDefaultConnectionReadTimeoutInMilliSecond()).build();
		HttpClientBuilder builder = HttpClientBuilder.create();
		
		PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();
		poolingConnManager.setMaxTotal(configHelperService.getDefaultMaxHttpConnections());
		poolingConnManager.setDefaultMaxPerRoute(configHelperService.getDefaultMaxHttpConnectionsPerRoute());
		
		builder.setConnectionManager(poolingConnManager);
		builder.setDefaultRequestConfig(requestConfig);
		
		CloseableHttpClient httpClient = builder.build();
		return httpClient;
	}	

	private List<ClientHttpRequestInterceptor> getCustomInterceptorsForRestTemplate() {
		List<ClientHttpRequestInterceptor> listInterceptors = new ArrayList<>();
		listInterceptors.add(getRestTemplateHttpInterceptor());
		return listInterceptors;
	}

	private ClientHttpRequestInterceptor getRestTemplateHttpInterceptor() {
		return (request, body, execution) -> {
			//do anything as and when required
			return execution.execute(request, body);
		};
	}

}
