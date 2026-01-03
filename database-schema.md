## Schema de la base de données

## Resume des collections
PicturePost:    travelshare_picture_posts/
SharedTo:       travelshare_sharedto/
Comment:        travelshare_comments/
Tags:           travelshare_tags/
User:           travelshare_users/
UserGroup:      travelshare_usergroups/
Follows:        travelshare_follows/
Like:           travelshare_likes/
Report:         travelshare_reports/

## Post

PicturePost:    travelshare_picture_posts/
    - id: String                # unique
    - authorId: String
    - description: String
    - instructions: String
    - date: Timestamp           # date de la photo
    - createdAt: Timestamp      # qd la photo a été envoyée sur le serveur 
    - visibility: boolean
    - location: Location

Location:
    - type: String              # APPROXIMATE ou EXACT
    - latitude: double
    - longitude: double
    - name: String
    - city: String
    - region: String
    - country: String

Comment:    travelshare_comments/
    - id: String                # unique
    - authorId: String
    - content: String
    - createdAt: Timestamp

SharedTo:    travelshare_sharedto/
    - id: String                # unique
    - fromUserId: String
    - postId: String
    - userGroupId: String
    
Tags:   travelshare_tags/
    - id: String
    - tagName: String

Annotation:
    - annotationType: String    # TEXT ou AUDIO ou ASSISTED (IA)
    - createdAt: Timestamp

## User

User:    travelshare_users/
    - id: String                    # unique
    - userType: String              # GUEST or CONNECTED
    - createdAt: Timestamp

UserGroup:    travelshare_usergroups/
    - id: String                    # unique
    - usersId: ArrayList<String>    # members
    - createdAt: Timestamp

Follows:    travelshare_follows/
    - id: String                # unique
    - fromUserId: String
    - followableContentId: String
    - folloawbleContentType: String
    - createdAt: Timestamp

Like:   travelshare_likes/
    - id: String                # unique
    - fromUserId: String
    - postId: String
    - createdAt: Timestamp

Report:     travelshare_reports/
    - id: String
    - message: String
    - category: String
    - createdAt: Timestamp
