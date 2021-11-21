//import java.util.*;
//
///**
// * Base file for the ChatterBot exercise.
// * The bot's replyTo method receives a statement.
// * If it starts with the constant REQUEST_PREFIX, the bot returns
// * an answer (supplied via the constructor), that contains what after
// * the prefix. Otherwise, it returns one of
// * a few possible replies as supplied to it via its constructor.
// * In this case, it may also include the statement after
// * the selected reply (coin toss).
// *
// * @author Yonatan Lourie
// */
//class ChatterBot {
//    /**
//     * After that word (REQUEST_PREFIX), the bot will reply the rest of the sentence.
//     */
//    static final String REQUEST_PREFIX = "say ";
//    static final String REQUESTED_PHRASE_PLACEHOLDER = "<phrase>";
//
//    Random rand = new Random();
//    String[] repliesToIllegalRequest;
//    String[] repliesToLegalRequest;
//    String name;
//
//    /**
//     * Constructor
//     *
//     * @param name                    The name of the bot.
//     * @param repliesToLegalRequest   Array of possible replies to legal requests.
//     * @param repliesToIllegalRequest Array of possible replies to illegal requests.
//     */
//    ChatterBot(String name, String[] repliesToLegalRequest, String[] repliesToIllegalRequest) {
//        this.name = name;
//        this.repliesToIllegalRequest = new String[repliesToIllegalRequest.length];
//        for (int i = 0; i < repliesToIllegalRequest.length; i = i + 1) {
//            this.repliesToIllegalRequest[i] = repliesToIllegalRequest[i];
//        }
//
//        this.repliesToLegalRequest = new String[repliesToLegalRequest.length];
//        for (int i = 0; i < repliesToLegalRequest.length; i = i + 1) {
//            this.repliesToLegalRequest[i] = repliesToLegalRequest[i];
//        }
//    }
//
//    /**
//     * @return The name of the bot.
//     */
//    String getName() {
//        return this.name;
//    }
//
//    /**
//     * reply to the given statement, will reply different if the statement is legal or not.
//     *
//     * @param statement the string that the bot get and will reply to.
//     * @return String new statement
//     */
//    String replyTo(String statement) {
//        if (statement.startsWith(REQUEST_PREFIX)) {
//            String phrase = statement.replaceFirst(REQUEST_PREFIX, "");
//            return respondToLegalRequest(phrase);
//        }
//        return respondToIllegalRequest(statement);
//    }
//
//    /**
//     * will respond to the statement with one of the possible replies (or without the statement).
//     *
//     * @param statement string that not contains the REQUEST_PREFIX.
//     * @return new statement.
//     */
//    String respondToIllegalRequest(String statement) {
//        int randomIndex = rand.nextInt(repliesToIllegalRequest.length);
//        String reply = repliesToIllegalRequest[randomIndex];
//        if (rand.nextBoolean()) {
//            reply = reply + statement;
//        }
//        return reply;
//    }
//
//    /**
//     * will respond to the statement with one of the possible replies (or just the statement).
//     *
//     * @param statement string that contains the REQUEST_PREFIX.
//     * @return new statement (or just the statement).
//     */
//    String respondToLegalRequest(String statement) {
//        int randomIndex = rand.nextInt(repliesToLegalRequest.length);
//        String reply;
//        String responsePattern = repliesToLegalRequest[randomIndex];
//        if (rand.nextBoolean()) {
//            reply = responsePattern.replaceAll(REQUESTED_PHRASE_PLACEHOLDER, statement);
//            return reply;
//        }
//        return statement;
//
//    }
//}
