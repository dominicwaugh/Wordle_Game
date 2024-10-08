### Big-Oh  
- isValidSequence()  
  - The Big-Oh for the original isValidSequence is O(N) because it is one loop through the ArrayList
  - The Big-Oh for the recursive isValidSequence_red is O(N).
  - The Big-Oh for the recursive binary isValidSequence_binary is O(log(N)) because it is a binary search
  - The Big-Oh for the isSorted method is O(N) because it goes through the ArrayList linearly.

### Feedback response
Thank you for your feedback! I recognized the problem in the program and have fixed it for you.
The program was already designed to use any file given to it. In order to be able to use dictionaries in other
languages and such, I had to make sure that all the functions of the program passed the data
in the format of words rather than numbers. In order to be able to handle larger datasets and dictionaries,
I implemented a function that would search sorted dictionaries (alphabetical or numerical order) in a
specific way that wouldn't have to search the whole dictionary in order to save time and work more efficiently.