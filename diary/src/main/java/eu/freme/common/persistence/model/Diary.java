package eu.freme.common.persistence.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import eu.freme.common.example.diary.DiaryEvent;
import eu.freme.common.exception.InternalServerErrorException;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 17.02.2016.
 */
@Entity
public class Diary extends OwnedResource {
    String name;

    @JsonIgnore
    @Lob
    String events;

    @Transient
    List<DiaryEvent> deserializedEvents;

    public Diary(){}

    public Diary(String name) {
        super();
        this.name = name;
        deserializedEvents = new ArrayList<DiaryEvent>();
        events = "[]";
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
            throw new InternalServerErrorException("Could not serialize DiaryEvents.");
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
