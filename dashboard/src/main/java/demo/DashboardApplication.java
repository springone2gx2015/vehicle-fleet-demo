package demo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

@EnableAutoConfiguration
@ComponentScan
@Controller
@EnableZuulProxy
public class DashboardApplication {
	private static final Logger logger = LoggerFactory
			.getLogger(DashboardApplication.class);
	@Autowired
	private ZuulProperties zuulProperties;

	public static void main(String[] args) {
		SpringApplication.run(DashboardApplication.class, args);
	}

	/**
	 * Size of a byte buffer to read/write file
	 */
	private static final int BUFFER_SIZE = 4096;

	@RequestMapping(value = "/download", method = RequestMethod.GET)
	// TODO: stub this out or remove it in UI
	public void downloadFile(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "columns") String columns,
			@RequestParam(value = "startDate") String startDate,
			@RequestParam(value = "endDate") String endDate,
			@RequestParam(value = "type") String type) {
		response.setContentType("text/csv");
		response.setHeader("X-Frame-Options", "ALLOWALL");

		String requestType = "text/csv";
		String fileExtension = ".csv";
		if (type.equals("json")) {
			requestType = "application/json";
			fileExtension = ".json";
		}
		else if (type.equals("tsv")) {
			requestType = "text/tsv";
			fileExtension = ".tsv";
		}
		else if (type.equals("psv")) {
			requestType = "text/psv";
			fileExtension = ".psv";
		}

		String fileName = "export-" + System.currentTimeMillis() + fileExtension;
		// creates mock data
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", fileName);
		response.setHeader(headerKey, headerValue);

		RestTemplate restTemplate = new RestTemplate();
		DataExportRequest exportRequest = new DataExportRequest();
		Set<String> requestedColumns = new HashSet<>();
		String[] columnParameter = columns.split(",");
		for (String columnRequest : columnParameter) {
			requestedColumns.add(columnRequest);
		}
		exportRequest.setColumns(requestedColumns);
		Long startDateRequest = 0L;
		Long endDateRequest = 0L;
		try {
			startDateRequest = Long.parseLong(startDate);
		}
		catch (NumberFormatException e) {
			logger.warn("Invalid Start Date...");
		}
		exportRequest.setStartDate(startDateRequest);

		try {
			endDateRequest = Long.parseLong(endDate);
		}
		catch (NumberFormatException e) {
			logger.warn("Invalid End Date...");
		}
		exportRequest.setEndDate(endDateRequest);

		ZuulRoute route = this.zuulProperties.getRoutes().get("data-export-service");
		String exportEndpoint = route.getUrl() + "/export";
		logger.info("Requesting export from: {}", exportEndpoint);

		MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
		headers.add("Accept", requestType);
		HttpEntity<DataExportRequest> entity = new HttpEntity<DataExportRequest>(
				exportRequest, headers);

		String exportContent = restTemplate.postForObject(exportEndpoint, entity,
				String.class);

		// get output stream of the response
		OutputStream outStream;
		try {
			outStream = response.getOutputStream();
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;

			// String exampleString =
			// "4V4NC9EH7FN187429,32.4857252827719,-100.19494832724";
			InputStream inputStream = new ByteArrayInputStream(
					exportContent.getBytes(StandardCharsets.UTF_8));

			// write bytes read from the input stream into the output stream
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			outStream.close();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@RequestMapping("/")
	public ModelAndView main() {
		logger.info("Showing dashboard...");
		return new ModelAndView("pages/dashboard");
	}

	@RequestMapping("/login")
	public String login() {
		return "login";
	}

	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class ApplicationSecurity extends WebSecurityConfigurerAdapter {

		@Autowired
		private SecurityProperties security;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.authorizeRequests().anyRequest().fullyAuthenticated().and().formLogin()
			.loginPage("/login").failureUrl("/login?error").permitAll().and()
			.logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			.logoutSuccessUrl("/login").and().csrf()
			.csrfTokenRepository(csrfTokenRepository()).and().headers()
			./* frameOptions(). */addHeaderWriter(new HeaderWriter() {

				@Override
				public void writeHeaders(HttpServletRequest arg0,
						HttpServletResponse response) {
					response.setHeader("X-Frame-Options", "ALLOWALL");
				}
			}).and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
		}

		@Override
		public void configure(AuthenticationManagerBuilder auth) throws Exception {
			/*
			 * The following users are set: admin/admin (roles: ADMIN, USER) user/user
			 * (roles: USER)
			 */
			auth.inMemoryAuthentication().withUser("admin").password("admin")
			.roles("ADMIN", "USER").and().withUser("user").password("user")
			.roles("USER");
		}

		private Filter csrfHeaderFilter() {
			return new OncePerRequestFilter() {
				@Override
				protected void doFilterInternal(HttpServletRequest request,
						HttpServletResponse response, FilterChain filterChain)
								throws ServletException, IOException {
					CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class
							.getName());
					if (csrf != null) {
						Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
						String token = csrf.getToken();
						if (cookie == null || token != null
								&& !token.equals(cookie.getValue())) {
							cookie = new Cookie("XSRF-TOKEN", token);
							cookie.setPath("/");
							response.addCookie(cookie);
						}
					}
					filterChain.doFilter(request, response);
				}
			};
		}

		private CsrfTokenRepository csrfTokenRepository() {
			HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
			repository.setHeaderName("X-XSRF-TOKEN");
			return repository;
		}
	}
}
