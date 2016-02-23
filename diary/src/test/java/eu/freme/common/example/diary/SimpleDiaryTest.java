package eu.freme.common.example.diary;

import com.mashape.unirest.http.exceptions.UnirestException;
import eu.freme.bservices.testhelper.AuthenticatedTestHelper;
import eu.freme.bservices.testhelper.LoggingHelper;
import eu.freme.bservices.testhelper.OwnedResourceManagingHelper;
import eu.freme.bservices.testhelper.SimpleEntityRequest;
import eu.freme.bservices.testhelper.api.IntegrationTestSetup;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.persistence.model.SimpleDiary;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * Created by Arne on 22.02.2016.
 */
public class SimpleDiaryTest {
    private Logger logger = Logger.getLogger(SimpleDiaryTest.class);
    AuthenticatedTestHelper ath;
    OwnedResourceManagingHelper<SimpleDiary> ormh;

    // initialize the ApplicationContext, helper objects and authenticate the users needed for the tests
    public SimpleDiaryTest() throws UnirestException {
        // load the needed modules and create the ApplicationContext
        ApplicationContext context = IntegrationTestSetup.getContext("diary-example-test-package.xml");
        // Create the AuthenticatedTestHelper.
        // It provides tokens for a userWithPermission, a userWithoutPermission and an admin.
        // There is no difference between the userWithPermission and the userWithoutPermission,
        // but it is intended to use the first one to create items, so it will be the owner of them,
        // and check access restrictions with the other.
        ath = context.getBean(AuthenticatedTestHelper.class);
        // Create a OwnedResourceManagingHelper handling SimpleDiary entities.
        // All CRUD operations will be send to the management endpoints at "/simplediary" according to the
        // RequestMapping annotation of the SimpleDiaryController. Have a look at the source code of
        // the SimpleDiaryController for further information.
        // To let the ormh convert the SimpleDiary to json and back to check its content,
        // the entity class has to be provided as second argument.
        // Furthermore, it needs the AuthenticatedTestHelper.
        // We don't have to name a certain entityIdentifier because the default id is used.
        ormh = new OwnedResourceManagingHelper<SimpleDiary>("/simplediary", SimpleDiary.class, ath, null);
        // create the tokens for the users described above
        ath.authenticateUsers();
    }

    @Test
    public void testSimpleDiaryManagmenent() throws IOException, UnirestException {

        logger.info("test SimpleDiary entity management");

        // create a SimpleEntityRequest to create a SimpleDiary. Set the content to: "My first Day"
        SimpleEntityRequest createRequest = new SimpleEntityRequest("My first day");
        // create a SimpleEntityRequest to update the SimpleDiary with "My second day"
        SimpleEntityRequest updateRequest = new SimpleEntityRequest("My second day");

        // create a SimpleDiary. After sending the createRequest,
        // the response has to contain at least the fields defined in this SimpleDiary.
        SimpleDiary expectedCreatedDiary = new SimpleDiary();
        // the expected content is: "My first day"
        expectedCreatedDiary.setContent("My first day");

        // create another SimpleDiary. After sending the updateRequest,
        // the response has to contain at least the fields defined in this SimpleDiary.
        SimpleDiary expectedUpdatedDiary = new SimpleDiary();
        // According to the implementation of SimpleDairyController.updateEntity,
        // the body content of the update request will be added as new line to the previous content.
        expectedUpdatedDiary.setContent(expectedCreatedDiary.getContent() + "\n" + "My second day");

        // Let the OwnedResourceManagingHelper do all CRUD checks.
        // In addition, this needs an identifier which points to no item.
        // We use a numeric one ("999"), because a SimpleDiary is identified by the numerical default id.
        ormh.checkCRUDOperations(createRequest, updateRequest, expectedCreatedDiary, expectedUpdatedDiary, "999");
    }

    @Test
    public void testBadDays() throws IOException, UnirestException {
        logger.info("create a SimpleDiary: My first day");
        SimpleDiary diary = ormh.createEntity(new SimpleEntityRequest("My first day"), ath.getTokenWithPermission(), HttpStatus.OK);

        logger.info("try to update the diary with \"This was a bad day!\": But we do not take bad days, so this should return BAD_REQUEST");
        LoggingHelper.loggerIgnore(BadRequestException.class.getCanonicalName());
        ormh.updateEntity(diary.getIdentifier(), new SimpleEntityRequest("This was a bad day!"), ath.getTokenWithPermission(), HttpStatus.BAD_REQUEST);
        LoggingHelper.loggerUnignore(BadRequestException.class.getCanonicalName());

        logger.info("remove the SimpleDiary");
        ormh.deleteEntity(diary.getIdentifier(), ath.getTokenWithPermission(), HttpStatus.OK);
    }

}
