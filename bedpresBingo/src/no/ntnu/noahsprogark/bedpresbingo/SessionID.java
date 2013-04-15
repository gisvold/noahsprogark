package no.ntnu.noahsprogark.bedpresbingo;

/**
 * User: Nina Margrethe Smørsgård
 * GitHub: https://github.com/NinaMargrethe/
 * Date: 4/15/13
 */
public class SessionID {

    private long duration;
    private static final long DURATION = 60 * 60 * 1000;
    private int gameID;
    private String id;

    SessionID() {
        duration = System.currentTimeMillis() + (DURATION);

    }

    public long getDuration() {
        return duration;
    }

    public String getId() {
        return id;
    }


}