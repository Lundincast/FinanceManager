package com.lundincast.presentation.model;


import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

/**
 * A class to perform migration of Realm file from initial version to latest version
 */
public class Migration implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // During a migration, a DynamicRealm is exposed. A DynamicRealm is an untyped variant of a normal Realm, but
        // with the same object creation and query capabilities.
        // A DynamicRealm uses Strings instead of Class references because the Classes might not even exist or have been
        // renamed.

        // Access the Realm schema in order to create, modify or delete classes and their fields.
        RealmSchema schema = realm.getSchema();

        // Migrate from version 1 to version 2
        if (oldVersion == 1) {
            // Create new OverheadModel class
            RealmObjectSchema overheadSchema = schema.create("OverheadModel")
                    .addField("overheadId", int.class, FieldAttribute.PRIMARY_KEY)
                    .addField("price", double.class)
                    .addRealmObjectField("category", schema.get("CategoryModel"))
                    .addField("dayOfMonth", short.class)
                    .addField("comment", String.class);

            oldVersion++;
        }
    }
}
