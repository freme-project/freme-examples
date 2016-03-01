package eu.freme.common.persistence.dao;

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
        // we use the name as the identifier of a diary instead of the id
        return ((DiaryRepository)this.repository).findOneByName(identifier);
    }
}
