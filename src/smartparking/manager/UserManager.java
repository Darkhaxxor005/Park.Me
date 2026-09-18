package smartparking.manager;

import smartparking.model.User;

import java.util.ArrayList;

public class UserManager {

    private static ArrayList<User> users =
            DataStorage.loadUsers();


    public static ArrayList<User> getUsers() {

        return users;
    }

    public static boolean usernameExists(
            String username,
            int ignoreIndex) {

        for (int i = 0; i < users.size(); i++) {

            if (i != ignoreIndex &&
                    users.get(i).getUsername().equals(username)) {

                return true;
            }
        }

        return false;
    }

    public static boolean addUser(User user) {

        if (usernameExists(user.getUsername())) {
            return false;
        }

        users.add(user);

        DataStorage.saveUsers(users);

        return true;
    }


    public static void updateUser(
            int index,
            String username,
            String password) {

        User user = users.get(index);

        user.setUsername(username);
        user.setPassword(password);

        DataStorage.saveUsers(users);
    }


    public static void deleteUser(int index) {

        users.remove(index);

        DataStorage.saveUsers(users);
    }


    public static User getUser(int index) {

        return users.get(index);
    }


    public static int getUserCount() {

        return users.size();
    }


    public static boolean usernameExists(
            String username) {

        for (User user : users) {

            if (user.getUsername()
                    .equals(username)) {

                return true;
            }
        }

        return false;
    }


    public static boolean checkLogin(
            String username,
            String password) {

        for (User user : users) {

            if (user.getUsername()
                    .equals(username)
                    &&
                    user.getPassword()
                            .equals(password)) {

                return true;
            }
        }

        return false;
    }
}