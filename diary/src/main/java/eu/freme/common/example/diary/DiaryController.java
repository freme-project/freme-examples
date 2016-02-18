package eu.freme.common.example.diary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.persistence.model.Diary;
import eu.freme.common.persistence.model.User;
import eu.freme.common.rest.OwnedResourceManagingController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

        if(getEntityDAO().findOneByIdentifierUnsecured(name)!=null){
            throw new BadRequestException("A diary with the name=\""+name+"\" already exists! Please use another name for the new diary.");
        }

        Diary newDiary = new Diary(name);

        DiaryEvent newEvent;
        if(!Strings.isNullOrEmpty(body)){
            ObjectMapper mapper = new ObjectMapper();
            try {
                newEvent = mapper.readValue(body, DiaryEvent.class);
            } catch (IOException e) {
                throw new BadRequestException("Error in the json: could not create a DiaryEvent from body content.");
            }
        }else{
            newEvent = new DiaryEvent();
            newEvent.setDescription("this is my first event");
            newEvent.setPlace("here");
        }

        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        newEvent.addParticipient(currentUser);
        newDiary.addEvent(newEvent);
        newDiary.serializeEvents();

        return newDiary;
    }

    @Override
    protected void updateEntity(Diary diary, String body, Map<String, String> parameters, Map<String, String> headers) throws BadRequestException {

        ObjectMapper mapper = new ObjectMapper();

        DiaryEvent newEvent;
        try {
            newEvent = mapper.readValue(body, DiaryEvent.class);
        } catch (IOException e) {
            throw new BadRequestException("Error in the json: could not create a DiaryEvent from body content.");
        }

        diary.addEvent(newEvent);

    }
}
