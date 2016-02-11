package eu.freme.eservices.example;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import org.springframework.context.ApplicationContext;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import eu.freme.bservices.testhelper.TestHelper;
import eu.freme.common.starter.FREMEStarter;

public class ECapitalizationServiceTest {

	@Test
	public void test() throws UnirestException {
		ApplicationContext context = FREMEStarter
				.startPackageFromClasspath("e-capitalization-test-package.xml");
		TestHelper testHelper = context.getBean(TestHelper.class);

		HttpResponse<String> response = Unirest
				.post(testHelper.getAPIBaseUrl() + "/e-capitalization")
				.queryString("input", "hello world")
				.queryString("informat", "text").asString();

		assertTrue(response.getStatus() == 200);
		assertTrue(response.getBody().contains("HELLO WORLD"));
	}
}
