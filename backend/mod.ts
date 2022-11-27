import { Application } from "https://deno.land/x/oak@v11.1.0/mod.ts";
import authRouter from "./modules/auth/auth.router.ts";

const app = new Application();

app.use(authRouter);

await app.listen({ port: 8000 });
