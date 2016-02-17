package eu.freme.common.persistence.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu.freme.common.exception.InternalServerErrorException;
import eu.freme.common.persistence.model.Diary;
import eu.freme.common.persistence.repository.DiaryRepository;
import org.springframework.stereotype.Component;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 17.02.2016.
 */
@Component
public class DiaryDAO extends OwnedResourceDAO<Diary>{

    @Override
    public String tableName() {
        return Diary.class.getSimpleName();
    }

    @Override
    public Diary findOneByIdentifierUnsecured(String identifier) {
        Diary diary = ((DiaryRepository)this.repository).findOneByName(identifier);
        // can be null, when checking for existence
        if(diary != null) {
            // deserialize events in the database
            diary.deserializeEvents();
        }
        return diary;
    }

    @Override
    public Diary save(Diary diary){
        // serialize events in preparation for saving to the database
        diary.serializeEvents();
        return super.save(diary);
    }

}
