package ies.lab.app.dto;


public class Message {
    private String nMec;
    private int generatedNumber;
    private String type;

    // required by Jackson
    public Message() {
    }

    public Message(String nMec, int generatedNumber, String type) {
        this.nMec = nMec;
        this.generatedNumber = generatedNumber;
        this.type = type;
    }

    public String getnMec() {
        return nMec;
    }

    public void setnMec(String nMec) {
        this.nMec = nMec;
    }

    public int getGeneratedNumber() {
        return generatedNumber;
    }

    public void setGeneratedNumber(int generatedNumber) {
        this.generatedNumber = generatedNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "nMec='" + nMec + '\'' +
                ", generatedNumber=" + generatedNumber +
                ", type='" + type + '\'' +
                '}';
    }

}
