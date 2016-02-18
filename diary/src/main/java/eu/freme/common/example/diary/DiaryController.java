package eu.freme.common.example.diary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.persistence.model.Diary;
import eu.freme.common.rest.OwnedResourceManagingController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 17.02.2016.
 */

@RestController
@RequestMapping("/complexdiary")
public class DiaryController extends OwnedResourceManagingController<Diary>{

    @Override
    protected Diary createEntity(String body, Map<String, String> parameters, Map<String, String> headers) throws BadRequestException {

        String name = parameters.get("name");
        if(Strings.isNullOrEmpty(name))
            throw new BadRequestException("The request parameter \"name\" was not found. Please provide a name for the new diary!");

        // check, if an entity with the given name is already in the db
        if(getEntityDAO().findOneByIdentifierUnsecured(name)!=null){
            throw new BadRequestException("A diary with the name=\""+name+"\" already exists! Please use another name for the new diary.");
        }

        Diary newDiary = new Diary(name);

        DiaryEvent newEvent;
        if(!Strings.isNullOrEmpty(body.trim())){
            // assume that the body contains json coding a DiaryEvent
            ObjectMapper mapper = new ObjectMapper();
            try {
                newEvent = mapper.readValue(body, DiaryEvent.class);
            } catch (IOException e) {
                throw new BadRequestException("Error in the json: could not create a DiaryEvent from body content. "+e.getMessage());
            }
        }else{
            // if there is no body, set some defaults
            newEvent = new DiaryEvent();
            newEvent.setDescription("this is my first event");
            newEvent.setPlace("here");
        }

        // Add the owner to the participants.
        // This is possible, because the Diary constructor calls
        // the default OwnedResource constructor. This is not necessary,
        // if you do not need the current user in this method.
        // The current user will be set as owner in
        // OwnedResourceManagingController.addEntity also, if the owner
        // is not already set.
        newEvent.addParticipant(newDiary.getOwner());
        newDiary.addEvent(newEvent);

        return newDiary;
    }

    @Override
    protected void updateEntity(Diary diary, String body, Map<String, String> parameters, Map<String, String> headers) throws BadRequestException {

        ObjectMapper mapper = new ObjectMapper();

        DiaryEvent newEvent;
        try {
            newEvent = mapper.readValue(body, DiaryEvent.class);
        } catch (IOException e) {
            throw new BadRequestException("Error in the json: could not create a DiaryEvent from body content. "+e.getMessage());
        }

        diary.addEvent(newEvent);

    }
}
