android-basic-twitter-client
============================

Basic Twitter client

Updated Stories:
- User login screen is branded
- There are two fragments - Home and Mentions
- Both show the timelines for respective headings
- User can infinitely scroll
- User data and tweets is persisted in  tables
- User can reply on any tweet - the handle is filled in properly, remaining chars work fine.
- User can compose tweet as usual.
- User can retweet
- User can favourite a tweet
- User can click on the image on any tweet and that users profile is loaded
- User can click on action bar to get his own profile
- In any user profile, one can see profile image, tag line, number of tweets, number of followers, number of followings

Implemented following advanced stories:
- Can reply to any tweet
- Can favorite, retweet any tweet
- Used pager (previous commits had ActionBar tabs too)


Provides the following Stories:

- User can sign in to Twitter using OAuth login
- User can view the tweets from their home timeline
- User should be able to see the username, name, body and image for each tweet
- User should be displayed the relative timestamp for a tweet "8m", "7h"
- User can view more tweets as they scroll with infinite pagination
- User can compose a new tweet
- User can click a “Compose” icon in the Action Bar on the top right
- User can then enter a new tweet and post this to twitter
- User is taken back to home timeline with new tweet visible in timeline

Completed following optional stories
- Links in tweets are clickable and will launch the web browser (see autolink)
- User can see a counter with total number of characters left for tweet
- User can refresh tweets timeline by pulling down to refresh (i.e pull-to-refresh)
- Improved the user interface and theme the app to feel "twitter branded"
- Compose activity is replaced with a modal overlay

Missed - ActiveAndroid - couldnt get a stable app, but will continue to push once it is done.

![Video Walkthrough](demo_new_twitter_client.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).
