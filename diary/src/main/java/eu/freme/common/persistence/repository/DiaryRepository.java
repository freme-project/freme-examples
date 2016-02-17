package eu.freme.common.persistence.repository;

import eu.freme.common.persistence.model.Diary;
import eu.freme.common.persistence.model.SimpleDiary;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 17.02.2016.
 */
public interface DiaryRepository extends OwnedResourceRepository<Diary> {
    Diary findOneByName(String name);
}
