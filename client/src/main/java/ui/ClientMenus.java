package ui;

import model.GameData;
import model.UserData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;

import java.io.PrintStream;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientMenus {

    public static void helpMenuOptions(PrintStream out){
        out.println("Here are the following commands: ");
        out.print(SET_TEXT_COLOR_GREEN);
        out.print("   * Register - Provide a ");
        out.print(SET_TEXT_COLOR_ORANGE);
        out.print("<USERNAME> <PASSWORD> <EMAIL> ");
        out.print(SET_TEXT_COLOR_GREEN);
        out.println("to register a new user!");

        out.print(SET_TEXT_COLOR_BLUE);
        out.print("   * Login - Provide an existing ");
        out.print(SET_TEXT_COLOR_ORANGE);
        out.print("<USERNAME> <PASSWORD> ");
        out.print(SET_TEXT_COLOR_BLUE);
        out.println("to login!");

        out.print(SET_TEXT_COLOR_MAGENTA);
        out.println("   * Help - List of available commands");

        out.print(SET_TEXT_COLOR_RED);
        out.println("   * Quit your current session");
        out.println();
        out.print(SET_TEXT_COLOR_WHITE);
    }

    public static void helpAuthorizedOptions(PrintStream out){
        out.println("Here are the following available commands: ");
        out.print(SET_TEXT_COLOR_BLUE);
        out.print("   * Create - Provide a ");
        out.print(SET_TEXT_COLOR_ORANGE);
        out.print("<gameName> ");
        out.print(SET_TEXT_COLOR_BLUE);
        out.println("to create a new game of chess!");

        out.println("   * List - to list all current chess games!");

        out.print("   * Join - Provide a ");
        out.print(SET_TEXT_COLOR_ORANGE);
        out.print("<gameID> [White|Black] ");
        out.print(SET_TEXT_COLOR_BLUE);
        out.println("to join and play in an existing game of chess!");

        out.print("   * Observe - Provide a ");
        out.print(SET_TEXT_COLOR_ORANGE);
        out.print("<gameID> ");
        out.print(SET_TEXT_COLOR_BLUE);
        out.println("to watch an active game of chess!");

        out.println("   * Logout - When you are finished! ");

        out.print(SET_TEXT_COLOR_MAGENTA);
        out.println("   * Help - List of available commands");
        out.println();
        out.print(SET_TEXT_COLOR_WHITE);
    }


    public static UserData registerInfoSteps(PrintStream out, Scanner terminalReader){
        out.println("Create your username:");
        out.print("> ");
        String username = terminalReader.nextLine();
        while(username.isEmpty()){
            out.println("Username may not be blank, please try again: ");
            out.print("> ");
            username = terminalReader.nextLine();
        }
        out.println("Create your password:");
        out.print("> ");
        String password = terminalReader.nextLine();
        while(password.isEmpty()){
            out.println("Password may not be blank, please try again: ");
            out.print("> ");
            password = terminalReader.nextLine();
        }
        out.println("Enter your email:");
        out.print("> ");
        String email = terminalReader.nextLine();
        while(email.isEmpty()){
            out.println("Email may not be blank, please try again:");
            out.print("> ");
            email = terminalReader.nextLine();
        }
        return new UserData(username, password, email);
    }

    public static LoginRequest loginVerifySteps(PrintStream out, Scanner terminalReader){
        out.println("Enter your username:");
        out.print("> ");
        String username = terminalReader.nextLine();
        while(username.isEmpty()){
            out.println("Username may not be blank, please try again: ");
            out.print("> ");
            username = terminalReader.nextLine();
        }
        out.println("Enter your password:");
        out.print("> ");
        String password = terminalReader.nextLine();
        while(password.isEmpty()){
            out.println("Password may not be blank, please try again: ");
            out.print("> ");
            password = terminalReader.nextLine();
        }
        return new LoginRequest(username, password);
    }

    public static JoinGameRequest joinSteps(PrintStream out, Scanner terminalReader){
        out.println("Enter the # of the game you want to join and play:");
        out.print("> ");
        String givenID = terminalReader.nextLine();
        while(givenID.isEmpty()){
            out.println("Game # may not be blank, please try again: ");
            out.print("> ");
            givenID = terminalReader.nextLine();
        }
        int gameID = Integer.parseInt(givenID);
        out.println("Enter the color for the team you'd like to play as (White/BLack):");
        out.print("> ");
        String chosenTeam = terminalReader.nextLine();
        while(chosenTeam.isEmpty()){
            out.println("Team color may not be blank, please try again: ");
            out.print("> ");
            chosenTeam = terminalReader.nextLine();
        }
        return new JoinGameRequest(chosenTeam, gameID);
    }

    public static CreateGameRequest createSteps(PrintStream out, Scanner terminalReader){
        out.println("Enter a name for the chess game you'd like to create:");
        out.println("**Note: Creating a game does not automatically join you to that game**");
        out.print("> ");
        String gameName = terminalReader.nextLine();
        while(gameName.isEmpty()){
            out.println("The game name may not be blank, please try again: ");
            out.print("> ");
            gameName = terminalReader.nextLine();
        }
        return new CreateGameRequest(gameName);
    }

    public static String observeSteps(PrintStream out, Scanner terminalReader){
        out.println("Enter the # of the game you wish to observe:");
        out.print("> ");
        String givenID = terminalReader.nextLine();
        while(givenID.isEmpty()){
            out.println("Game # may not be blank, please try again: ");
            out.print("> ");
            givenID = terminalReader.nextLine();
        }
        return givenID;
    }

    public static String gameDataInterpreter(GameData gameInList){
        String clientReadableGame = ""; //= "Game ID #: " + gameInList.gameID() + " --";
        if(gameInList.whiteUsername() == null){
            clientReadableGame += " White Team: <Available>" + " --";
        }
        else{
            clientReadableGame += " White Team: \"" + gameInList.whiteUsername() + "\" --";
        }
        if(gameInList.blackUsername() == null){
            clientReadableGame += " Black Team: <Available>" + " --";
        }
        else{
            clientReadableGame += " Black Team: \"" + gameInList.blackUsername() + "\" --";
        }
        clientReadableGame += " Game Name: \"" + gameInList.gameName() + "\"\n";
        return clientReadableGame;
    }


}
