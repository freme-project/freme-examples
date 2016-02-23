package eu.freme.common.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import eu.freme.common.example.diary.DiaryEvent;
import eu.freme.common.exception.BadRequestException;
import eu.freme.common.exception.InternalServerErrorException;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a more complex model class for an access restricted entity object.
 * It contains a list of DiaryEvents which are (de)serialized while accessing
 * the database via the DiaryDAO. Therefore, it implements the methods
 * serializeEvents and deserializeEvents which are called in the overwritten
 * methods postRead and preSave.
 * Furthermore, entities of this class are identified by a "name" instead of the
 * default auto incremented "id".
 */
@Entity
public class Diary extends OwnedResource {
    // The (non default) identifier of the diary.
    // To replace the dfault identifier("id"),
    // DiaryDAO needs to overwrite OwnedResourceDAO.findOneByIdentifierUnsecured,
    // Diary needs to overwrite getIdentifier and
    // DiaryRepository has to define findOneByName(String name).
    String name;

    // we use "deserializedEvents" to build the json.
    @JsonIgnore
    @Lob // do not forget to mark large Strings as Lobs!
    String events;

    @Transient // do not persist this to the database
    List<DiaryEvent> deserializedEvents;

    // This default constructor is needed for jpa construction.
    // The owner has to  be set to "null", because no authenticated user
    // is available during jpa construction.
    public Diary(){
        super(null);
        deserializedEvents = new ArrayList<DiaryEvent>();
    }

    public Diary(String name) {
        // set visibility=PUBLIC, the creationTime
        // and owner={currently authenticated user}
        super();
        this.name = name;
        deserializedEvents = new ArrayList<DiaryEvent>();
        events = "[]";
    }

    // This is executed after the entity is created from the database content
    @Override
    public void postFetch(){
        deserializeEvents();
    }

    // This is executed before the entity will be persisted to the database
    @Override
    public void preSave(){
        serializeEvents();
    }

    @JsonIgnore
    @Override
    public String getIdentifier(){
        return getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEvents() throws JsonProcessingException {
        serializeEvents();
        return events;
    }

    public void serializeEvents() {
        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            events = ow.writeValueAsString(deserializedEvents);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Could not serialize DiaryEvents.");
        }
    }

    public void deserializeEvents() {
        try{
            ObjectMapper mapper = new ObjectMapper();
            deserializedEvents = mapper.readValue(events,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, DiaryEvent.class));
        } catch (IOException e) {
            throw new InternalServerErrorException("Could not deserialize DiaryEvents.");
        }
    }

    public void addEvent(DiaryEvent event){
        deserializedEvents.add(event);
    }

    public List<DiaryEvent> getDeserializedEvents(){
        return deserializedEvents;
    }
}
