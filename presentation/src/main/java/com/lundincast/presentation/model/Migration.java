package com.lundincast.presentation.model;


import io.realm.DynamicRealm;
import io.realm.DynamicRealmObject;
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

        // Migrate from version 2 to version 3
        if (oldVersion == 2) {
            // Create new AccountModel class
            RealmObjectSchema accountSchema = schema.create("AccountModel")
                    .addField("id", long.class, FieldAttribute.PRIMARY_KEY)
                    .addField("name", String.class)
                    .addField("color", int.class)
                    .addField("balance", double.class);

            // Add transactionType field in TransactionModel and populate existing records
            schema.get("TransactionModel")
                    .addField("transactionType", String.class, FieldAttribute.REQUIRED)
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.setString("transactionType", "Expense");
                        }
                    });

            // Add fromAccount field in TransactionModel and set it to null
            schema.get("TransactionModel")
                    .addRealmObjectField("fromAccount", schema.get("AccountModel"))
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("fromAccount", null);
                        }
                    });

            oldVersion++;
        }

        // Migrate from version 3 to version 4
        if (oldVersion == 3) {
            // Add toAccount field in TransactionModel and set it to null
            schema.get("TransactionModel")
                    .addRealmObjectField("toAccount", schema.get("AccountModel"))
                    .transform(new RealmObjectSchema.Function() {
                        @Override
                        public void apply(DynamicRealmObject obj) {
                            obj.set("toAccount", null);
                        }
                    });

            oldVersion++;
        }
    }
}
