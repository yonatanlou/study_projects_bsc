import java.util.Scanner;


class Chat {
    public static void main(String[] args) {
        String[] repliesToIllegalRequests1 = {"what <request>", "say I should say <request>",
                "please, its too long. just say say"};
        String[] repliesToIllegalRequests2 = {"say say <request>", "whaaat <request>"
                , "please stop", "I dont care about <request>", "please, its too long. just say say"};

        String[] repliesToLegalRequests = {"say <phrase>? okay: <phrase>", "<phrase>", "say <phrase> yourself!"};


        String[] botNames = {"Jonathan", "Matan"};
        ChatterBot[] bots = new ChatterBot[2];

        bots[0] = new ChatterBot(botNames[0], repliesToLegalRequests, repliesToIllegalRequests1);
        bots[1] = new ChatterBot(botNames[1], repliesToLegalRequests, repliesToIllegalRequests2);


        Scanner scanner = new Scanner(System.in);
        String statement = scanner.nextLine();

        for (int i = 0; ; i = i + 1) {
            statement = bots[i % bots.length].replyTo(statement);
            System.out.print(bots[i % bots.length].getName() + ": " + statement);
            scanner.nextLine();
        }


    }
}