package Models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message implements Comparable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("sender")
    private String sender;

    @JsonProperty("receiver")
    private String receiver;

    @JsonProperty("text")
    private String text;

    @JsonProperty("messageDate")
    private long messageDate;

    public Message () {

    }

    public Message(String id, String sender, String receiver, String text, long messageDate) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.messageDate = messageDate;
    }

    public Message(String id, String sender, String receiver, String text) {
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.messageDate = System.currentTimeMillis();
    }

    public Message(String sender, String receiver, String text) {
        this.id = sender + System.currentTimeMillis();
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.messageDate = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(long messageDate) {
        this.messageDate = messageDate;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", sender='" + sender + '\'' +
                ", receiver='" + receiver + '\'' +
                ", text='" + text + '\'' +
                ", createdAt=" + messageDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Message message = (Message) o;

        if (messageDate != message.messageDate) return false;
        if (!id.equals(message.id)) return false;
        if (!sender.equals(message.sender)) return false;
        if (!receiver.equals(message.receiver)) return false;
        return text.equals(message.text);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + sender.hashCode();
        result = 31 * result + receiver.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + (int) (messageDate ^ (messageDate >>> 32));
        return result;
    }

    @Override
    public int compareTo(Object o) {
        return (int)(((Message)o).messageDate - messageDate);
    }
}