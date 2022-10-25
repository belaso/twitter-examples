package online.trutz.twitter.samples.model;

import org.springframework.data.mongodb.core.mapping.MongoId;

public record Tweet(@MongoId String id, String author_id, String text, String created_at, String source,
		PublicMetrics public_metrics) {

}
