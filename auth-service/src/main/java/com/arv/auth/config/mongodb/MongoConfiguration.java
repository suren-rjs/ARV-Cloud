package com.arv.auth.config.mongodb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.index.MongoPersistentEntityIndexResolver;
import org.springframework.data.mongodb.core.mapping.BasicMongoPersistentEntity;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class MongoConfiguration {

    private final MongoTemplate mongoTemplate;

    private final MongoConverter mongoConverter;

    @EventListener(ApplicationReadyEvent.class)
    public void initIndicesAfterStartup() {

        log.info("Mongo InitIndices AfterStartup init");
        var init = System.currentTimeMillis();

        var mappingContext = this.mongoConverter.getMappingContext();

        if (mappingContext instanceof MongoMappingContext) {
            MongoMappingContext mongoMappingContext = (MongoMappingContext) mappingContext;
            mongoMappingContext.setAutoIndexCreation(true);
            for (BasicMongoPersistentEntity<?> persistentEntity : mongoMappingContext.getPersistentEntities()) {
                var clazz = persistentEntity.getType();
                if (clazz.isAnnotationPresent(Document.class)) {
                    var resolver = new MongoPersistentEntityIndexResolver(mongoMappingContext);

                    var indexOps = mongoTemplate.indexOps(clazz);
                    resolver.resolveIndexFor(clazz).forEach(indexOps::ensureIndex);
                }
            }
        }

        log.info("Mongo Init Indices After Startup take: {}", System.currentTimeMillis() - init);
    }
}
