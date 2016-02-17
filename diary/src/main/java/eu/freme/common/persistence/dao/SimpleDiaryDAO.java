package eu.freme.common.persistence.dao;

import eu.freme.common.persistence.model.SimpleDiary;
import org.springframework.stereotype.Component;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 17.02.2016.
 */
@Component
public class SimpleDiaryDAO extends OwnedResourceDAO<SimpleDiary>{
    @Override
    public String tableName() {
        return SimpleDiary.class.getSimpleName();
    }
}
