package eu.freme.common.example.diary;

import eu.freme.common.persistence.model.User;

import java.util.ArrayList;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 17.02.2016.
 */
public class DiaryEvent {
    long time;
    String place;
    ArrayList<User> participants;
    String description;

    public DiaryEvent(){
        participants = new ArrayList<User>();
        time = System.currentTimeMillis();
    }

    public long getTime() {
        return time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public ArrayList<User> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<User> participants) {
        this.participants = participants;
    }

    public void addParticipant(User participant){
        participants.add(participant);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
