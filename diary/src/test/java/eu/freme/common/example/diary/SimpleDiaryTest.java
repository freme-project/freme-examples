package eu.freme.common.example.diary;

import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.bservices.testhelper.AuthenticatedTestHelper;
import eu.freme.bservices.testhelper.OwnedResourceManagingHelper;
import eu.freme.bservices.testhelper.SimpleEntityRequest;
import eu.freme.bservices.testhelper.api.IntegrationTestSetup;
import eu.freme.common.persistence.model.SimpleDiary;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * Created by Arne on 22.02.2016.
 */
public class SimpleDiaryTest {
    private Logger logger = Logger.getLogger(SimpleDiaryTest.class);
    AuthenticatedTestHelper ath;
    OwnedResourceManagingHelper<SimpleDiary> ormh;

    public SimpleDiaryTest() throws UnirestException {
        ApplicationContext context = IntegrationTestSetup.getContext("diary-example-test-package.xml");
        ath = context.getBean(AuthenticatedTestHelper.class);
        ormh = new OwnedResourceManagingHelper<SimpleDiary>("/simplediary", SimpleDiary.class, ath, null);
        ath.authenticateUsers();
    }

    @Test
    public void testSimpleDiaryManagmenent() throws IOException, UnirestException {

        SimpleEntityRequest request = new SimpleEntityRequest("my first day");
        SimpleEntityRequest updateRequest = new SimpleEntityRequest("my second day");
        //ormh.checkCRUDOperations(request, updateRequest);
    }

}
