import java.util.ArrayList;
import java.util.List;

class RecordsObj {
    List<RecordObj> records;
    List<RecordObj> recordsNoms;
    List<RecordObj> recordsAccuses;

    public RecordsObj() {
        records = new ArrayList<>();
        recordsNoms = new ArrayList<>();
        recordsAccuses = new ArrayList<>();
    }

    public void addRecord(String type, String p1, double x, String p2) {
        RecordObj record = new RecordObj(type, p1, x, p2);
        records.add(record);
        switch (type) {
            case "nomination":
                recordsNoms.add(record);
                break;
            case "accusation":
                recordsAccuses.add(record);
                break;
        }
    }

    public List<RecordObj> getRecords() {
        return records;
    }

    public List<RecordObj> getNomRecords() {
        return recordsNoms;
    }

    public List<RecordObj> getAccuseRecords() {
        return recordsAccuses;
    }
}

class RecordObj {
    private String type;
    private String player1;
    private String player2;
    private double score;

    public RecordObj(String type, String player1, double score, String player2) {
        this.type = type;
        this.player1 = player1;
        this.score = score;
        this.player2 = player2;
    }

    public String getType() {
        return type;
    }

    public String getPlayer1() {
        return player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public double getScore() {
        return score;
    }
}