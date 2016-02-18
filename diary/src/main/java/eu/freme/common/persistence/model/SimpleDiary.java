package eu.freme.common.persistence.model;

import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * This is a simple model class for an access restricted entity object.
 * It contains a data property "content" and the inherited properties from
 * OwnedResource.
 * In most cases similar implementations should fulfill your needs.
 */
@Entity
public class SimpleDiary extends OwnedResource {

    @Lob // do not forget to mark large Strings as Lobs!
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
