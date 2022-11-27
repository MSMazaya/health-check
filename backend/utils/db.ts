import { Bson, MongoClient } from "https://deno.land/x/mongo@v0.31.1/mod.ts";
import { config } from "https://deno.land/x/dotenv@v1.0.1/mod.ts";

config({ export: true });
const env = Deno.env.toObject();

const client = new MongoClient();

await client.connect(env.MONGO_URI);

export default client.database("tubes_ltf_3");
