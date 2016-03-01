package eu.freme.common.example.diary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.bservices.testhelper.AuthenticatedTestHelper;
import eu.freme.bservices.testhelper.OwnedResourceManagingHelper;
import eu.freme.bservices.testhelper.SimpleEntityRequest;
import eu.freme.bservices.testhelper.api.IntegrationTestSetup;
import eu.freme.common.persistence.model.Diary;
import eu.freme.common.rest.OwnedResourceManagingController;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

/**
 * This class tests the DiaryController.
 *
 * Only the enhancements to SimpleDiaryTest are explained here,
 * check the source code of SimpleDiaryTest to get a full explanation.
 *
 * Created by Arne on 22.02.2016.
 */
public class DiaryTest {
    private Logger logger = Logger.getLogger(DiaryTest.class);
    AuthenticatedTestHelper ath;
    OwnedResourceManagingHelper<Diary> ormh;

    public DiaryTest() throws UnirestException, IOException {
        ApplicationContext context = IntegrationTestSetup.getContext("diary-example-test-package.xml");
        ath = context.getBean(AuthenticatedTestHelper.class);
        ormh = new OwnedResourceManagingHelper<Diary>("/complexdiary", Diary.class, ath);
        ath.authenticateUsers();
    }

    @Test
    public void testDiaryManagement() throws IOException, UnirestException {

        logger.info("test Diary management");

        String diaryName = "myDiary";

        // create a first event
        DiaryEvent firstEvent = new DiaryEvent();
        firstEvent.setDescription("This is my first important event.");
        firstEvent.setPlace("here");
        firstEvent.setTime(System.currentTimeMillis());

        // the diary will be created with the first event,
        // so it has to go into the expected result
        Diary expectedCreatedDiary = new Diary();
        expectedCreatedDiary.addEvent(firstEvent);
        expectedCreatedDiary.setName(diaryName);
        expectedCreatedDiary.setDescription("This is my Diary");

        // create a second event
        DiaryEvent secondEvent = new DiaryEvent();
        secondEvent.addParticipant(ath.getUserWithoutPermission());
        secondEvent.setDescription("This is my second important event. I met UserWithoutPermission.");
        secondEvent.setPlace("there");
        secondEvent.setTime(System.currentTimeMillis());

        // create the expected updated result
        Diary expectedUpdatedDiary = new Diary();
        expectedUpdatedDiary.setName(expectedCreatedDiary.getName());
        expectedUpdatedDiary.setDescription(expectedCreatedDiary.getDescription());
        expectedUpdatedDiary.addEvent(firstEvent);
        // the update request just adds the second event
        expectedUpdatedDiary.addEvent(secondEvent);

        // serialize the events to send them as body
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        SimpleEntityRequest createRequest = new SimpleEntityRequest(ow.writeValueAsString(firstEvent))
                // IMPORTANT: we provide the identifier ("name"). It is set to "myDiary".
                .putParameter("name", diaryName)
                // We have set a description in expectedCreatedDiary, so we have to do it in the request, too.
                .putParameter(OwnedResourceManagingController.descriptionParameterName, expectedCreatedDiary.getDescription());

        SimpleEntityRequest updateRequest = new SimpleEntityRequest(ow.writeValueAsString(secondEvent));

        // The current user (ormh uses userWithPermission) is added automatically in the controller as participant.
        // So it has to be in the expected results, but not in the request bodies.
        // Therefore, it is added after serialization.
        firstEvent.addParticipant(ath.getUserWithPermission());
        secondEvent.addParticipant(ath.getUserWithPermission());

        ormh.checkCRUDOperations(createRequest, updateRequest,expectedCreatedDiary, expectedUpdatedDiary, "badDaysDiary");

    }
}
