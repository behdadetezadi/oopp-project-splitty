/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.utils;

import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Quote;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;


public class ServerUtils {
	private static final String SERVER = "http://localhost:8080/";
	private static final Client client = ClientBuilder.newClient(new ClientConfig());


	public static Event getEventByInviteCode(String inviteCode) {
		// Placeholder method for retrieving event details from the server
		// You need to implement the actual server communication logic here
		// For now, let's just return a mock event


		/*
		if (inviteCode.equals("valid_invite_code")) {
			return new Event("Sample Event", "Description", "2024-03-20");
		} else {
			return null;
		}
		*/

        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant("Johnny","Depp"));
        participants.add(new Participant("Brad","Pitt"));

		//return new Event("test event", participants);


		Client client = ClientBuilder.newClient();
		try {
			// Send GET request to the server
			Response response = client.target(SERVER)
					.path("api/events/inviteCode/" + Long.parseLong(inviteCode))
					.request(MediaType.APPLICATION_JSON)
					.get();

			// Check if response is successful (status code 200)
			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				// Deserialize the response entity into an Event object
				Event event = response.readEntity(Event.class);
				return event;
			} else {
				// Handle non-OK response status code
				System.err.println("Failed to retrieve event. Status code: " + response.getStatus());
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null; // Handle error appropriately
		} finally {
			client.close(); // Close the client to release resources
		}

	}




	/**
	 * Adds an expense
	 * //TODO
	 *
	 * @param username       the person who paid for the expense
	 * @param description description of the expense
	 * @param amountValue the amount the person has paid for the expense
	 */
	public static Expense addExpense(String username, String description, double amountValue) {
		try {
			Participant participant = client.target(SERVER)
					.path("api/participants/username/{username}")
					.resolveTemplate("username", username)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.get(new GenericType<Participant>() {});
			if (participant == null) {
				throw new RuntimeException("Participant not found with the username: " + username);
			}
			Expense expense = new Expense(participant, description, amountValue);
			return client.target(SERVER)
					.path("api/expenses/")
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.post(Entity.entity(expense, APPLICATION_JSON), Expense.class);
		} catch (NotFoundException e) {
			throw new RuntimeException("Participant not found with the username: " + username);
		} catch (BadRequestException e) {
			throw new RuntimeException("Bad request: " + e.getMessage());
		} catch (RuntimeException e) {
			throw new RuntimeException("Couldn't add the expense: " + e.getMessage());
		}
	}
	/**
	 * Fetches a list of expenses for a specific participant.
	 *
	 * @param participantId The unique identifier of the participant.
	 * @return A list of expenses associated with the given participant.
	 */
	public static List<Expense> getExpensesForParticipant(Long participantId) {
		try {
			return client.target(SERVER)
					.path("api/expenses/participant/{participantId}")
					.resolveTemplate("participantId", participantId)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.get(new GenericType<List<Expense>>() {});
		} catch (NotFoundException e) {
			throw new RuntimeException("No expenses found for participant with ID: " + participantId);
		} catch (Exception e) {
			throw new RuntimeException("Error fetching expenses for participant: " + e.getMessage());
		}
	}

	/**
	 * gets quotes
	 * @throws IOException io exception
	 * @throws URISyntaxException URI syntax exception
	 */
	public void getQuotesTheHardWay() throws IOException, URISyntaxException {
		var url = new URI("http://localhost:8080/api/quotes").toURL();
		var is = url.openConnection().getInputStream();
		var br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			System.out.println(line);
		}
	}

	/**
	 * get quotes
	 * @return a list of quotes
	 */
	public List<Quote> getQuotes() {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/quotes") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
                .get(new GenericType<List<Quote>>() {});
	}

	/**
	 * adds a quote
	 * @param quote a type Quote
	 * @return //TODO
	 */
	public Quote addQuote(Quote quote) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/quotes") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(quote, APPLICATION_JSON), Quote.class);
	}

	/**
	 * adds a participant
	 * @param participant a participant
	 * @return //TODO
	 */
	public static Participant addParticipant(Participant participant) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/participants") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(participant, APPLICATION_JSON), Participant.class);
	}

	/**
	 * adds an event
	 * @param event an event
	 * @return //TODO
	 */
	public static Event addEvent(Event event) {
		return ClientBuilder.newClient(new ClientConfig()) //
				.target(SERVER).path("api/events") //
				.request(APPLICATION_JSON) //
				.accept(APPLICATION_JSON) //
				.post(Entity.entity(event, APPLICATION_JSON), Event.class);
	}


	//TODO send something to make sure it works
    public static void updateEventTitle(Long eventId, String newTitle) {
        try {
            // Encode the new title to ensure proper URL formatting
            String encodedNewTitle = URLEncoder.encode(newTitle, "UTF-8");

            // Construct the URL with the new title as a query parameter
            String urlString = SERVER + "api/events/" + eventId + "/updateTitle?newTitle=" + encodedNewTitle;
            URL url = new URL(urlString);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            // No need for request body in this case

            // Perform the request
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // If needed, read and handle the response here
            } else {
                // Handle unsuccessful response
                System.err.println("Failed to update event title. Response code: " + responseCode);
            }

            // Disconnect the connection
            connection.disconnect();
        } catch (IOException e) {
            //TODO better exception handling
			e.printStackTrace();
            // Handle exception appropriately
        }
    }

	/**
	 * gets all participants (will need changing for different events )
	 * @return an arrray list of participants
	 */
	public static List<Participant> getAllParticipants() {
		try {
			Response response = client.target(SERVER)
					.path("api/participants")
					.request(MediaType.APPLICATION_JSON)
					.get();

			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				return response.readEntity(new GenericType<List<Participant>>(){});
			} else {
				System.err.println("Error fetching participants: " + response.getStatus());
				return Collections.emptyList();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	/**
	 * get the participants by the event id
	 * @param eventId as a long number
	 * @return an array list of participants
	 */
	public static List<Participant> getParticipantsByEventId(long eventId) {
		try {
			Response response = client.target(SERVER)
					.path("api/events/" + eventId + "/participants")
					.request(MediaType.APPLICATION_JSON)
					.get();
			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				return response.readEntity(new GenericType<List<Participant>>(){});
			} else {
				System.err.println("Error getting participants for event with id: " + eventId + ", Status code: " + response.getStatus());
				return Collections.emptyList();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	/**
	 * adds participant to an event
	 * @param eventId long
	 * @param participant a participant
	 * @return a participant
	 */
	public static Participant addParticipantToEvent(long eventId, Participant participant) {
		try {
			return client.target(SERVER)
					.path("api/events/" + eventId + "/participants")
					.request(APPLICATION_JSON)
					.post(Entity.entity(participant, APPLICATION_JSON), Participant.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * updating a participant
	 * @param eventId long
	 * @param participantId long
	 * @param participantDetails participant
	 * @return participant
	 */
	public static boolean updateParticipant(long eventId, long participantId, Participant participantDetails) {
		try {
			Response response = client.target(SERVER)
					.path("api/events/" + eventId + "/participants/" + participantId)
					.request(APPLICATION_JSON)
					.put(Entity.entity(participantDetails, APPLICATION_JSON));

			if (response.getStatusInfo().getFamily() == Response.Status.Family.SUCCESSFUL) {
				Participant updatedParticipant = response.readEntity(Participant.class);
				return updatedParticipant != null; 
			} else {
				System.err.println("Update failed with status code: " + response.getStatus());
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}


	/**
	 * deletes a participant from an event
	 * @param participantId long
	 * @return true if the deletion was successful, false otherwise
	 */
	public static boolean deleteParticipant(long participantId, long eventId) {
		try {
			Response response = client.target(SERVER)
					.path("api/events/"+eventId+"/participants/" + participantId)
					.request(APPLICATION_JSON)
					.delete();

			return response.getStatus() == Response.Status.NO_CONTENT.getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}




}