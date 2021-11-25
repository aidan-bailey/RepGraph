package com.repgraph.repgraph;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import com.repgraph.controllers.LandingController;
import com.repgraph.models.Graph;
import com.repgraph.models.*;
import com.repgraph.services.LibraryService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class RepgraphApplicationTests {

	@Autowired
	private LandingController _landingController;

	@Autowired
	private LibraryService _libraryService;

	@Autowired
	private WebApplicationContext _webApplicationContext;

	/**
	 * SmokeTest for initial controller checks.
	 * 
	 * @throws Exception
	 */
	@Test
	void contextLoads() throws Exception {
		assertThat(_landingController).isNotNull();
		assertThat(_libraryService).isNotNull();
	}

	/**
	 * File upload check.
	 * 
	 * @throws Exception
	 */
	@Test
	void uploadFileTest() throws Exception {
		MockMultipartFile mockFile = new MockMultipartFile("file",
				Files.readAllBytes(Paths.get("src/test/resources/test.dmrs")));
		MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(_webApplicationContext).build();
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/landing/upload").file(mockFile))
				.andReturn();
		assertThat(result.getResponse().getStatus()).isEqualTo(200);
	}
}

