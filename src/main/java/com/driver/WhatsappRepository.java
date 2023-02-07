package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class WhatsappRepository {

    //Assume that each user belongs to at most one group
    //You can use the below mentioned hashmaps or delete these and create your own.
    private HashMap<Group, List<User>> groupUserMap;
    private HashMap<Group, List<Message>> groupMessageMap;
    private HashMap<Message, User> senderMap;
    private HashMap<Group, User> adminMap;
    private HashSet<String> userMobile;
    private int customGroupCount;
    private int messageId;

    public WhatsappRepository(){
        this.groupMessageMap = new HashMap<Group, List<Message>>();
        this.groupUserMap = new HashMap<Group, List<User>>();
        this.senderMap = new HashMap<Message, User>();
        this.adminMap = new HashMap<Group, User>();
        this.userMobile = new HashSet<>();
        this.customGroupCount = 0;
        this.messageId = 0;
    }

    public String createUser(String name, String mobile) throws Exception {
        //If the mobile number exists in database, throw "User already exists" exception
        //Otherwise, create the user and return "SUCCESS"

        if(userMobile.contains(mobile)){
            throw new Exception("User already exists");
        }

        User user=new User(name, mobile);

        userMobile.add(mobile);

        return "SUCCESS";
    }


    public Group createGroup(List<User> users) {
        // The list contains at least 2 users where the first user is the admin. A group has exactly one admin.
        // If there are only 2 users, the group is a personal chat and the group name should be kept as the name of the second user(other than admin)
        // If there are 2+ users, the name of group should be "Group count". For example, the name of first group would be "Group 1", second would be "Group 2" and so on.
        // Note that a personal chat is not considered a group and the count is not updated for personal chats.
        // If group is successfully created, return group.

        //For example: Consider userList1 = {Alex, Bob, Charlie}, userList2 = {Dan, Evan}, userList3 = {Felix, Graham, Hugh}.
        //If createGroup is called for these userLists in the same order, their group names would be "Group 1", "Evan", and "Group 2" respectively.

        int numberOfParticipants=users.size();
        String name="";
        if(numberOfParticipants==2){
            name=users.get(1).getName();
        }
        else {
            customGroupCount++;
            name="Group "+customGroupCount;
        }

        //create group, add map entry for groups and list of users and add map entry for group and admin user
        Group group=new Group(name, numberOfParticipants);
        groupUserMap.put(group,users);
        adminMap.put(group, users.get(0));

        return group;
    }


    public int createMessage(String content) {
        // The 'i^th' created message has message id 'i'.
        // Return the message id.

        messageId++;
        Message message=new Message(messageId, content);

        return messageId;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{


        //Throw "Group does not exist" if the mentioned group does not exist
        if(!groupUserMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }

        //Throw "You are not allowed to send message" if the sender is not a member of the group
        if(!groupUserMap.get(group).contains(sender)){
            throw new Exception("You are not allowed to send message");
        }
        //if group not in  group message map add map entry
        if(!groupMessageMap.containsKey(group)){
            groupMessageMap.put(group, new ArrayList<>());
        }
        //update add messsage to the group map entry
        groupMessageMap.get(group).add(message);


        //update sender map
        senderMap.put(message,sender);

        //If the message is sent successfully, return the final number of messages in that group.
        return groupMessageMap.get(group).size();
    }


    public String changeAdmin(User approver, User user, Group group) throws Exception{
        //Throw "Group does not exist" if the mentioned group does not exist
        if(!groupUserMap.containsKey(group) || !adminMap.containsKey(group)){
            throw new Exception("Group does not exist");
        }

        //Throw "Approver does not have rights" if the approver is not the current admin of the group
        if(!adminMap.get(group).equals(approver)){
            throw new Exception("Approver does not have rights");
        }

        //Throw "User is not a participant" if the user is not a part of the group
        if(!groupUserMap.get(group).contains(user)){
            throw new Exception("User is not a participant");
        }

        //Change the admin of the group to "user" and return "SUCCESS". Note that at one time there is only one admin and the admin rights are transferred from approver to user.
        //changing map entry for group admin
        adminMap.put(group,user);

        return "SUCCESS";
    }


    public int removeUser(User user) throws Exception{
        //This is a bonus problem and does not contains any marks
        //A user belongs to exactly one group
        //If user is not found in any group, throw "User not found" exception
        //If user is found in a group and it is the admin, throw "Cannot remove admin" exception
        //If user is not the admin, remove the user from the group, remove all its messages from all the databases, and update relevant attributes accordingly.
        //If user is removed successfully, return (the updated number of users in the group + the updated number of messages in group + the updated number of overall messages)

        return 1;
    }


    public String findMessage(Date start, Date end, int K) throws Exception{
        //This is a bonus problem and does not contains any marks
        // Find the Kth latest message between start and end (excluding start and end)
        // If the number of messages between given time is less than K, throw "K is greater than the number of messages" exception

        return "";
    }
}
