# NewsReader
News Reader Android mobile app consumes the News API - https://newsapi.org/ . App Home screen displays the latest news


-mobile app that can display the latest news from various news sources (NDTV, CNN, Times, BBC, etc.) in one single place. 
The app should also be able to display the details of the news article.

Features -
  - Users can search for breaking news 
  - Users can click on articles, it will open the article on a webview .
  - RecyclerView uses pagination to optimize api calls
  
This repository demonstrates MVVM and consumes free news api .
 - Architecture - Mvvm , ViewModel & ROOM
 - Dependency Injection - Koin
 - Network - Retrofit & Coroutines
 - Unit Test - KoTest 
   - Kotest file added - com.robo.news.MyKoTestClass
     Junit + Mock test Added - com.robo.news.viewmodel.home.HomeViewModelTest
     Junit + Koin DI fale mocking test added - com.robo.news.viewmodel.home.HomeViewModelTest

Note - Bookmark function & List Ui is not done. 
 Review team can check app build through apk folder & shreenshot folder
 

 

