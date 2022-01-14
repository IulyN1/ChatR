package com.example.chatr.utils;

import com.example.chatr.domain.Friendship;
import com.example.chatr.domain.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Network {
    int length;
    int[][] array;
    List<User> users;
    Map<User, Integer> map;

    /**
     * Constructior for the Network
     *
     * @param usersList       the list of all the users in the social network
     * @param friendshipsList the list of all the friendships in the social network
     */
    public Network(List<User> usersList, List<Friendship> friendshipsList) {
        length = usersList.size();
        users = usersList;
        array = new int[length][length];
        map = new HashMap<>();
        for (int i = 0; i < usersList.size(); i++)
            map.put(usersList.get(i), i);

        for (Friendship friendship : friendshipsList) {
            int x = map.get(friendship.getUser1());
            int y = map.get(friendship.getUser2());
            array[x][y] = array[y][x] = 1;
        }
    }

    /**
     * Gets the number of connected components in the social network graph
     *
     * @return the number of connected components
     */
    public int nrConnectedComponents() {
        if (length == 0)
            return 0;
        int ct = 0;
        boolean[] nodes = new boolean[length];

        boolean ok = false;
        int first_node = 0;
        while (!ok) {
            ok = true;
            ct++;
            List<Integer> list = new ArrayList<>();

            nodes[first_node] = true;
            list.add(first_node);

            while (!list.isEmpty()) {
                int c = list.remove(0);
                for (int i = 0; i < length; i++) {
                    if (!nodes[i] && array[i][c] == 1) {
                        nodes[i] = true;
                        list.add(i);
                    }
                }
            }

            for (int i = 0; i < length; i++)
                if (!nodes[i]) {
                    first_node = i;
                    ok = false;
                }
        }

        return ct;
    }

    /**
     * Gets the longest path in the social network graph
     *
     * @return the List of Strings with all the users on this path
     */
    public List<String> longestPath() {
        if (length == 0)
            return new ArrayList<>();
        int k = 0;
        int[] ok = new int[length];
        while (k < length) {
            if (ok[k] != 0) {
                k++;
                continue;
            }
            ok[k] = 1;
            List<Integer> queue = new ArrayList<>();
            queue.add(k);
            while (!queue.isEmpty()) {
                int k1 = queue.remove(0);
                for (int i = 0; i < length; i++) {
                    if (ok[i] == 0 && array[i][k1] == 1) {
                        ok[i] = ok[k1] + 1;
                        queue.add(i);
                    }
                }
            }

            k++;
        }
        int max = ok[0], maxi = 0;
        for (int i = 1; i < length; i++) {
            if (max < ok[i]) {
                max = ok[i];
                maxi = i;
            }
        }

        List<String> strings = new ArrayList<>();
        strings.add(users.get(maxi).getFirstName() + " " + users.get(maxi).getLastName());
        max--;
        while (max != 0) {
            for (int i = 0; i < length; i++)
                if (ok[i] == max && array[i][maxi] == 1) {
                    max--;
                    maxi = i;
                    strings.add(users.get(maxi).getFirstName() + " " + users.get(maxi).getLastName());
                    break;
                }
        }

        return strings;
    }
}
