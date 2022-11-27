import { Application } from "https://deno.land/x/oak@v11.1.0/mod.ts";
import authRouter from "./modules/auth/auth.router.ts";
import checkDataRouter from "./modules/check_data/check_data.router.ts";

const app = new Application();

app.use(authRouter);
app.use(checkDataRouter);

await app.listen({ port: 8000 });
