package eu.freme.common.persistence.model;

import javax.persistence.Entity;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 17.02.2016.
 */
@Entity
public class SimpleDiary extends OwnedResource {
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
