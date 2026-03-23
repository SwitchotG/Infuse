package org.infuse.plugin.Class.Ray;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.ExtraInfo;
import com.hypixel.hytale.codec.schema.SchemaContext;
import com.hypixel.hytale.codec.schema.config.Schema;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.BsonValue;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ReactionCodec implements Codec<IRayReaction> {

    @Override
    public IRayReaction decode(BsonValue bsonValue, ExtraInfo extraInfo) {
        if (!bsonValue.isDocument()) {
            throw new IllegalArgumentException("Expected document");
        }

        BsonDocument doc = bsonValue.asDocument();

        if (!doc.containsKey("type")) {
            throw new IllegalArgumentException("Missing 'type' field in reaction");
        }

        BsonValue typeValue = doc.get("type");
        if (!typeValue.isString()) {
            throw new IllegalArgumentException("'type' must be a string");
        }

        String type = typeValue.asString().getValue();

        Codec<? extends IRayReaction> codec = ReactionRegistry.getCodec(type);
        if (codec == null) {
            throw new IllegalArgumentException("Unknown reaction type: " + type);
        }

        return codec.decode(doc, extraInfo);
    }

    @Override
    public BsonValue encode(IRayReaction value, ExtraInfo extraInfo) {

        String id = ReactionRegistry.getId(value.getClass());
        if (id == null) {
            throw new IllegalArgumentException("Unregistered reaction: " + value.getClass());
        }

        @SuppressWarnings("unchecked")
        Codec<IRayReaction> codec =
                (Codec<IRayReaction>) ReactionRegistry.getCodec(id);

        BsonValue encoded = codec.encode(value, extraInfo);

        if (!encoded.isDocument()) {
            throw new IllegalStateException("Reaction codec must return a document");
        }

        BsonDocument original = encoded.asDocument();
        BsonDocument doc = (BsonDocument)original.clone();

        doc.put("type", new BsonString(id));

        return doc;
    }

    @Nonnull
    @Override
    public Schema toSchema(@Nonnull SchemaContext schemaContext) {
        Schema[] schemas = ReactionRegistry.getAll().values().stream()
                .map(codec -> codec.toSchema(schemaContext))
                .toArray(Schema[]::new);

        return Schema.anyOf(schemas);
    }
}
