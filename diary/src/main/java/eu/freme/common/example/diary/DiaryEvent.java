package eu.freme.common.example.diary;

import eu.freme.common.persistence.model.User;

import java.util.ArrayList;

/**
 * Created by Arne Binder (arne.b.binder@gmail.com) on 17.02.2016.
 */
public class DiaryEvent {
    long time;
    String place;
    ArrayList<User> participients;
    String description;

    public DiaryEvent(){
        participients = new ArrayList<User>();
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

    public ArrayList<User> getParticipients() {
        return participients;
    }

    public void setParticipients(ArrayList<User> participients) {
        this.participients = participients;
    }

    public void addParticipient(User participient){
        participients.add(participient);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
