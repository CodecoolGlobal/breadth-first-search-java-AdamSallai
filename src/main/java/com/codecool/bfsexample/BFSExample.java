package com.codecool.bfsexample;

import com.codecool.bfsexample.model.UserNode;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BFSExample {
    private static List<UserNode> users;
    private static GraphPlotter graphPlotter;
    private static void populateDB() {

        RandomDataGenerator generator = new RandomDataGenerator();
        users = generator.generate();

        graphPlotter = new GraphPlotter(users);
        
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
        System.out.println(getMinimumDistance(firstUser, secondUser));
        System.out.println("\n===================");
        Set<UserNode> friendsOfFriendsAtGivenDistance = getFriendsOfFriendsAtGivenDistance(firstUser, 2);
        graphPlotter.highlightNodes(friendsOfFriendsAtGivenDistance, firstUser);
        System.out.println(friendsOfFriendsAtGivenDistance);
        System.out.println("\n===================");
        getShortestPathsBetweenUsers(firstUser, secondUser);
    }

    private static int getMinimumDistance(UserNode firstUser, UserNode secondUser) {
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
        return distance;
    }

    private static Set<UserNode> getFriendsOfFriendsAtGivenDistance(UserNode firstUser, int distance) {
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
        return friendsOfFriends;
    }

    private static void getShortestPathsBetweenUsers(UserNode firstUser, UserNode secondUser) {
        System.out.println("Shortest paths between "+ firstUser.getFullName() + " and " + secondUser.getFullName()+":");

        List<List<UserNode>> listOfPaths = new ArrayList<>();

        List<UserNode> path = new ArrayList<>();
        pathFinder(firstUser, secondUser, path, listOfPaths);

        System.out.println(listOfPaths);
    }

    private static void pathFinder(UserNode current, UserNode secondUser, List<UserNode> path, List<List<UserNode>> listOfPaths) {
        if (listOfPaths.size() != 0 && listOfPaths.get(0).size() < path.size()) {
            return;
        }
        if (current.equals(secondUser)) {
            path.add(current);
            if (listOfPaths.size() != 0 && path.size() < listOfPaths.get(0).size()) {
                listOfPaths.clear();
            }
            listOfPaths.add(path);
        } else {
            for (UserNode friend : current.getFriends()) {
                if (!path.contains(friend)) {
                    List<UserNode> newPath = new ArrayList<>(path);
                    newPath.add(current);
                    pathFinder(friend, secondUser, newPath, listOfPaths);
                }
            }
        }
    }
}
