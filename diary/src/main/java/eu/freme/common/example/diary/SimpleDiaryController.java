package eu.freme.common.example.diary;

import eu.freme.common.exception.BadRequestException;
import eu.freme.common.persistence.model.SimpleDiary;
import eu.freme.common.rest.OwnedResourceManagingController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 17.02.2016.
 */

@RestController
@RequestMapping("/simplediary")
public class SimpleDiaryController extends OwnedResourceManagingController<SimpleDiary>{

    @Override
    protected SimpleDiary createEntity(String body, Map<String, String> parameters, Map<String, String> headers) throws BadRequestException {
        SimpleDiary newDiary = new SimpleDiary();
        newDiary.setContent(body);
        return newDiary;
    }

    @Override
    protected void updateEntity(SimpleDiary simpleDiary, String body, Map<String, String> parameters, Map<String, String> headers) throws BadRequestException {
        if(body.contains("bad day"))
            throw new BadRequestException("\"bad days\" won't go into the diary!");
        simpleDiary.setContent(simpleDiary.getContent()+"\n"+body);
    }
}
