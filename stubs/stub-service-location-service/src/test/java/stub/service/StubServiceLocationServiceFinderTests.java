package stub.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.restdocs.RestDocumentation.document;
import static org.springframework.restdocs.RestDocumentation.documentationConfiguration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import stub.service.StubServiceLocationServiceApplicationTests.ForwardAwareMockMvcBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StubServiceLocationServiceApplication.class)
@WebAppConfiguration
public class StubServiceLocationServiceFinderTests {

	@Autowired
	WebApplicationContext context;

	@Value("${org.springframework.restdocs.outputDir:target/generated-snippets}")
	private String restdocsOutputDir;

	private MockMvc mockMvc;

	@Before
	public void init() {
		System.setProperty("org.springframework.restdocs.outputDir",
				this.restdocsOutputDir);
		this.mockMvc = ForwardAwareMockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration()).build();
	}

	@Test
	public void getLocations() throws Exception {
		this.mockMvc
		.perform(MockMvcRequestBuilders.get(
				"/serviceLocations/search/findFirstByLocationNear?location={lat},{long}",
				39, -77))
		.andExpect(
				MockMvcResultMatchers.content().string(containsString("_links")))
		.andDo(document("findByLocation"));
	}

}