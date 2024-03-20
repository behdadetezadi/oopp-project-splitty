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

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import commons.Event;
import commons.Participant;
import commons.Expense;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.glassfish.jersey.client.ClientConfig;

import commons.Quote;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;

public class ServerUtils {
	private static final String SERVER = "http://localhost:8080/";



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


		return new Event("test event");


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
			// first get the participant based on the username provided
			Participant participant = ClientBuilder.newClient(new ClientConfig())
					.target(SERVER)
					.path("api/participants/username/{username}")
					.resolveTemplate("username", username)
					.request(APPLICATION_JSON)
					.accept(APPLICATION_JSON)
					.get(new GenericType<Participant>() {});
			// if the username doesn't exist
			if (participant == null) {
				throw new RuntimeException("Participant not found with the username: " + username);
			}

			Expense expense = new Expense(participant, description, amountValue);
			
			return ClientBuilder.newClient(new ClientConfig())
					.target(SERVER)
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



}