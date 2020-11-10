package com.codecool.bfsexample;

import com.codecool.bfsexample.model.UserNode;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BFSExample {
    private static List<UserNode> users;
    private static void populateDB() {

        RandomDataGenerator generator = new RandomDataGenerator();
        users = generator.generate();

        GraphPlotter graphPlotter = new GraphPlotter(users);
        
        System.out.println("Done!");
    }

    public static void main(String[] args) {
        populateDB();
        Random random = new Random();
        int first = random.nextInt(users.size());
        int second = random.nextInt(users.size());
        while(first == second) {
            second = random.nextInt(users.size());
        }
        UserNode firstUser = users.get(first);
        UserNode secondUser = users.get(second);
        System.out.println("\n===================");
        getMinimumDistance(firstUser, secondUser);
        System.out.println("\n===================");
        getFriendsOfFriendsAtGivenDistance(firstUser, 3);
        System.out.println("\n===================");
        getShortestPathsBetweenUsers(firstUser, secondUser);
    }

    private static void getMinimumDistance(UserNode firstUser, UserNode secondUser) {
        System.out.println("The minimum distance between "+ firstUser.getFullName() + " and " + secondUser.getFullName()+":");

        int distance = 0;
        Set<UserNode> visitedUsers = new HashSet<>();
        visitedUsers.add(firstUser);
        Set<UserNode> usersToVisit = firstUser.getFriends();
        boolean found = false;

        while(!found && visitedUsers.size() != users.size()) {
            distance++;
            Set<UserNode> friendsOfFriends = new HashSet<>();
            for (UserNode userNode : usersToVisit) {
                if (userNode.equals(secondUser)){
                    found = true;
                    break;
                }
                visitedUsers.add(userNode);
                friendsOfFriends.addAll(userNode.getFriends().stream().filter(userNode1 -> !visitedUsers.contains(userNode1)).collect(Collectors.toSet()));
            }
            usersToVisit = friendsOfFriends;
        }
        System.out.println(distance);
    }

    private static void getFriendsOfFriendsAtGivenDistance(UserNode firstUser, int distance) {
        System.out.println(firstUser.getFullName()+"'s friends of friends at the distance of "+ distance +":");

        Set<UserNode> visitedUsers = new HashSet<>();
        visitedUsers.add(firstUser);
        Set<UserNode> friendsOfFriends = firstUser.getFriends();

        for (int i = 1; i < distance; i++) {
            Set<UserNode> temporaryFriendsOfFriends = new HashSet<>();
            for (UserNode userNode : friendsOfFriends) {
                visitedUsers.add(userNode);
                temporaryFriendsOfFriends.addAll(userNode.getFriends().stream().filter(userNode1 -> !visitedUsers.contains(userNode1)).collect(Collectors.toSet()));
            }
            friendsOfFriends = temporaryFriendsOfFriends;
        }
        System.out.println(friendsOfFriends);
    }

    private static void getShortestPathsBetweenUsers(UserNode firstUser, UserNode secondUser) {
        System.out.println("Shortest paths between "+ firstUser.getFullName() + " and " + secondUser.getFullName()+":");
    }
}
