package ui;

import model.UserData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;

import java.io.PrintStream;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientMenus {

    public static void helpMenuOptions(PrintStream OUT){
        OUT.println("Here are the following commands: ");
        OUT.print(SET_TEXT_COLOR_GREEN);
        OUT.print("   * Register - Provide a ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<USERNAME> <PASSWORD> <EMAIL> ");
        OUT.print(SET_TEXT_COLOR_GREEN);
        OUT.println("to register a new user!");

        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.print("   * Login - Provide an existing ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<USERNAME> <PASSWORD> ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.println("to login!");

        OUT.print(SET_TEXT_COLOR_MAGENTA);
        OUT.println("   * Help - List of available commands");

        OUT.print(SET_TEXT_COLOR_RED);
        OUT.println("   * Quit your current session");
        OUT.println();
        OUT.print(SET_TEXT_COLOR_WHITE);
    }

    public static void helpAuthorizedOptions(PrintStream OUT){
        OUT.println("Here are the following available commands: ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.print("   * Create - Provide a ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<gameName> ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.println("to create a new game of chess!");

        OUT.println("   * List - to list all current chess games!");

        OUT.print("   * Join - Provide a ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<gameID> [White|Black] ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.println("to join and play in an existing game of chess!");

        OUT.print("   * Observe - Provide a ");
        OUT.print(SET_TEXT_COLOR_ORANGE);
        OUT.print("<gameID> ");
        OUT.print(SET_TEXT_COLOR_BLUE);
        OUT.println("to watch an active game of chess!");

        OUT.println("   * Logout - When you are finished! ");

        OUT.print(SET_TEXT_COLOR_MAGENTA);
        OUT.println("   * Help - List of available commands");
        OUT.println();
        OUT.print(SET_TEXT_COLOR_WHITE);
    }


    public static UserData registerInfoSteps(PrintStream OUT, Scanner TERMINAL_READER){
        OUT.println("Create your username:");
        OUT.print("> ");
        String username = TERMINAL_READER.nextLine();
        while(username.isEmpty()){
            OUT.println("Username may not be blank, please try again: ");
            username = TERMINAL_READER.nextLine();
        }
        OUT.println("Create your password:");
        OUT.print("> ");
        String password = TERMINAL_READER.nextLine();
        while(password.isEmpty()){
            OUT.println("Password may not be blank, please try again: ");
            password = TERMINAL_READER.nextLine();
        }
        OUT.println("Enter your email:");
        OUT.print("> ");
        String email = TERMINAL_READER.nextLine();
        while(email.isEmpty()){
            OUT.println("Email may not be blank, please try again:");
            email = TERMINAL_READER.nextLine();
        }
        return new UserData(username, password, email);
    }

    public static LoginRequest loginVerifySteps(PrintStream OUT, Scanner TERMINAL_READER){
        OUT.println("Enter your username:");
        OUT.print("> ");
        String username = TERMINAL_READER.nextLine();
        while(username.isEmpty()){
            OUT.println("Username may not be blank, please try again: ");
            username = TERMINAL_READER.nextLine();
        }
        OUT.println("Enter your password:");
        OUT.print("> ");
        String password = TERMINAL_READER.nextLine();
        while(password.isEmpty()){
            OUT.println("Password may not be blank, please try again: ");
            password = TERMINAL_READER.nextLine();
        }
        return new LoginRequest(username, password);
    }

    public static JoinGameRequest joinSteps(PrintStream OUT, Scanner TERMINAL_READER){
        OUT.println("Enter the game ID # for the game you want to join and play:");
        OUT.print("> ");
        String givenID = TERMINAL_READER.nextLine();
        while(givenID.isEmpty()){
            OUT.println("Game ID may not be blank, please try again: ");
            givenID = TERMINAL_READER.nextLine();
        }
        int gameID = Integer.parseInt(givenID);
        OUT.println("Enter the color for the team you'd like to play as (White/BLack):");
        OUT.print("> ");
        String chosenTeam = TERMINAL_READER.nextLine();
        while(chosenTeam.isEmpty()){
            OUT.println("Password may not be blank, please try again: ");
            chosenTeam = TERMINAL_READER.nextLine();
        }
        return new JoinGameRequest(chosenTeam, gameID);
    }

    public static CreateGameRequest createSteps(PrintStream OUT, Scanner TERMINAL_READER){
        OUT.println("Enter a name for the chess game you'd like to create:");
        OUT.println("**Note: Creating a game does not automatically join you to that game**");
        OUT.print("> ");
        String gameName = TERMINAL_READER.nextLine();
        while(gameName.isEmpty()){
            OUT.println("Game ID may not be blank, please try again: ");
            gameName = TERMINAL_READER.nextLine();
        }
        return new CreateGameRequest(gameName);
    }


}
