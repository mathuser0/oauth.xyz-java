package io.bspk.oauth.xyz.data;

import java.time.Duration;
import java.time.Instant;

import org.apache.commons.lang3.RandomStringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author jricher
 *
 */
@Data
@Accessors(chain = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Handle {

	public enum Presentation {
		BEARER,
		SHA3;

		@JsonCreator
		public static Presentation fromJson(String key) {
			return key == null ? null :
				valueOf(key.toUpperCase());
		}

		@JsonValue
		public String toJson() {
			return name().toLowerCase();
		}
	}

	private String value;
	private Presentation presentation;
	private Instant expiration;

	@JsonIgnore
	public boolean expires() {
		return getExpiration() != null;
	}


	// factory methods

	/**
	 * Create a handle with a random value and no expiration
	 */
	public static Handle create() {
		return new Handle().setValue(RandomStringUtils.randomAlphanumeric(64))
			.setPresentation(Presentation.BEARER);
	}

	public static Handle createSha3() {
		return new Handle().setValue(RandomStringUtils.randomAlphanumeric(64))
			.setPresentation(Presentation.SHA3);
	}

	/**
	 * Create a handle with a random value and an expiration based on the lifetime
	 */
	public static Handle create(Duration lifetime) {
		return create().setExpiration(Instant.now().plus(lifetime));
	}
}
