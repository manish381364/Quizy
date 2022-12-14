package com.example.quizy;

public class Questions {
    private String question;
    private String op1;
    private String op2;
    private String op3;
    private String op4;
    private String ans;
    public Questions(String question, String op1, String op2, String op3, String op4, String ans) {
        this.question = question;
        this.op1=op1;
        this.op2=op2;
        this.op3=op3;
        this.op4=op4;
        switch (ans) {
            case "1":
                this.ans = op1;
                break;
            case "2":
                this.ans = op2;
                break;
            case "3":
                this.ans = op3;
                break;
            case "4":
                this.ans = op4;
                break;
        }
    }
    public String getQuestion() {
        return question;
    }

    public String getOp1() {
        return op1;
    }

    public String getOp2() {
        return op2;
    }

    public String getOp3() {
        return op3;
    }

    public String getOp4() {
        return op4;
    }

    public String getAns() {
        return ans;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOp1(String op1) {
        this.op1 = op1;
    }

    public void setOp2(String op2) {
        this.op2 = op2;
    }

    public void setOp3(String op3) {
        this.op3 = op3;
    }

    public void setOp4(String op4) {
        this.op4 = op4;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
