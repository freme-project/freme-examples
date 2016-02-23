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
 * This class tests the DiaryController.
 *
 * Only the enhancements to the SimpleDiaryTest are explained here,
 * check the source code of SimpleDiaryTest to get a full explanation.
 *
 * Created by Arne on 22.02.2016.
 */
public class DiaryTest {
    private Logger logger = Logger.getLogger(DiaryTest.class);
    AuthenticatedTestHelper ath;
    OwnedResourceManagingHelper<Diary> ormh;

    public DiaryTest() throws UnirestException {
        ApplicationContext context = IntegrationTestSetup.getContext("diary-example-test-package.xml");
        ath = context.getBean(AuthenticatedTestHelper.class);
        // we have to give the identifier of a Diary as last parameter
        ormh = new OwnedResourceManagingHelper<Diary>("/complexdiary", Diary.class, ath);
        ath.authenticateUsers();
    }

    @Test
    public void testDiaryManagement(){

    }
}
