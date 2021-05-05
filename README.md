# EnocTest

Libraries used:
- Android X Card View
- Retrofit
- Glide
- Coroutines
- Timber for logging
- LiveData for LifeCycle management


Architecture used:
- Model View ViewModel (MVVM) with Live Data and Coroutines.

Architectural Layers:
- Views layer
- View Models laher
- Models layer
- Repository layer
- Network layer

Q) How did you ensure that the display of the avatar image (from a remote URL) gave the best user experience?
A) We are using Glide library for downloading image from the URL which has a safe fallback if image is not loaded. It is thread safe so if due to slow internet if iageis loaded slowly and user navigates from that screen then it handles the fallback with the activity lifecycle. It also caches the image once loaded so everytime its not calling the remote URL.

Q) How did you set up the app so that it was automatically logged in for the user on subsequent uses?
A) We are storing the app token which was received from the login API and whenever user comes back we are checking if we have the token then navigate forward to Profile screen.
There is one more thing, on the profile screen we are calling API to fetch user information, if server returns Unauthenticated error then we logout user and return back to the login screen (Not implemented)

Q) How did you cope with building the sample app without having access to the real back-end API?
A) I tried using Githib pages for hardcoded responses but got some issue, and due to time limitation then I fallback to use Charles Proxy debugger in which I mapped my API calls to the local files with responses (which I have also attached within the repository) to continue with the flow and test everything.

Q) What testing did you do, and why?
A) On Login screen:
    * Email and password fields should not be empty
    * Backend should respond with valid data otherwise display error on the login screen

On Profile Screen:
    * Checked with giving and revoking permissions to access the gallery to select a photo so every flow works fine
    * User returned from the backend should be a valid user.

I was not able to comlete the testing with Unit tests (at least for the business layer).


TASKS DONE:

Required:
1. Build a simple Android app that uses standard native interface controls to provide the login and account profile views.
- DONE

2. Ensure that on subsequent invocations of the app, that the user is automatically logged in (assuming at least one successful previous login).
- DONE

3. Add the ability for the user to change their avatar using either by taking a photo with the inbuilt camera, or by selecting a photo from their photo library.
- DONE

4. Ensure that when the device captures a new image that the data sent back to the back-end is not greater than 1mb.
- DONE


STRETCH:
1. Add the ability for the app to display the user’s Gravatar [see: www.gravatar.com] if a) a Gravatar for their email exists, and b) they have not specifically set up their own photo.
- DONE

2. Display the user’s avatar in a circle. Ensure that the photo is correctly positioned within the display area.
- DONE

3. Apply a filter to the image (of your choice) prior to uploading the the back-end.
- DONE

4. Implement the app using a Redux [ref] architecture (or equivalent), or another suitable framework of your choice.
- DONE (Used MVVM Architecture with Live Data and Coroutines)
