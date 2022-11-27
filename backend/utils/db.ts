import { Bson, MongoClient } from "https://deno.land/x/mongo@v0.31.1/mod.ts";

const env = Deno.env.toObject();

const client = new MongoClient();

await client.connect(env.MONGO_URI);

export default client.database("tubes_ltf_3");
