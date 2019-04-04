# Web page parser

## Program description

This program retrieves the link and depth of search that user specifies (1-5). The result of
execution is list of emails found at specified amount of levels. First level is the page specified
by user, the second level of pages are all page addresses found at the first page level, etc.

## App origin source link: 
https://github.com/NikitaHrinko/android_sample_parser

## Project structure description:

Project contains 5 packages: 
1. Root package (webpageparser) with the app main class. MainActivity class is responsible for 
processing of user input and output of information on the screen.
2. Package exception: contains custom exceptions that may occur during the app execution. These
 exceptions are used to send messages with information about app execution, such as: invalid 
 links that can't be processed (InvalidURLException), fails while loading (PageLoadingException) 
 or reading (PageReadingException) page.
3. Package interaction: contains classes that are used to pass messages to the UI about the 
execution process, such as error reports and progress reports, and additional constants to 
process these messages accordingly.
4. Package parser: contains classes responsible for text processing according to the project 
description. 
5. Package reader: contains class responsible for loading of web-pages from the internet and 
reading those pages line by line.

### `MainActivity` class

1. Class Fields

This class contains 4 types of fields: objects that represent every control element placed on 
the app main page, list for invalid links that couldn't be processed by the app, 
`processingThread` that stores current thread busy with processing of web-pages, 
`messageHandler` responsible for UI updates based on information, retrieved from processing 
thread, as well as releasing processing thread when it's done (by receiving message after it 
ends). `messageHandler` can't be made a static class (as AndroidStudio IDE suggests) because 
this object is bound to the single instance of `MainActivity` class and must have access to 
it's fields to modify information on the screen. 

2. Class methods

`showAlert` method is used to show messages in case an error occurs during the execution.

`onCreate` method is responsible for binding actual UI items to fields of class, as well as 
binding of processing action to the button on the UI. This action starts processing when there 
is no other thread already busy with same task, gets the depth and URL from the UI and passes 
this data to the `DataExtractor` instance, calls for email extraction and passes the results to 
`messageHandler`. 

3. Progress

Progress is split into 2 stages: page reading and text parsing. The ammount of pages that has 
to be processed is unknown because of the fact that every page on current level has to be read 
and processed to find links to other pages, and when it's done, all located links have to be 
processed the same way. This process repeats before it reaches the deepest level possible (1-5, 
based on user input). Afterwards every page concatenated in the same list is sent to find all 
the emails. The first process takes up most of the processing time due to the fact that reading 
pages from the internet is almost always slower than the phone can process this text.

### `MessageDispatcher` class

This class contains two `dispatch` methods: one of those has `String messageText` parameter 
that sets `String` value in message bundle, the other one has parameter `float messageNumber` 
that sets `float` value in message bundle. The first one is used to send text messages to the 
UI, such as links failed to parse and final list of emails. The second one is used to change 
progress value.

### `DataExtractor` class

This class is responsible for text concatenation, processing and sending messages to the UI. 

Class methods:

1. `getFullTesxt` - gets visited links, currently processed page link and depth level. This is 
a recursive procedure that stops when current depth reaches 0. If currently processed page was 
already visited (may happen due to the fact that pages have links to one-another) it doesn't 
process it, otherwise it reads this page and concatenates reading result with result of the 
same process on every page from links listed on this one. As a result, it returns list with 
every line of every page that has to be processed.
2. `getEmails` - calls previous procedure for the link specified by the user and gets every 
email located on the pages found.
3. `dispatchMessages` sends messages that occured during the execution of previously described 
methods.

### `TextParser` class

This class contains methods that extract text by specified regular expression pattern. Methods 
use standard java regexp utility package. Regular expressions are made to parse basic emails 
and links.

### `SourceTextReader` class

This class is used to read specified pages from the internet and return them as lists of text 
lines. In the process there may appear exceptions that depend on hardware, internet connection 
etc, so it's out of programmers control and has to be processed in some way. This is where custom 
exceptions come in handy, to send specific messages to upper levels for processing.
