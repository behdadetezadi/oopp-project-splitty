
package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Person {
    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	public String firstName;
	public String lastName;

	/**
	 * default constructor for a person
	 */
	@SuppressWarnings("unused")
	protected Person() {
		// for object mapper
	}

	/**
	 * constructor for a person
	 * @param firstName as a string
	 * @param lastName as a string
	 */
	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * getter for the generated id
	 * @return a long number
	 */
	public long getId() {
		return id;
	}

	/**
	 * equals method for a person using an equals builder
	 * @param obj Object
	 * @return a boolean (true or false)
	 */
	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	/**
	 * unique hashcode generator
	 * @return an int
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * to string method for a person
	 * @return a string
	 */
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
	}
}