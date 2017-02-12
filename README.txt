Friend App:
- An app to demonstrate how to create an app with a database using Content Provider.
- Create 3 classes:
	- FriendsDatabase
	- FriendsProvider
	- FriendsContract
- FriendsDatabase:
 - extends SQLiteOpenHelper
 
 Description of the technical feature of the app:
 
 Why we use Content Provider?
 The app might be accessing the database when the user re-orientates their deivce or the user press Home and access another app. That can potentially corrupt the data because the life cycle does not have any idea about the database being accessed. We may have to link these 2 things together, the database and the life cycle.
 
 We can do that by opening our database once and then query/re-query data as needed and only close the database when the activity is destroyed. In other words, we want the life cycle to be aware of the database. There are lots of ways to do it; one way is to use an old deprecated startManagingCursor - you would pass the cursor and then the lifecycles would manage the cursor automatically, but thats not recommended. We can do it manually; we can add code to each lifecycle event to do the relevant thing for the database - e.g. close the database when the Activity is closed. That results to adding of a lot of code to each screen. We can also use AsyncTask as it access the data asynchronously but lots of code.
 
 Here comes the Content Provider.
 
 Standard way to provide access to a structured set of data (SQLite is one of them and also a good example). It is also a core feature of Android Development.
 We setup the Content Provider in our app and then we can provide a mechanism to return those results to the calling process (our app or any other app if we choose to use it). It is used to manage the data and provide back the  data back to the calling process. We do not need Content Provider technically if we do not intend to share the data but its better to use it so that our app is set the right way in the event you want to share data in future.
 Android provide us with a good list of built in Content Providers for audio, video, images etc. It is recommended to use Content Provider in all the database projects because it adds more flexibility and when it comes down to it, is not much more complex than other methods.
 
 How do we access Content Provider?
 By using Cursor Loader. A Cursor Loader run an asynchronous query in the background against a Content Provider and returns the results to an activity or Fragment activity. This means the app continues to work while the database is being accessed.
