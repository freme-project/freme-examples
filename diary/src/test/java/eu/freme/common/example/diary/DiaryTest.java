package eu.freme.common.example.diary;

import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.bservices.testhelper.AuthenticatedTestHelper;
import eu.freme.bservices.testhelper.OwnedResourceManagingHelper;
import eu.freme.bservices.testhelper.api.IntegrationTestSetup;
import eu.freme.common.persistence.model.Diary;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

/**
 * Created by Arne on 22.02.2016.
 */
public class DiaryTest {
    private Logger logger = Logger.getLogger(DiaryTest.class);
    AuthenticatedTestHelper ath;
    OwnedResourceManagingHelper<Diary> ormh;

    public DiaryTest() throws UnirestException {
        ApplicationContext context = IntegrationTestSetup.getContext("diary-example-test-package.xml");
        ath = context.getBean(AuthenticatedTestHelper.class);
        ormh = new OwnedResourceManagingHelper<Diary>("/complexdiary", Diary.class, ath, "name");
        ath.authenticateUsers();
    }

    @Test
    public void testDiaryManagement(){

    }
}
