# Recipe puppy

TechStack: Clean Architecture, MVVM, Dagger2, Coroutines, LiveData, Arrow, Groupie, Retrofit and unit tests.

The challenge: Build a recipes book
This challenge should be done by using the free to use RecipePuppy API. We would like you to retrieve some recipes from there, display the recipes and perform certain operations on those recipes. Hereby the details:
1. For the API connection, you should use their search endpoint and perform recipe searches with one or multiple ingredients and parse the results.
( ie:  http://www.recipepuppy.com/api/?i=onions,garlic&p=1 )
2. Use a search bar as user input for the first point, do the search call after 3 characters are filled to avoid overloading the API with unnecessary calls and show the results in a list with a layout like the picture below. Each recipe should show the image on top, the recipe name, its ingredients (this one could have multiple lines so the layouts should support dynamic heights) and a label in a 45% angle that would show only if it contains lactose (to simplify consider that only milk and cheese contain lactose).
3. Add pagination to the list whenever the user scrolls, this should be as seamless as possible.
4. Each recipe has an  href parameter that is an URL pointing to a website with the recipe details. Whenever the user clicks on a recipe use this parameter to open the website in a new view  without leaving the app.
5. Offline functionality, each recipe should have a favorite button and clicking it should save the full recipe offline. Create a separate screen and a way to access it to show the favorite recipes.

Bonus features Please make sure all previous tasks comply with a proper architecture and have all the tests (Unit, UI, integration...) necessary before going any further.
6. Make the app compatible with tablets and adjust the layout accordingly. In this case we would like to have the list of recipes with two columns and the detailed view should be opened side by side in the list.
7. After the user has scrolled 5 elements, display a native popup dialog prompting the user to rate the app with the text “Rate this app” with three different options:
a. The “Rate now” dismisses the dialog and triggers a new view without leaving the app targeting:  http://www.recipepuppy.com
b. The “No, Thanks” dismisses the dialog and triggers that the rate dialog will not be displayed any longer.
c. The “Later” trigger dismisses the dialog but after scrolling 10 more elements is displayed again.