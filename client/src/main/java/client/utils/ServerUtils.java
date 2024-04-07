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
import java.lang.reflect.Type;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;


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


	public static Participant getParticipant(long participantId) {
		Response response = client.target(SERVER)
				.path("api/participants/{id}")
				.resolveTemplate("id", participantId)
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
				.get();
		if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
			throw new RuntimeException("Participant not found with the id: " + participantId);
		}else if (response.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL) {
			throw new RuntimeException("Failed to retrieve participant. Status code: " + response.getStatus());
		}
		return response.readEntity(Participant.class);
	}


	/**
	 * Adds an expense to an event
	 * @param participantId the person who paid for the expense
	 * @param description description of the expense
	 * @param amountValue the amount the person has paid for the expense
	 */
	public static Expense addExpense(long participantId, String description, double amountValue, long eventId) {
		try {
			Participant participant = getParticipant(participantId);
			Expense expense = new Expense(participant, description, amountValue,eventId);
			System.out.println(eventId);
			return client.target(SERVER)
					.path("api/events/{eventId}/expenses")
					.resolveTemplate("eventId", eventId)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.post(Entity.entity(expense, APPLICATION_JSON), Expense.class)
					;
		} catch (BadRequestException e) {
			throw new RuntimeException("Bad request: " + e.getMessage());
		} catch (RuntimeException e) {
			throw new RuntimeException("Couldn't add the expense: " + e.getMessage());
		}
	}

	/**
	 * Deletes an expense from the database
	 * @param expenseId the id of the expense
	 */
	public static void deleteExpense(long expenseId, long eventId){
		try{
			Response response = deleteExpenseFromEvent(expenseId, eventId);
			if(response.getStatus() == Response.Status.OK.getStatusCode()) {
				response = client.target(SERVER)
						.path("api/expenses/{id}")
						.resolveTemplate("id", expenseId)
						.request(APPLICATION_JSON)
						.delete();
				if(response.getStatus() == Response.Status.OK.getStatusCode()) {
					System.out.println("Expense deleted successfully");
				} else if (response.getStatus() == Response.Status.NOT_FOUND.getStatusCode()) {
					throw new RuntimeException("Expense not found with ID: " + expenseId);
				} else {
					throw new RuntimeException("Failed to delete expense. HTTP status code: " + response.getStatus());
				}
			} else{
				throw new RuntimeException("Failed to remove expense from event. HTTP status code: " + response.getStatus());
			}
		} catch(RuntimeException e){
			throw new RuntimeException("Failed to delete expense: " + e.getMessage());
		}
	}

	/**
	 * Deletes an event by its ID.
	 * @param eventId The ID of the event to be deleted.
	 * @return A boolean indicating whether the deletion was successful.
	 */
	public static boolean deleteEvent(long eventId) {
		Response response = null;
		try {
			response = client.target(SERVER)
					.path("api/events/{eventId}")
					.resolveTemplate("eventId", eventId)
					.request()
					.delete();

			if (response.getStatus() == Response.Status.NO_CONTENT.getStatusCode()) {
				return true;
			} else {
				System.err.println("Failed to delete event. Status code: " + response.getStatus());
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	/**
	 * deletes an expense from the event
	 * @param expenseId the expense id
	 * @param eventId the event id
	 * @return a response
	 */
	private static Response deleteExpenseFromEvent(long expenseId, long eventId) {
		Response response = client.target(SERVER)
				.path("api/events/{eventId}/expenses/{expenseId}")
				.resolveTemplate("eventId", eventId)
				.resolveTemplate("expenseId", expenseId)
				.request(APPLICATION_JSON)
				.delete();
		return response;
	}

	/**
	 * Updates an existing expense in an event
	 * @param expenseId the id of the expense that needs to be updated
	 * @param updatedExpense the new updated version of the expense
	 */
	public static void updateExpense(long expenseId, Expense updatedExpense, long eventId){
		try {
			client.target(SERVER)
					.path("api/events/{eventId}/expenses/{expenseId}")
					.resolveTemplate("eventId", eventId)
					.resolveTemplate("expenseId", expenseId)
					.request()
					.put(Entity.entity(updatedExpense, APPLICATION_JSON));
		} catch(NotFoundException e) {
			throw new RuntimeException("Expense not found with ID: " + expenseId);
		} catch(RuntimeException e) {
			throw new RuntimeException("Couldn't update the expense: " + e.getMessage());
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
		} catch (Exception e) {
			return new ArrayList<>();
		}
//		 catch (Exception e) {
//			throw new RuntimeException("Error fetching expenses for participant: " + e.getMessage());
//		}
	}
	/**
	 * Fetches a list of expenses for a specific event.
	 *
	 * @param eventId The unique identifier of the event.
	 * @return A list of expenses associated with the given event.
	 */
	public static List<Expense> getExpensesForEvent(Long eventId) {
		try {
			List<Expense> expenses = client.target(SERVER)
					.path("api/events/{eventId}/expenses")
					.resolveTemplate("eventId", eventId)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.get(new GenericType<List<Expense>>() {});
			return expenses;
		} catch (NotFoundException e) {
			// Instead of throwing an exception, return an empty list
			return new ArrayList<>();
		} catch (Exception e) {
			throw new RuntimeException("Error fetching expenses for event: " + e.getMessage());
		}
	}

	/**
	 * adds a participant
	 * @param participant a participant
	 * @return //TODO
	 */
	public static Participant addParticipant(Participant participant) {
		return ClientBuilder.newClient(new ClientConfig())
				.target(SERVER).path("api/participants")
				.request(APPLICATION_JSON)
				.accept(APPLICATION_JSON)
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
	 * Fetches all events from the server.
	 * @return A list of all events.
	 */
	public static List<Event> getAllEvents() {
		Response response = null;
		try {
			response = client.target(SERVER)
					.path("api/events")
					.request(MediaType.APPLICATION_JSON)
					.get();
			if (response.getStatus() == Response.Status.OK.getStatusCode()) {
				return response.readEntity(new GenericType<List<Event>>(){});
			} else {
				System.err.println("Failed to retrieve all events. Status code: " + response.getStatus());
				return Collections.emptyList();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		} finally {
			if (response != null) {
				response.close();
			}
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

			return response.getStatus() == Response.Status.OK.getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Long polling of Participants
	 * @param eventId as a long
	 * @return an array list of participants
	 */

	private static final ExecutorService EXEC = Executors.newSingleThreadExecutor();

	public static void registerForUpdates(long eventId, Consumer<Participant> consumer) {
		EXEC.submit(()->{
			while (!Thread.interrupted()) {
				var res = ClientBuilder.newClient(new ClientConfig())
						.target(SERVER).path("api/events/" + eventId + "/participants/updates")
						.request(APPLICATION_JSON)
						.accept(APPLICATION_JSON)
						.get(Response.class);
				if (res.getStatus()==204){
					continue;
				}
				var q = res.readEntity(Participant.class);
				consumer.accept(q);
			}
		});

	}

	public void stop(){
		EXEC.shutdownNow();
	}

	private StompSession session = connect("ws://localhost:8080/websocket");
	private StompSession connect (String url){
		var client = new StandardWebSocketClient();
		var stomp = new WebSocketStompClient(client);
		stomp.setMessageConverter(new MappingJackson2MessageConverter());
		try{
			return stomp.connect(url, new StompSessionHandlerAdapter() {}).get();}
		catch(InterruptedException e ){
			Thread.currentThread().interrupt();
		}
		catch (ExecutionException e ){
			throw new RuntimeException(e);
		}
		throw new IllegalStateException();
	}

	public void registerForMessages(String dest, Long eventId, Long participantId, Consumer<Participant> consumer){
		session.subscribe(dest, new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return Participant.class;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				consumer.accept((Participant) payload);
			}
		});
	}

	public void send(String dest, Object o){
		session.send(dest,o);
	}

	public void registerForEventUpdates(String dest, long eventId, String newTitle, Consumer<Event> consumer){
		session.subscribe(dest, new StompFrameHandler() {
			@Override
			public Type getPayloadType(StompHeaders headers) {
				return Event.class;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				consumer.accept((Event) payload);
			}
		});

	}

}