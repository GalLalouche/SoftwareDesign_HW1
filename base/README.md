# CourseApp: Assignment 1

## Authors
* Ron Yitzhak, 315722744
* Aviad Shiber, 302935697

### Previous assignment
This assignment uses the code from the submission by: <315722744\>-<302935697\>

## Notes

### Implementation Summary
#### Storage Layer
    -UserStorage, ChannelStorage, TokenStorage
        -Wrappers for read and write operations
        -each username is mapped to unique user id
        -each channel name is mapped to unique channel id
        -each <property> of user/channel represented by key-value pair in the form: <id>_<propertyName> -> <propertyValue>
    -StatisticsStorage
        -contains information about user's count and channel's count

#### Managers layer
    -UserManager: User Manager is the entity that responsible to all the actions related to user
        -each user has id, name, status, privilege, and channels list
        -contains avl tree of users, and responsible to update it
            -each user has a representing node in the tree
            -the key of this node is <channels count><user id> (takes care to primary and secondary required order)
    -TokenManager: Token Manager is the entity that responsible to all the actions related to tokens in the system
        -mapping from token to user id
    -ChannelManager: Channel Manager is the entity that responsible to all the actions related to channels in the system
        -each channel has id, name, active users counter and 2 lists: members list and operator list (contains user ids)
        -contains 2 avl trees of channels, and responsible to update them
            -each channel has a representing node in the trees
                -the key of the node in the first tree is <number of users in channel><channel id>
                -the key of the second node is <number of active users in channel><channel id>
    -StatisticsManager(used by User and Channel managers): API to get/set statistics values

#### App Layer
    -implements all course app logic
    -Responsible to the communication between Users and Channels
    (add/remove channel from user's list, add/remove user from channel's lists)


### Testing Summary
- Unit testing:
  - All managers
  - AvlTree
  - CourseApp

### Difficulties
Please list any technological difficulties you had while working on this assignment, especially
with the tools used: Kotlin, JUnit, MockK, Gradle, and Guice.

### Feedback
Please be more clear with complexity requirements / the data structure that we need