package model;

public class Line {
    private String number;
    private String position;
    private String comment;

    public Line(String number, String position, String comment) {
        this.number = number;
        this.position = position;
        this.comment = comment;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
