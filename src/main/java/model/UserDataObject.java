package model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import classes.User;

public class UserDataObject {

    private Set<String> usernames = new HashSet<>();
    private Map<String, User> userMap = new HashMap<>();
    private AtomicInteger size = new AtomicInteger();
    private static final UserDataObject INSTANCE = new UserDataObject();

    /**
     * Private constructor for the UserDataObject to implement
     * the Singleton pattern
     */
    private UserDataObject() {
        if (INSTANCE != null) {
            throw new IllegalStateException("Already instantiated the"
                + "UserDataObject.  Please getInstance().");
        }
        DatabaseReference userList = getUserList();
        userList.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,
                    String prevChildKey) {
                // New child added, increment count
                int newSize = size.incrementAndGet();
                System.out.println("Added " + dataSnapshot.getKey()
                    + ", count is " + newSize);
                //update users here
                updateUsers(dataSnapshot.getKey(), newSize);
                updateUserList(dataSnapshot.getKey(),
                    dataSnapshot.getValue(User.class));
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot,
                    String prevChildKey) {
                System.out.println("Edited " + dataSnapshot.getKey()
                    + ", count is " + size);
                //No need to do anything to data.
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("The user "
                    + dataSnapshot.getKey() + " has been deleted");
                updateUsersOnDelete(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot,
                String prevChildKey) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("FireBase error:"
                    + databaseError.getMessage());
            }
        });
    }

    /**
     * Returns the singleton instance
     * @return UserDataObject singleton
     */
    public static UserDataObject getInstance() {
        return INSTANCE;
    }

    /**
     * Returns a reference to the "UserList" key in the database
     * @return UserList database reference
     */
    private DatabaseReference getUserList() {
        return DataManager.getReference("/userList");
    }

    /**
     * Returns a reference to the "Users" key in the database
     * @return Users database reference
     */
    private DatabaseReference getUsers() {
        return DataManager.getReference("/users");
    }

    /**
     * Adds a user to the firebase database
     * @param  userToAdd - user instance to add
     * @param  userName - the key in the database entry
     */
    public void addSingleUser(User userToAdd, String userName) {
        //Map the user with the userName
        DatabaseReference user = getUserList().child("/" + userName);
        user.setValue(userToAdd);
    }

    /**
     * Deletes the User object entry. Be sure to check for existence first.
     * @param  username The username key in the mapping
     */
    public void deleteUser(String username) {
        DatabaseReference user = getUserList().child("/" + username);
        user.removeValue();
        DatabaseReference u = getUsers().child("/" + username);
        u.removeValue();
    }

    /**
     * Determines if the queried userName is in the database
     * @param  userName The username to query
     * @return          True if the user exists in the database
     */
    public Boolean userExists(String userName) {
        return usernames.contains(userName);
    }

    /**
     * Returns the User object mapped to the specified username
     * @param  username The username key in the mapping
     * @return          User object
     */
    public User getUser(String username) {
        return userMap.get(username);
    }

    /**
     * Overwrites the existing mapping associated with the userName key
     * @param  userToAdd New user object to add to the database
     * @param  userName Key to map the user with
     */
    public void editSingleUser(User userToAdd, String userName) {
        addSingleUser(userToAdd, userName);
    }

    /**
     * Helper method from an add callback to userList
     * @param  username - key to update
     * @param  count - the new count of usernames
     */
    private void updateUsers(String username, int count) {
        //Update database
        DatabaseReference users = getUsers();
        users.child("/size").setValue(count);
        users.child("/" + username).setValue(true);
        //Update local storage
        usernames.add(username);
    }

    /**
     * Helper method from the Database callback
     * @param username Key of the object that was reported.
     */
    private void updateUsersOnDelete(String username) {
        userMap.remove(username);
        usernames.remove(username);
        int newCount = size.decrementAndGet();
        getUsers().child("/size").setValue(newCount);
    }

    /**
     * Updates local User storage when a new User is added.
     * @param  username Key to map the User object with
     * @param  user User object from FireBase
     */
    private void updateUserList(String username, User user) {
        userMap.put(username, user);
    }
}
