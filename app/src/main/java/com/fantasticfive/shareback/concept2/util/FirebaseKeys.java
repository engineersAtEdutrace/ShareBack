package com.fantasticfive.shareback.concept2.util;

import com.fantasticfive.shareback.concept2.bean.ActiveSession;
import com.fantasticfive.shareback.concept2.bean.CreatedSession;
import com.fantasticfive.shareback.concept2.bean.JoinedSession;
import com.fantasticfive.shareback.concept2.bean.Session;

/**
 * Created by sagar on 19/2/17.
 */
public class FirebaseKeys {
    /**
     * |-active_sessions
     * |              |-session_id[]{Object of ActiveSession}    //**Important to keep session_id as key so that instructor can resume session
     * |                        |-session_id //**Important for JSON to Object Conversion Consistency
     * |                        |-instructor_id  //**Important to make entry in joined_users field
     * |                        |-session_name   //**Important so as to display session name
     * |                        |-active_users[] //Nice to Have
     * |-active_users
     * |            |-session_id
     * |                    |-user_id[] {Object of ActiveUser}
     * |                             |-user_id //**Important
     * |                             |-name //Just to Improve Response
     * |-users
     * |    |-user_id[]
     * |            |-session_id[] {Object of CreatedSession & JoinedSession}
     * |                    |-{CreatedSession}
     * |                    |            |-session_id
     * |                    |            |-session_name
     * |                    |            |-joined_users[]
     * |                    |            |-files[] {Object of SharedFile}
     * |                    |            |    |-path
     * |                    |            |    |-name
     * |                    |            |-comments[]
     * |                    |            |-ratings {Array of rating}
     * |                    |                |-rating1
     * |                    |                |-rating2
     * |                    |                |-rating3
     * |                    |                |-rating4
     * |                    |                |-rating5
     * |                    |-{JoinedSession}
     * |                                |-session_id
     * |                                |-session_name
     * |                                |-instructor_id
     */
    private static final String ACTIVE_SESSIONS = "active_sessions";
    private static final String ACTIVE_USERS = "active_users";
    private static final String INSTRUCTOR_ID = "instructor_id";
    private static final String FILES = "files";
    private static final String JOINED_USERS = "joined_users";
    private static final String USERS = "users";
    private static final String RATINGS = "ratings";
    private static final String COMMENTS = "comments";
    private static final String JOINED_SESSION = "joined_session";
    private static final String RATING_1 = "rating1";
    private static final String RATING_2 = "rating2";
    private static final String RATING_3 = "rating3";
    private static final String RATING_4 = "rating4";
    private static final String RATING_5 = "rating5";

    public static String activeSessions(){
        return ACTIVE_SESSIONS;
    }

    public static String activeSessionInstructor(String sessionId){
        return ACTIVE_SESSIONS+"/"+sessionId+"/"+INSTRUCTOR_ID;
    }

    public static String activeSessionUser(String sessionId){
        return ACTIVE_SESSIONS+"/"+sessionId+"/"+ACTIVE_USERS;
    }

    public static String activeUser(String sessionId){
        return ACTIVE_USERS+"/"+sessionId;
    }

    /*public static String userSessions(){
        String userId = UserData.getUserId();
        return USERS+"/"+userId;
    }*/

    public static String joinedUsers(ActiveSession session){
        String instructorId = session.getInstructorId();
        String sessionId = session.getSessionId();

        return USERS+"/"+instructorId+"/"+sessionId+"/"+JOINED_USERS;
    }

    public static String joinedUsers(CreatedSession session){
        String userId = UserData.getInstance().getUserId();
        String sessionId = session.getSessionId();

        return USERS+"/"+userId+"/"+sessionId+"/"+JOINED_USERS;
    }

    public static String sessionFiles(ActiveSession session){
        String instructorId = session.getInstructorId();
        String sessionId = session.getSessionId();

        return USERS+"/"+instructorId+"/"+sessionId+"/"+FILES;
    }

    public static String sessionFiles(String instructorId, String sessionId){
        return USERS+"/"+instructorId+"/"+sessionId+"/"+FILES;
    }

    public static String userSessions(){
        String userId = UserData.getInstance().getUserId();
        return USERS+"/"+userId;
    }

    public static String userSessionComments(CreatedSession session){
        String userId = UserData.getInstance().getUserId();
        String sessionId = session.getSessionId();
        return USERS+"/"+userId+"/"+sessionId+"/"+COMMENTS;
    }

    public static String userSessionRating(CreatedSession session){
        String userId = UserData.getInstance().getUserId();
        String sessionId = session.getSessionId();
        return USERS+"/"+userId+"/"+sessionId+"/"+RATINGS;
    }

    public static String rating(ActiveSession session, int rating){
        String instructorId = session.getInstructorId();
        String sessionId = session.getSessionId();
        return USERS+"/"+instructorId+"/"+sessionId+"/"+RATINGS;
    }

    /*public static String rating(ActiveSession session, int rating){
        String instructorId = session.getInstructorId();
        String sessionId = session.getSessionId();
        String ratingKey = "";
        switch (rating){
            case 1: ratingKey = RATING_1; break;
            case 2: ratingKey = RATING_2; break;
            case 3: ratingKey = RATING_3; break;
            case 4: ratingKey = RATING_4; break;
            case 5: ratingKey = RATING_5; break;
        }
        return USERS+"/"+instructorId+"/"+sessionId+"/"+ratingKey;
    }*/

    public static String comment(ActiveSession session){
        String instructorId = session.getInstructorId();
        String sessionId = session.getSessionId();
        return USERS+"/"+instructorId+"/"+sessionId+"/"+COMMENTS;
    }
}
