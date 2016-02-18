package eu.freme.common.persistence.repository;

import eu.freme.common.persistence.model.Diary;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 17.02.2016.
 */
public interface DiaryRepository extends OwnedResourceRepository<Diary> {
    // we have to define this to use name as identifier
    Diary findOneByName(String name);
}
