package online.trutz.twitter.samples.model;

import org.springframework.data.mongodb.core.mapping.MongoId;

public record Politiker(@MongoId String id, String name, String username, String haltung) {
}
